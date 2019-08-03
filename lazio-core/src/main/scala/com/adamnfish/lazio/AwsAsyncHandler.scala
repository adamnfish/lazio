package com.adamnfish.lazio

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.handlers.AsyncHandler
import zio.{IO, Promise}


object AwsAsyncHandler {
  def awsToZio
      [R <: AmazonWebServiceRequest, T, Client]
      (client: Client)
      (sdkMethod: Client => ((R, AsyncHandler[R, T]) => java.util.concurrent.Future[T])): (R => IO[Exception, T]) = { req =>
    for {
      promise <- Promise.make[Exception, T]
      _ = sdkMethod(client)(req, new AsyncHandler[R, T] {
        override def onError(e: Exception): Unit = promise.fail(e)
        override def onSuccess(request: R, result: T): Unit = promise.succeed(result)
      })
      effect <- promise.await
    } yield effect
  }
}
