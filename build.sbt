ThisBuild / version := "0.0.3"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "com.wildpants"

mainClass := Some("com.wildpants.wub.main")

libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.15" % Test

/* ----------------------- Scala/Native Configs ---------------------- */

enablePlugins(ScalaNativePlugin)
nativeLinkStubs := true

import scala.scalanative.build._
nativeConfig ~= {
  _.withLTO(LTO.full).withMode(Mode.releaseFull).withGC(GC.immix)
}
