package com.wildpants.wub
package parser

import console.Style.*
import parser.TaskUsage.*

import scala.annotation.targetName
import scala.collection.mutable.ListBuffer

/**
 * == TaskUsage ==
 *
 * Description of a task with its name and arguments' requirement.
 *
 * @note [[toString]] output style:
 *          {{{
 *          task-name  - <required-arg> [optional-arg] ...
 *          }}}
 * @example build with following DSL:
 *          {{{
 *          import TaskUsage.*
 *          val usage = TaskUsage {
 *            Builder() <<< "task-name" <<< ("arg1", Required) <<< ("arg2", Optional)
 *          }
 *          }}}
 * @param builder a [[TaskUsage.Builder]]
 * @author wildpants
 * */
class TaskUsage(builder: Builder) {

  /**
   * The name of task.
   * */
  val name: String = builder.name

  /**
   * A [[Seq]] of argument's name and [[Requirement]]
   * */
  val args: Seq[(String, Requirement, String)] = builder.args.toList
  
  /**
   * == TaskUsage.detailedDescription ==
   *
   * Get a [[Seq]] of formatted argument name and its' description.
   *
   * @return the [[Seq]] contains detailed info.
   * */
  def detailedDescription: Seq[(String, String)] = args.map { (name, status, desc) =>
    val formattedName = status match
      case Required => s"<$name>"
      case Optional => s"[$name]"
    (formattedName, desc)
  }

  override def toString: String = {
    val nameSection = s"${ name <<< Bold }"
    val argSection = args match {
      case a if a.isEmpty => ""
      case _ => "  - " + args.map { (name, status, _) =>
        status match
          case Required => s"<$name>"
          case Optional => s"[$name]"
      }.reduce(_ + " " + _)
    }
    nameSection + argSection
  }
}

object TaskUsage {

  /**
   * == TaskUsage.Requirement ==
   *
   * An enum that represents an task argument's requirement. (Required or Optional)
   * */
  //noinspection ScalaWeakerAccess
  enum Requirement {
    case Required
    case Optional
  }

  export Requirement.{Required, Optional}

  /**
   * == TaskUsage.Builder ==
   *
   * Builder for [[TaskUsage]]
   *
   * @see [[TaskUsages]]
   * */
  class Builder {

    var name: String = _
    val args: ListBuffer[(String, Requirement, String)] = ListBuffer.empty

    /**
     * == TaskUsage.Builder.<<< ==
     *
     * Set a name for target [[TaskUsage]].
     *
     * @note multiple invocation will overwrite the former setting.
     * @param name the task name to set.
     * @return the [[Builder]] itself so the API could be called fluently.
     * */
    @targetName("setName")
    inline def <<<(name: String): Builder = { this.name = name; this }

    /**
     * == TaskUsage.Builder.<<< ==
     *
     * Add an argument for target [[TaskUsage]].
     *
     * @param arg argument name and its [[Requirement]].
     * @return the [[Builder]] itself so the API could be called fluently.
     * */
    @targetName("addArg")
    inline def <<<(arg: (String, Requirement, String)): Builder = { this.args.addOne(arg); this }
  }
}
