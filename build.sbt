ThisBuild / version := "0.0.2"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "com.wildpants"

/* ------------------------ Project Structure ------------------------ */

mainClass := Some("com.wildpants.wub.app.main")

lazy val root = (project in file("."))
  .settings(name := "wub")
  .settings(addCommandAlias("run", "app/run"))

lazy val cli = (project in file("./cli"))
  .settings(name := "cli")

lazy val app = (project in file("./app"))
  .settings(name := "app")
  .dependsOn(cli)

/* -------------------------- Dependencies --------------------------- */

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.15" % Test

/* ----------------------- Scala/Native Configs ---------------------- */

enablePlugins(ScalaNativePlugin)
nativeLinkStubs := true

import scala.scalanative.build._
nativeConfig ~= {
  _.withLTO(LTO.full).withMode(Mode.releaseFull).withGC(GC.immix)
}
