import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val V = new {
  val akkaVersion = "2.6.10"
  val akkaHttpVersion = "10.2.1"
  val enumero = "1.4.0"  
}

lazy val macroParadise = ("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)

lazy val root = (project in file("."))
  .settings(
    name := "RPS game",
    resolvers ++= Seq(Resolver.bintrayRepo("buildo", "maven")),
    addCompilerPlugin(macroParadise),
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "org.typelevel" %% "cats-core" % "2.1.1",
      "io.buildo" %% "enumero" % V.enumero,
      "io.buildo" %% "enumero-circe-support" % V.enumero,
      "io.buildo" %% "wiro-http-server" % "0.8.1",
      "com.typesafe.akka" %% "akka-http" % V.akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % V.akkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % V.akkaVersion,
      "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
      "io.circe" %% "circe-core" % "0.8.0",
      "io.circe" %% "circe-generic" % "0.8.0",
      "ch.megard" %% "akka-http-cors" % "1.1.0",
      "org.scalatest" %% "scalatest" % "3.2.3" % "test",
      "org.scalamock" %% "scalamock" % "4.4.0" % "test",
      "org.scalactic" %% "scalactic" % "3.2.2"
    )
  )
