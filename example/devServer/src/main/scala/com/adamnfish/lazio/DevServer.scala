package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import io.javalin.Javalin
import io.javalin.http.{Context, HandlerType}
import zio.DefaultRuntime

import scala.Console._
import scala.io.StdIn


object DevServer {

  val devLambda = new Lambda {
    // Override the runtime to provide dev dependencies for the program's execution
    override val runtime = new DefaultRuntime {}
  }


  def main(args: Array[String]): Unit = {
    val app = Javalin.create
    // handlers for HTTP methods to mimic API Gateway's proxy integration
    List(
      HandlerType.GET, HandlerType.POST,
      HandlerType.PUT, HandlerType.DELETE,
      HandlerType.PATCH, HandlerType.TRACE,
    ).foreach { handlerType =>
      app.addHandler(handlerType, "*", handleRequest)
    }
    app.start(7000)

    println(s"$BOLD$GREEN_B${WHITE}Lambda dev server running: ${RED}ctrl+d$WHITE to stop$RESET")

    // listen for `ctrl+d` to stop the dev server
    var running = true
    while(running){
      StdIn.readLine match {
        case x if x == null =>
          app.stop()
          running = false
        case _ =>
      }
    }
  }

  def handleRequest(ctx: Context): Unit = {
    val requestEvent = new APIGatewayProxyRequestEvent()
      .withBody(ctx.body())
      .withHeaders(ctx.headerMap())
      .withPath(ctx.path())
      .withHttpMethod(ctx.method())
      .withIsBase64Encoded(false)
    val lambdaContext = new FakeLambdaContext

    val program = devLambda.handle(requestEvent, lambdaContext)
    val response = devLambda.runtime.unsafeRun(program)

    ctx.status(response.statusCode)
    response.headers.foreach { case (k, v) =>
      ctx.header(k, v)
    }
    ctx.result(response.body)
  }
}
