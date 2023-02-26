import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {
  lazy val scalatest = Def.setting { "org.scalatest" %%% "scalatest" % "3.2.15" % Test }
}
