package com.wildpants.wub
package cli
package parser

import CliApp.*
import internal.*
import internal.ProductParsingUtils.readProduct
import syntax.AppBuilder

import console.ConsoleOps.*

import scala.collection.mutable
import scala.deriving.Mirror

/** Configuration for your CLI-APP.
  */
class CliApp private (
  private val appName: String,
  private val version: String,
  private val author: String,
  private val desc: String,
  rawTasks: Map[String, CliCmd],
  rawTaskInfo: Map[String, CmdInfo]
):

  private val tasks: Map[String, CliCmd] = setupHelpAndVersion(rawTasks, rawTaskInfo)._1
  private val taskInfo: Map[String, CmdInfo] = setupHelpAndVersion(rawTasks, rawTaskInfo)._2
  private val outputGen: OutputGenerator = OutputGenerator(appName, version, author, desc, taskInfo)

  /** Launch your CLI-APP.
    */
  def launch(args: Seq[String]): Unit =
    val result = run(args)
    processResult(result)

  /* ------------------------ Private stuff ------------------------ */

  private def run(args: Seq[String]): TaskResult =
    args.toList match
      case cmd :: cmdArgs => dispatchTask(cmd, cmdArgs)
      case Nil            => TaskResult.NoCmdArgError

  private def dispatchTask(cmdName: String, args: Seq[String]): TaskResult =
    tasks.get(cmdName) match
      case None => TaskResult.CmdNotFoundError(cmdName)
      case Some(cmd) => cmd(args) match
          case Left(reason) => TaskResult.Failure(cmdName, reason)
          case Right(_)     => TaskResult.Success(cmdName)

  private def processResult(result: TaskResult): Unit =
    import TaskResult.*
    result match
      case Success(cmd)          => processSuccess(cmd)
      case Failure(cmd, reason)  => processFailure(cmd, reason)
      case NoCmdArgError         => processNoCmdArgError
      case CmdNotFoundError(cmd) => processCmdNotFoundError(cmd)

  private def processSuccess(cmd: String): Unit = () // no extra works when success

  private def processFailure(cmd: String, reason: String): Unit =
    printLineError(outputGen.taskFailureInfo(cmd, reason))

  private def processNoCmdArgError: Unit =
    printLineError(outputGen.noCmdInfo)

  private def processCmdNotFoundError(cmd: String) =
    printLineError(outputGen.cmdNotFoundInfo(cmd))

  /* ------------------------ Setup for Help and Version task ------------------------ */

  /** print help message for all commands or the given command
    *
    * @param command
    *   the name of command to get help info
    */
  private case class Help(command: String = "")

  /** print the version of current app distribution
    */
  private case class Version()

  private def setupHelpAndVersion(tasks: Map[String, CliCmd], taskInfo: Map[String, CmdInfo]) =
    val (versionTask, versionInfo) = task[Version](_ => Right(printLine(outputGen.versionInfo)))
    val (helpTask, helpInfo) = task[Help] {
      _ match
        case Help("") => Right { printLine(outputGen.globalHelp) }
        case Help(cmd) => taskInfo.get(cmd) match
            case None    => Left(s"command '$cmd' not found")
            case Some(v) => Right { printLine(outputGen.commandHelp(cmd)) }
    }
    val t = tasks.updated("help", helpTask).updated("version", versionTask)
    val i = taskInfo.updated("help", helpInfo).updated("version", versionInfo)
    t -> i

  end setupHelpAndVersion

end CliApp

object CliApp:

  /** A type-aliase for function that represents the cli task.
    *
    * It takes a [[Seq]] of [[String]] as input from console, returns an [[Either]]: [[Left]] with
    * error message when failed, [[Right]] of [[Unit]] when success.
    */
  type CliCmd = Seq[String] => Either[String, Unit]

  /** Construct a [[CliApp]] instance.
    *
    * @param name
    *   the name of your app
    * @param version
    *   the version of your app
    * @param author
    *   author of the app
    * @param desc
    *   the description text of your app
    * @param tasks
    *   task logic for you app commands
    * @return
    *   the [[CliApp]] instance
    */
  def apply(
    name: String,
    version: String,
    author: String,
    desc: String,
    tasks: (String, (CliCmd, CmdInfo))*
  ): CliApp =
    val task_ = tasks.map(t => (t._1, t._2._1)).toMap
    val info_ = tasks.map(t => (t._1, t._2._2)).toMap
    new CliApp(name, version, author, desc, task_, info_)

  def apply(builder: AppBuilder): CliApp =
    apply(
      builder.name.get,
      builder.version.get,
      builder.author.get,
      builder.desc.get,
      builder.commands.toList*
    )

end CliApp

/** Shortcut for register a task.
  *
  * This is useful in building [[CliApp]] instance.
  *
  * @see
  *   [[apply]]
  */
inline def task[T: Mirror.ProductOf](func: T => Either[String, Unit]): (CliCmd, CmdInfo) =
  ((args: Seq[String]) => readProduct[T](args).flatMap(x => func(x))) -> inspectCmd[T]
