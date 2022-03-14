import Dependencies._

ThisBuild / organization := "com.moust.racoon"
ThisBuild / licenses := Seq(License.Apache2)

ThisBuild / scalaVersion := "2.12.15"
ThisBuild / crossScalaVersions := Seq("2.12.15", "2.13.8")

ThisBuild / tlBaseVersion := "1.0"
ThisBuild / tlCiReleaseBranches := Seq("master")
ThisBuild / tlSonatypeUseLegacyHost := true
ThisBuild / developers := List(
  tlGitHubDev("moust", "Quentin Aupetit")
)

lazy val commonSettings = Seq(
  resolvers ++= Seq(Resolver.mavenLocal),
  Test / parallelExecution := false,
  Compile / doc / scalacOptions := (Compile / doc / scalacOptions).value.filter(_ != "-Xfatal-warnings"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  testFrameworks += new TestFramework("munit.Framework"),
)

lazy val warnUnusedImport = Seq(
  scalacOptions ++= Seq("-Ywarn-unused:imports"),
  Compile / console / scalacOptions ~= {
    _.filterNot(Set("-Ywarn-unused-import", "-Ywarn-unused:imports"))
  },
  Test / console / scalacOptions := (Compile / console / scalacOptions).value,
)

lazy val allSettings =
  commonSettings ++
  commonDeps ++
  warnUnusedImport

lazy val root = Project("racoon", file("."))
  .settings(name := "racoon")
  .enablePlugins(NoPublishPlugin)
  .settings(allSettings)
  .aggregate(
    core,
    doobie,
    elastic4s,
  )

lazy val core = (project in file("modules/core"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(name := "racoon-core")
  .settings(allSettings)
  .settings(libraryDependencies += attoCore)

lazy val doobie = (project in file("modules/doobie"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(name := "racoon-doobie")
  .dependsOn(core)
  .settings(allSettings)
  .settings(libraryDependencies += doobieCore)

lazy val elastic4s = (project in file("modules/elastic4s"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(name := "racoon-elastic4s")
  .dependsOn(core)
  .settings(allSettings)
  .settings(libraryDependencies += elastic4sCore)
