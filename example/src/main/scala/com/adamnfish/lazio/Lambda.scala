package com.adamnfish.lazio

import com.adamnfish.lazio.model.ApiGatewayProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import zio.ZIO


class Lambda extends LazioAPIGatewayProxy {
  override val handler: ZIO[(APIGatewayProxyRequestEvent, Context), Nothing, ApiGatewayProxyResponse] = {
    for {
      request <- ZIO.access[(APIGatewayProxyRequestEvent, Context)](r => r._1)
    } yield {
      ApiGatewayProxyResponse(
        200,
        Map.empty,
        s"test; path: ${request.getPath}; <body>: ${request.getBody}"
      )
    }
  }
}
