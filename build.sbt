ThisBuild / version       := "0.0.1"
ThisBuild / scalaVersion  := "3.2.2"
ThisBuild / organization  := "com.wildpants"

name                      := "wub"
idePackagePrefix          := Some("com.wildpants.wub")

libraryDependencies       += "com.lihaoyi" %%% "utest"  % "0.8.1" % Test

testFrameworks            += new TestFramework("utest.runner.Framework")

enablePlugins(ScalaNativePlugin)
