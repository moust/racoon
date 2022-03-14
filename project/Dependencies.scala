import sbt.Keys.libraryDependencies
import sbt._

object Dependencies {
  lazy val attoVersion       = "0.9.5"
  lazy val doobieVersion     = "0.13.4"
  lazy val elastic4sVersion  = "7.15.5"
  lazy val scalaCheckVersion = "1.15.4"
  lazy val scalatestVersion  = "3.2.10"
  lazy val munitVersion      = "0.7.29"

  lazy val commonDeps = Seq(
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test,
      "org.scalameta" %% "munit" % munitVersion % Test,
      "org.scalameta" %% "munit-scalacheck" % munitVersion % Test,
    ))

  lazy val attoCore = "org.tpolecat" %% "atto-core" % attoVersion

  lazy val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion

  lazy val elastic4sCore = "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion
}
