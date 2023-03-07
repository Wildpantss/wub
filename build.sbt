/* ---------------------------- Build Info --------------------------- */

ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "com.wildpants"

/* ------------------------- Project Structure ----------------------- */

Compile / mainClass := Some("com.wildpants.wub.app.main")

lazy val root = (project in file("."))
  .settings(name := "wub")
  .settings(addCommandAlias("run", "wubApp/run"))
  .settings(addCommandAlias("pkg", "wubApp/graalvm-native-image:packageBin"))
  .aggregate(wubApp, wubCli)

lazy val wubApp = (project in file("./wub-app"))
  .settings(name := "wubApp")
  .enablePlugins(GraalVMNativeImagePlugin)
  .settings(commonSettings)
  .dependsOn(wubCli)

lazy val wubCli = (project in file("./wub-cli"))
  .settings(name := "wubCli")
  .enablePlugins(GraalVMNativeImagePlugin)
  .settings(commonSettings)

/* -------------------------- Common Settings ------------------------- */

lazy val commonSettings = Seq(
  libraryDependencies ++= commonDeps,
  graalVMNativeImageCommand := graalCmdPos,
  graalVMNativeImageOptions ++= graalOptions
)

/* --------------------------- Dependencies -------------------------- */

lazy val commonDeps = Seq(
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

/* ------------------------- GraalVM Configs ------------------------- */

lazy val graalOptions = Seq(
  "--report-unsupported-elements-at-runtime",
  "-H:+ReportExceptionStackTraces",
  "--verbose",
  "--allow-incomplete-classpath",
  "--initialize-at-build-time",
  "--no-fallback"
)

lazy val graalCmdPos = { // check this out, dev-machine relative!
  "D:\\Scoop\\apps\\graalvm22-jdk17\\current\\bin\\native-image.cmd"
}
