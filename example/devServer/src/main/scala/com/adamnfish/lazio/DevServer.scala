package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, LambdaLogger, Context => LambdaContext}
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import zio.DefaultRuntime
import zio.console.Console
import io.javalin.Javalin
import io.javalin.http.{Context, HandlerType}


object DevServer {
  val handler = new Lambda {
    // Override these to provide dev dependencies
    override type Environment = Console
    override val runtime = new DefaultRuntime {}
  }

  def main(args: Array[String]): Unit = {
    val app = Javalin.create
    // add handlers for all supported HTTP methods
    // to mimic API Gateway's proxy integration
    HandlerType.values().foreach { handlerType =>
      app.addHandler(handlerType, "*", handleRequest)
    }

    app.start(7000)
  }

  def handleRequest(ctx: Context): Unit = {
    val requestEvent = new APIGatewayProxyRequestEvent()
      .withBody(ctx.body())
      .withHeaders(ctx.headerMap())
      .withPath(ctx.path())
      .withHttpMethod(ctx.method())
      .withIsBase64Encoded(false)
    val lambdaContext = new FakeLambdaContext

    val program = handler.handle(requestEvent, lambdaContext)
    val response = handler.runtime.unsafeRun(program)

    ctx.status(response.statusCode)
    response.headers.foreach { case (k, v) =>
      ctx.header(k, v)
    }
    ctx.result(response.body)
  }
}
