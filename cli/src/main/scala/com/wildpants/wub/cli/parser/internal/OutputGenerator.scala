package com.wildpants.wub.cli
package parser
package internal

import console.Style
import console.Style.*
import NameFormatUtils.*

import scala.util.Properties.lineSeparator as endl
import scala.collection.mutable.StringBuilder

class OutputGenerator(
  private val appName: String,
  private val version: String,
  private val author: String,
  private val desc: String,
  private val taskInfo: Map[String, CmdInfo]
):

  def globalHelp: String = StringBuilder()
    .append { s"${appName.toUpperCase <<< APP_NAME_STYLE}  v$version" + endl * 2 }
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
    val builder = StringBuilder()
      .append { s"${cmd <<< CMD_NAME_STYLE}: ${argInfo.desc}" + endl * 2 }
      .append { s"${"Arguments" <<< HEADER_STYLE}:" + endl }

    argDetails.foreach(a => builder.append { s"%-${maxNameLen + 6}s %s".format(a._1, a._2) + endl })
    builder.toString

  def versionInfo: String =
    s"${appName <<< APP_NAME_STYLE} v$version  -- by $author"

  def taskFailureInfo(cmd: String, reason: String): String =
    s"${"[error]" <<< Red} $reason"

  def noCmdInfo: String = StringBuilder()
    .append { s"${"[error]" <<< Red} please select a command to execute!" + endl * 2 }
    .append { s"${"Commands" <<< HEADER_STYLE}: " + endl }
    .append { commandList + endl }
    .toString

  def cmdNotFoundInfo(cmd: String): String = StringBuilder()
    .append { s"${"[error]" <<< Red} command '$cmd' not found!" + endl * 2 }
    .append { s"${"Commands" <<< HEADER_STYLE}: " + endl }
    .append { commandList + endl }
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
