package com.wildpants.wub.cli
package parser

import CCParsing.*
import CommonMacros.Entr.*
import InfoInspection.*
import NameFormatUtils.*
import console.*
import console.Style.*
import scala.deriving.Mirror.ProductOf
import scala.collection.mutable

/* ----------------------------------------------------------------------- */
/* ------------------------ Cli Application Stuff ------------------------ */
/* ----------------------------------------------------------------------- */

class CliApp private[parser] (
  val appInfo: AppInfo,
  val cmdTasks: Map[String, CommandTask],
  val cmdInfo: Map[String, CmdInfo]
):

  def launch(args: Seq[String]): Unit =
    import ExecResult.*
    val outGen = OutputTextGen(appInfo, cmdInfo)
    execute(args) match
      case Success(_)           => ()
      case Failure(name, cause) => ERROUT << outGen.genTaskFailText(name, cause)
      case CmdNotExists(name)   => ERROUT << outGen.genCommandNotExistText(name)
      case CmdNotAssign         => STDOUT << outGen.genGlobalHelpText

  /* ---------------------- Private Functions ---------------------- */

  private def execute(args: Seq[String]): ExecResult =
    args.toList match
      case cmdName :: cmdArgs => dispatchTask(cmdName, cmdArgs)
      case Nil                => ExecResult.CmdNotAssign

  private def dispatchTask(cmdName: String, cmdArgs: Seq[String]): ExecResult =
    cmdTasks.get(kebabToPascal(cmdName)) match
      case None       => ExecResult.CmdNotExists(cmdName)
      case Some(task) => executeTask(cmdName, task, cmdArgs)

  private def executeTask(name: String, task: CommandTask, args: Seq[String]) =
    task(args) match
      case Left(cause) => ExecResult.Failure(name, cause)
      case Right(_)    => ExecResult.Success(name)

end CliApp

object CliApp:

  def apply(info: AppInfo, tasks: (String, (CommandTask, CmdInfo))*): CliApp =
    val taskMap = tasks.map(pair => pair._1 -> pair._2._1).toMap
    val infoMap = tasks.map(pair => pair._1 -> pair._2._2).toMap
    new CliApp(info, taskMap, infoMap)

end CliApp

/* ------------------------ Command Task Stuff ------------------------ */

type CommandTask = Seq[String] => Either[String, Unit]

class CliAppBuilder:

  private var appName: String = ""
  private var appVersion: String = ""
  private var appAuthor: String = ""
  private var appDescription: String = ""
  private val appCmdTasks: mutable.Map[String, CommandTask] = mutable.Map.empty
  private val appCmdInfo: mutable.Map[String, CmdInfo] = mutable.Map.empty

  def name(n: String): CliAppBuilder =
    this.appName = n
    this

  def version(v: String): CliAppBuilder =
    this.appVersion = v
    this

  def author(a: String): CliAppBuilder =
    this.appAuthor = a
    this

  def desc(d: String): CliAppBuilder =
    this.appDescription = d
    this

  inline def cmd[T: ProductOf](func: T => Either[String, Unit]): CliAppBuilder =
    val cmdTask = (args: Seq[String]) => parseCaseClass[T](args).flatMap(func)
    val cmdInfo = inspectCmdInfo[T]
    val cmdName = inspectTypeName[T]
    this.appCmdTasks.addOne(cmdName, cmdTask)
    this.appCmdInfo.addOne(cmdName, cmdInfo)
    this

  def build: CliApp =
    val appInfo = AppInfo(appName, appVersion, appAuthor, appDescription)

    this.appCmdInfo
      .addOne("Help" -> inspectCmdInfo[Help])
      .addOne("Version" -> inspectCmdInfo[Version])

    this.appCmdTasks
      .addOne("Help" -> genHelpTask(this.appCmdInfo.toMap))
      .addOne("Version" -> genVersionTask(this.appCmdInfo.toMap))

    new CliApp(appInfo, appCmdTasks.toMap, appCmdInfo.toMap)

  /* -------------------- Auto-gen `Help` & `Version` -------------------- */

  /** print help message for all commands or the given command
    *
    * @param command
    *   the name of command to get help info
    */
  private case class Help(command: String = "")

  /** print the version of current app distribution
    */
  private case class Version()

  private def genHelpTask(cmdInfoMap: Map[String, CmdInfo]): CommandTask =
    val appInfo = AppInfo(appName, appVersion, appAuthor, appDescription)
    val outGen = OutputTextGen(appInfo, cmdInfoMap)
    val func: Help => Either[String, Unit] = _ match
      case Help("") => Right { STDOUT << outGen.genGlobalHelpText; () }
      case Help(cmd) =>
        val c = kebabToPascal(cmd)
        appCmdTasks.get(c) match
          case Some(_) => Right { STDOUT << outGen.genCommandHelpText(c); () }
          case None    => Left(s"command '$cmd' not found")
    (a: Seq[String]) => parseCaseClass[Help](a).flatMap(func)

  private def genVersionTask(cmdInfoMap: Map[String, CmdInfo]): CommandTask =
    val appInfo = AppInfo(appName, appVersion, appAuthor, appDescription)
    val outGen = OutputTextGen(appInfo, cmdInfoMap)
    val func = (v: Version) => Right { STDOUT << outGen.genVersionText; () }
    (a: Seq[String]) => parseCaseClass[Version](a).flatMap(func)

end CliAppBuilder

/* ------------------------------------------------------------------------ */
/* ------------------------ Execution Result Stuff ------------------------ */
/* ------------------------------------------------------------------------ */

private enum ExecResult:
  case Success(cmdName: String)
  case Failure(cmdName: String, cause: String)
  case CmdNotExists(cmdName: String)
  case CmdNotAssign

private class OutputTextGen(
  val appInfo: AppInfo,
  val cmdInfoMap: Map[String, CmdInfo]
):

  def genGlobalHelpText: String =
    mutable.StringBuilder(NL)
      .append { (appInfo.desc <<< Underline) + NL * 2 }
      .append { s"${"Usage" <<< HEADER_STYLE}: ${appInfo.name}" }
      .append { " <command>" <<< REQ_ARG_STYLE }
      .append { " [<args>]" <<< OPT_ARG_COLOR }
      .append { NL * 2 }
      .append { ("Commands:" <<< HEADER_STYLE) + NL }
      .append { commandList + NL * 2 }
      .toString

  def genCommandHelpText(cmdName: String) =
    val kebabCmdName = camelOrPascalToKebab(cmdName)
    val cmdInfo = cmdInfoMap(cmdName)
    val argDetails = cmdInfo.args.map(a => a.dispName -> a.desc)
    val maxNameLen = argDetails.map(_._1.size).maxOption.getOrElse(0)
    val usageString =
      cmdInfo.args.map(_.dispName).reduceOption(_ + " " + _).getOrElse("")
    val builder = StringBuilder(NL)
      .append { (cmdInfo.desc <<< Underline) + NL * 2 }
      .append { "Usage:" <<< HEADER_STYLE }
      .append { s" $kebabCmdName $usageString" + NL * 2 }

    if argDetails.size != 0 then
      builder.append { s"${"Arguments" <<< HEADER_STYLE}:" + NL }
    argDetails
      .foreach(a =>
        builder.append { s"%-${maxNameLen + 6}s %s".format(a._1, a._2) + NL }
      )
    builder.append(NL).toString

  def genVersionText: String = StringBuilder(NL)
    .append { s"${appInfo.name <<< APP_NAME_STYLE} v${appInfo.ver}" + NL }
    .append { s"by ${appInfo.author}" + NL }
    .toString

  def genTaskFailText(cmdName: String, cause: String): String =
    (cause <<< Red) + NL

  def genCommandNotExistText(cmdName: String): String =
    val msgPattern = "command '%s' not found! please choose one from follow:"
    StringBuilder(NL)
      .append { (msgPattern.formatted(cmdName) <<< Red) + NL }
      .append {
        cmdInfoMap.toList.map(_._1).map(x =>
          s"${camelOrPascalToKebab(x) <<< CMD_NAME_STYLE}"
        ).reduce(_ + NL + _)
      }
      .append { NL * 2 }
      .toString

  /* ------------------------ Private stuff ------------------------ */

  private def commandList: String =
    val usages = cmdInfoMap.values.map(_.usage).toList.sorted
    val maxNameLen = usages.map(_._1).map(_.length).max
    val fmtString = s"%-${maxNameLen + 24}s  %s"
    usages
      .map(u => fmtString.format((u._1 <<< CMD_NAME_STYLE), u._2))
      .reduceOption(_ + NL + _)
      .getOrElse("")

end OutputTextGen
