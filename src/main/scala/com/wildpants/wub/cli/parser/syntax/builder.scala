package com.wildpants.wub.cli
package parser
package syntax

import CliApp.CliCmd
import internal.CmdInfo

import scala.collection.mutable

class AppBuilder:
  var name: Option[String] = None
  var version: Option[String] = None
  var author: Option[String] = None
  var desc: Option[String] = None
  val commands: mutable.Map[String, (CliCmd, CmdInfo)] = mutable.Map.empty
end AppBuilder

def app(init: AppBuilder ?=> Unit) =
  given app: AppBuilder = AppBuilder()
  init
  app

def name(str: String)(using app: AppBuilder) =
  app.name = Some(str)

def version(str: String)(using app: AppBuilder) =
  app.version = Some(str)

def author(str: String)(using app: AppBuilder) =
  app.author = Some(str)

def desc(str: String)(using app: AppBuilder) =
  app.desc = Some(str)

def command(t: (String, (CliCmd, CmdInfo)))(using app: AppBuilder) =
  app.commands.addOne(t)
