ThisBuild / version := "0.0.2"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "com.wildpants"

/* ------------------------ Project Structure ------------------------ */

import Dependencies.*

lazy val root = (project in file("."))
  .settings(name := "wub")
  .settings(addCommandAlias("run", "app/run"))
  .settings(mainClass := Some("com.wildpants.wub.app.main"))
  .aggregate(cli, app)

lazy val cli = (project in file("./cli"))
  .settings(name := "cli")
  .settings(libraryDependencies += scalatest.value)

lazy val app = (project in file("./app"))
  .settings(name := "app")
  .settings(libraryDependencies += scalatest.value)
  .dependsOn(cli)

/* ----------------------- Scala/Native Configs ---------------------- */

// enablePlugins(ScalaNativePlugin)
// nativeLinkStubs := true

// import scala.scalanative.build._
// nativeConfig ~= {
//   _.withLTO(LTO.full).withMode(Mode.releaseFull).withGC(GC.immix)
// }
