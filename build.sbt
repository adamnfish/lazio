ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.adamnfish.lazio"
ThisBuild / organizationName := "adamnfish"

val awsSdkVersion = "1.11.602"
val zioVersion = "1.0.0-RC10-1"
val awsLambdaJavaEventsVersion = "2.2.6"

lazy val lazioCore = (project in file("lazio-core"))
  .settings(
    name := "lazio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "com.amazonaws" % "aws-java-sdk-core" % awsSdkVersion,
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
      "com.amazonaws" % "aws-lambda-java-events" % awsLambdaJavaEventsVersion,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val example = (project in file("example/lambda"))
  .settings(
    name := "lambda-example",
    packageName in Universal := normalizedName.value,
    topLevelDirectory in Universal := None,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "com.amazonaws" % "aws-lambda-java-events" % awsLambdaJavaEventsVersion
    )
  )
  .enablePlugins(JavaAppPackaging)
  .dependsOn(lazioCore)

lazy val exampleDevServer = (project in file("example/devServer"))
  .settings(
    name := "example-dev-server",
    libraryDependencies ++= Seq(
      "io.javalin" % "javalin" % "3.4.1",
      "org.slf4j" % "slf4j-simple" % "1.7.26",
      "dev.zio" %% "zio" % zioVersion,
      "com.amazonaws" % "aws-lambda-java-events" % awsLambdaJavaEventsVersion
    )
  )
  .dependsOn(example)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
