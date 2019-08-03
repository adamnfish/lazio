package com.adamnfish.lazio

import com.amazonaws.services.lambda.runtime.Context
import zio.ZIO
import zio.console.Console


object Logging {
  def log(message: String, context: Context): ZIO[Console, Nothing, Unit] = {
    ZIO.accessM(_.console putStrLn s"${context.getAwsRequestId}: $message")
  }
}
