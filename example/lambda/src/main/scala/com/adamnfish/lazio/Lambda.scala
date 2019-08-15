package com.adamnfish.lazio

import com.adamnfish.lazio.Logging.log
import com.adamnfish.lazio.model.ApiGatewayProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import zio.console.Console
import zio.{DefaultRuntime, ZIO}


class Lambda extends LazioAPIGatewayProxy {
  override type Environment = Console
  override val runtime = new DefaultRuntime {}

  override def handle(request: APIGatewayProxyRequestEvent, context: Context): ZIO[Environment, Nothing, ApiGatewayProxyResponse] = {
    for {
      _ <- log("Test log line", context)
    } yield {
      ApiGatewayProxyResponse(
        200,
        Map.empty,
        s"test; path: ${request.getPath}; <body>: ${request.getBody}"
      )
    }
  }
}
