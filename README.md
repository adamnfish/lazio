Lazio
=====

Easily write Scala AWS Lambda functions using ZIO.

## Lazio Core

This provides a ZIO wrapper around AWS Lambda handlers as well as some
helpful utilities. Using this library you can proide a ZIO program and
have its execution handled for you.

* [Core Lazio clas for HTTP integrations](lazio-core/src/main/scala/com/adamnfish/lazio/LazioAPIGatewayProxy.scala)
* [ZIO wrapper aruond AWS Scala SDK v1.x API calls](lazio-core/src/main/scala/com/adamnfish/lazio/ZioAwsSdk.scala)

The ZIO wrapper requires a ZIO program with the following type:

```scala
ZIO[Environment, Nothing, ApiGatewayProxyResponse]
```

For more information on ZIO and the types it expects, take a look at
[ZIO's documentation](https://zio.dev/docs/overview/overview_index)

`Environment` is the ZIO environment required by your program. You can
think of the environment as your application's dependencies.

The second type parameter is `Nothing` to indicate that all errors
should have been handled. This parameter is used by ZIO to represent
the errors that this program can produce during its execution. Since
there are no values of type `Nothing`, we are indicating that it
cannot fail. This means that the program you provide must always
return an `ApiGatewayProxyResponse`. Practically, this means you
should handle any errors encountered during the program's execution
and make sure that an appropriate HTTP response is returned (e.g. an
HTTP 5xx).

The final type parameter is
[`ApiGatewayProxyResponse`](lazio-core/src/main/scala/com/adamnfish/lazio/model/models.scala),
which represents an HTTP response. It is comprised of an HTTP status
code, a body, and a Map containing HTTP headers. A value of this type
will be returned at the end of the program's execution and this is
what will be sent back to the client by Lazio.

## Example Lambda

This project shows a simple example of how to use Lazio to create a
Lambda function. The program itself is a trivial example that uses
ZIO's Console to print to the console, and echoes some request details
back to the client.

The example also contains a CDK program that can be used to create the
Lambda function in an AWS account, behind API Gateway. The API Gateway
integration puts the Lambda function behind a public URL.

* [Example ZIO Lambda program](example/lambda/src/main/scala/com/adamnfish/lazio/Lambda.scala)
* [CDK program for creating the example in AWS](example/lambda/cdk/index.ts)

**Important note:** using the provided CDK program to create the
Lambda function in an AWS account will incur costs!

## Example Dev Server

The dev server allows you to execute your Lambda function locally. It
mimics the API Gateway integration so that you can use HTTP requests
locally to invoke the Lambda function in the same way you would do in
Production when the Lambda function is deployed.

You can run the dev server by selecting the `exampleDevServer` project
from the sbt shell and using sbt's `run` command.

```
sbt:lazio> project exampleDevServer
  ...
sbt:example-dev-server> run
  ...
```

When the server is running you can stop it and return to the sbt shell
using `ctrl+d`.
