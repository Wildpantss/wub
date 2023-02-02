package com.wildpants.wub
package parser

import console.Style.*
import parser.TaskLauncher.*
import parser.TaskUsage.*

import com.wildpants.wub.parser

import scala.annotation.targetName
import scala.collection.{immutable, mutable}
import scala.util.Properties.lineSeparator as endl
import scala.util.{Failure, Success, Try}

/**
 * = TaskLauncher =
 *
 * Launcher for CLI application tasks.
 *
 * Validate arguments and delegate the task arguments to related [[Task]].
 *
 * @note use [[TaskLauncher.Builder]] to build instance.
 * @see [[Task]]
 * @author wildpants
 * */
class TaskLauncher(builder: parser.TaskLauncher.Builder) {

  private val appName = builder.appInfo._1
  private val appVersion = builder.appInfo._2
  private val appDescription = builder.appDescription
  private val taskDispatcher: immutable.Map[String, Task] = builder.taskBuffer.addOne("help" -> generateHelpTask).toMap

  /**
   * == taskLauncher.launch ==
   *
   * Validate arguments and launch task.
   *
   * @note error message will output in stderr if argument validation not passed.
   * @param args the arguments from console.
   * */
  def launch(args: Seq[String]): Unit = args match {
    case ar if ar.isEmpty => printNoArgumentError()               // empty input
    case taskName :: args => taskDispatcher.get(taskName) match { // separate task-name and its arguments
      case None => invalidTaskName(taskName)                      // invalid task-name
      case Some(task) => task.execute(args) match
        case Right(_) =>
        case Left(msg) => Console.err.println(s"$ERROR_TAG task ${ taskName <<< TASK_STYLE } failed: $msg${ endl * 2 }")
    }
  }

  /* ---------------- Private stuff about error message output on console ---------------- */

  private def printNoArgumentError(): Unit = {
    Console.err.println(allTaskHelpOutput)
  }

  private def invalidTaskName(task: String): Unit = Console.err.println {
    s"$ERROR_TAG task ${ task <<< TASK_STYLE } not found, please input a correct task name."
      + endl * 2
      + availableTasksText
  }

  private def availableTasksText: String = {
    s"${ "Tasks" <<< HEADER_STYLE }" + endl
      + this.taskDispatcher.values.toList.sortBy(_.name)
        .map(v => f" ${"-" <<< Bold} ${ v.name <<< TASK_STYLE }    ${ v.descShort }")
        .reduce(_ + endl + _)
      + endl
  }

  private def singleTaskHelpOutput(taskName: String): String = {
    val task = taskDispatcher(taskName) // pre-checked, key is ensured to exist.
    s"${ task.descLong }" + endl * 2 +
      s"${ "Usage" <<< HEADER_STYLE }: ${ task.usage }" + endl * 2 +
      s"${ "Arguments" <<< HEADER_STYLE }:" + endl + task.usage.detailedDescription.map { (name, desc) =>
      "  %-20s%s".format(name, desc)
    }.reduce(_ + endl + _)
  }

  private def allTaskHelpOutput: String = {
    val tasks = taskDispatcher.values.toList.sortBy(_.name)
    s"$appDescription" + endl * 2 +
      s"${ "Usage" <<< HEADER_STYLE }: $appName <task>" + endl * 2 +
      s"${ "Tasks" <<< HEADER_STYLE }:" + endl + tasks
      .map(task => s"  %-50s%s".format(task.usage.toString, task.descShort))
      .reduce(_ + endl + _) + endl * 2 +
      s"version: $appVersion"
  }

  private def generateHelpTask: Task = new Task {

    override val name: String = "help"
    override val argSizeRange: (Int, Int) = 0 -> 1
    override val descShort: String = "print help message for all or for the given task"
    override val descLong: Description = Description() // this will never show
    override val usage: TaskUsage = TaskUsage {
      TaskUsage.Builder() <<< "help" <<< ("task-name", Optional, "an available task name    { Optional }")
    }

    override def execute(args: Seq[String]): Either[String, Unit] = {
      processArguments(args) match
        case Left(message) => Left(message)
        case Right(Some(taskName)) => println(singleTaskHelpOutput(taskName)); Right(())
        case Right(None) => println(allTaskHelpOutput); Right(())
    }

    private def processArguments(args: Seq[String]): Either[String, Option[String]] = {
      checkArgSize(args.size).flatMap { _ => { args.size match
        case 0 => Right(None)
        case 1 => taskDispatcher.get(args.head) match
          case Some(_) => Right(Some(args.head))
          case None => Left(s"task ${ args.head <<< TASK_STYLE } not found" + endl * 2 + availableTasksText)
      }}
    }
  }
}

object TaskLauncher {
  /**
   * == TaskLauncher.Builder ==
   *
   * Builder for [[TaskLauncher]]
   * */
  class Builder {

    var appInfo: (String, String) = _
    var appDescription: String = _
    val taskBuffer: mutable.Map[String, Task] = mutable.Map.empty

    /**
     * == TaskLauncher.Builder.<<< ==
     *
     * Add a [[Task]] for the [[TaskLauncher]] to build.
     *
     * @param task the [[Task]] to add.
     * @return the [[Builder]] itself, so the API could be called fluently.
     * */
    @targetName("addTask")
    inline def <<<(task: Task): Builder = { taskBuffer.put(task.name, task); this }

    /**
     * == TaskLauncher.Builder.<<< ==
     *
     * Add a 'name' and 'version' for the [[TaskLauncher]] to build.
     *
     * @param appInfo a tuple of 'name' and 'version'.
     * @return the [[Builder]] itself, so the API could be called fluently.
     * */
    @targetName("addAppInfo")
    inline def <<<(appInfo: (String, String)): Builder = { this.appInfo = appInfo; this }

    /**
     * == TaskLauncher.Builder.<<< ==
     *
     * Add application description for the [[TaskLauncher]] to build.
     *
     * @param desc the application description.
     * @return the [[Builder]] itself, so the API could be called fluently.
     * */
    @targetName("addAppDesc")
    inline def <<<(desc: Description): Builder = { appDescription = desc.toString; this}

  }
}
