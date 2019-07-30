package com.adamnfish.lazio

import com.amazonaws.AmazonWebServiceRequest
import com.amazonaws.handlers.AsyncHandler
import zio.{IO, Promise}


class AwsAsyncPromiseHandler[R <: AmazonWebServiceRequest, T](promise: Promise[Exception, T]) extends AsyncHandler[R, T] {
  def onError(e: Exception): Unit = {
    promise.fail(e)
  }
  def onSuccess(r: R, t: T): Unit = {
    promise.succeed(t)
  }
}

object AwsAsyncHandler {
  def awsToZio
      [R <: AmazonWebServiceRequest, T, Client]
      (client: Client)
      (sdkMethod: Client => ((R, AsyncHandler[R, T]) => java.util.concurrent.Future[T])): (R => IO[Exception, T]) = { req =>
    for {
      promise <- Promise.make[Exception, T]
      _ = sdkMethod(client)(req, new AwsAsyncPromiseHandler(promise))
      effect <- promise.await
    } yield effect
  }
}
