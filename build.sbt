ThisBuild / version       := "0.0.1"
ThisBuild / scalaVersion  := "3.2.2"
ThisBuild / organization  := "com.wildpants"

name                      := "wub"
idePackagePrefix          := Some("com.wildpants.wub")

libraryDependencies       += "org.scalatest" %%% "scalatest" % "3.2.15" % "test"

nativeLinkStubs           := true

enablePlugins(ScalaNativePlugin)
