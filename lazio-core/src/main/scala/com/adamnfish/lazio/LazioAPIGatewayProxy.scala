package com.adamnfish.lazio

import com.adamnfish.lazio.model.ApiGatewayProxyResponse
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import zio.ZIO

import scala.jdk.CollectionConverters._


trait LazioAPIGatewayProxy extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent] {
  type Environment

  val runtime: zio.Runtime[Environment]

  def handle(request: APIGatewayProxyRequestEvent, context: Context): ZIO[Environment, Nothing, ApiGatewayProxyResponse]

  override def handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = {
    val result = runtime.unsafeRun(handle(input, context))
    new APIGatewayProxyResponseEvent()
      .withBody(result.body)
      .withHeaders(result.headers.asJava)
      .withStatusCode(result.statusCode)
      .withIsBase64Encoded(false)
  }
}
