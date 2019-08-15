package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}


class FakeLambdaContext extends Context {
  override def getLogStreamName = "dev-server-logs"

  class DevLogger extends LambdaLogger {
    def log(string: String): Unit = {
      println(string)
    }
  }

  override def getFunctionName = "function-name"
  override def getRemainingTimeInMillis = 1000
  override def getLogger = new DevLogger
  override def getFunctionVersion: String = "dev"
  override def getMemoryLimitInMB = 512
  override def getClientContext: ClientContext = null
  override def getInvokedFunctionArn = "dev::arn"
  override def getIdentity: CognitoIdentity = null
  override def getLogGroupName = "log-group"
  override def getAwsRequestId = "dev-request"
}