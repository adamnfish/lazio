package com.adamnfish.lazio

import com.adamnfish.lazio.model.ApiGatewayProxyResponse
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import zio.{DefaultRuntime, ZIO}
import scala.jdk.CollectionConverters._


trait LazioAPIGatewayProxy extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent] {
  private val runtime = new DefaultRuntime {}

  override def handleRequest(input: APIGatewayProxyRequestEvent, context: Context): APIGatewayProxyResponseEvent = {
    val program = handler.provide((input, context))
    val result = runtime.unsafeRun(program)
    new APIGatewayProxyResponseEvent()
      .withBody(result.body)
      .withHeaders(result.headers.asJava)
      .withStatusCode(result.statusCode)
      .withIsBase64Encoded(false)
  }

  // Lambda is handled by the provided ZIO program (with all errors handled)
  val handler: ZIO[(APIGatewayProxyRequestEvent, Context), Nothing, ApiGatewayProxyResponse]
}
