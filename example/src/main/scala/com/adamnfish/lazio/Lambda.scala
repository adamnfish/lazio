package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent}
import zio.ZIO


class Lambda extends LazioAPIGatewayProxy {
  override val handler: ApiGatewayProxy = {
    for {
      request <- ZIO.access[(APIGatewayProxyRequestEvent, Context)](r => r._1)
    } yield {
      new APIGatewayProxyResponseEvent()
        .withBody("test" ++ request.getBody)
    }
  }
}
