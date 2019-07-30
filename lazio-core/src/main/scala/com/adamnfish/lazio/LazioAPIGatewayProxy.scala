package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import zio.{DefaultRuntime, ZIO}


trait LazioAPIGatewayProxy extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent] {
  private val runtime = new DefaultRuntime {}

  type ApiGatewayProxy = ZIO[(APIGatewayProxyRequestEvent, Context), String, APIGatewayProxyResponseEvent]

  override def handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = {
    val result = handler.provide((input, context))
    runtime.unsafeRun(result)
  }

  val handler: ZIO[(APIGatewayProxyRequestEvent, Context), String, APIGatewayProxyResponseEvent]
}
