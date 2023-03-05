/* ---------------------------- Build Info --------------------------- */

ThisBuild / version := "0.0.4"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "com.wildpants"

/* ------------------------- Project Structure ----------------------- */

lazy val wub = (project in file("."))
  .settings(name := "wub")
  .settings(addCommandAlias("run", "wubApp/run"))
  .settings(mainClass := Some("com.wildpants.wub.app.main"))
  .aggregate(wubApp, wubCli)

lazy val wubApp = (project in file("./wub-app"))
  .settings(name := "wubApp")
  .settings(libraryDependencies ++= commonDeps)
  .dependsOn(wubCli)

lazy val wubCli = (project in file("./wub-cli"))
  .settings(name := "wubCli")
  .settings(libraryDependencies ++= commonDeps)

/* --------------------------- Dependencies -------------------------- */

lazy val commonDeps = Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

/* ------------------------- GraalVM Configs ------------------------- */

enablePlugins(GraalVMNativeImagePlugin)
graalVMNativeImageOptions ++= Seq(
  "--report-unsupported-elements-at-runtime",
  "-H:+ReportExceptionStackTraces",
  "--verbose",
  "--allow-incomplete-classpath",
  "--initialize-at-build-time",
  "--no-fallback"
)
