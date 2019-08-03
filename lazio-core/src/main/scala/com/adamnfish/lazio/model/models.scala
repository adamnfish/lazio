package com.adamnfish.lazio.model


case class ApiGatewayProxyResponse(
  statusCode: Integer,
  headers: Map[String, String],
  body: String,
)
