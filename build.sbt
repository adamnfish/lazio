ThisBuild / scalaVersion     := "2.13.0"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.adamnfish.lazio"
ThisBuild / organizationName := "adamnfish"

val awsSdkVersion = "1.11.602"

lazy val lazioCore = (project in file("lazio-core"))
  .settings(
    name := "lazio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC10-1",
      "com.amazonaws" % "aws-java-sdk-core" % awsSdkVersion,
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
      "com.amazonaws" % "aws-lambda-java-events" % "2.2.6",
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val example = (project in file("example"))
  .settings(
    name := "example"
  )
  .dependsOn(lazioCore)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
