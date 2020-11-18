import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

val V = new {
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
      "io.buildo" %% "enumero-circe-support" % V.enumero
    )
  )
