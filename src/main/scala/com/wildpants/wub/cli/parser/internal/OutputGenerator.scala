package com.wildpants.wub.cli
package parser
package internal

import console.Style
import console.Style.*

import NameFormatUtils.*

import scala.collection.mutable.StringBuilder
import scala.util.Properties.lineSeparator as endl

/** Output text generator for [[CliApp]].
  */
private[parser] class OutputGenerator(
  private val appName: String,
  private val version: String,
  private val author: String,
  private val desc: String,
  private val taskInfo: Map[String, CmdInfo]
):

  def globalHelp: String = StringBuilder(endl)
    .append { desc + endl * 2 }
    .append { s"${"Usage" <<< HEADER_STYLE}: " }
    .append { s"$$ '${camelOrPascalToKebab(appName)} <command>'" + endl * 2 }
    .append { s"${"Commands" <<< HEADER_STYLE}: " + endl }
    .append { commandList + endl }
    .toString

  def commandHelp(cmd: String): String =
    val argInfo = taskInfo(cmd)
    val argDetails = argInfo.args.map(a => a.nameDispColored -> a.desc)
    val maxNameLen = argDetails.map(_._1.size).max
    val builder = StringBuilder(endl)
      .append { argInfo.desc + endl * 2 }
      .append { s"${"Commands" <<< HEADER_STYLE}:" + endl }

    argDetails.foreach(a => builder.append { s"%-${maxNameLen + 6}s %s".format(a._1, a._2) + endl })
    builder.append(endl).toString

  def versionInfo: String = StringBuilder(endl)
    .append { s"${appName <<< APP_NAME_STYLE} v$version  -- by $author" }
    .append { endl }
    .toString

  def taskFailureInfo(cmd: String, reason: String): String = StringBuilder(endl)
    .append { reason <<< Red }
    .append { endl }
    .toString

  def noCmdInfo: String = StringBuilder(endl)
    .append { ("please select a command to execute!" <<< Red) + endl * 2 }
    .append { s"${"Commands" <<< HEADER_STYLE}: " + endl }
    .append { commandList + endl }
    .toString

  def cmdNotFoundInfo(cmd: String): String = StringBuilder(endl)
    .append { (s"command '$cmd' not found! please choose one from follow:" <<< Red) + endl }
    .append {
      taskInfo.toList.map(_._1).map(x => s" - ${x <<< CMD_NAME_STYLE}").reduce(_ + endl + _)
    }
    .append { endl }
    .toString

  /* ------------------------ Private stuff ------------------------ */

  private def commandList: String =
    val usages = taskInfo.values.map(_.usageColored)
    val maxNameLen = usages.map(_._1).map(_.length).max
    val fmtString = s"%-${maxNameLen + 6}s  %s"
    usages
      .map(u => fmtString.format(u._1, u._2))
      .reduceOption(_ + endl + _)
      .getOrElse("")

end OutputGenerator
