package com.wildpants.wub
package parser

/**
 * = CliTask =
 *
 * Represents a 'task' in our semi-auto cli framework.
 *
 * (Unfortunately you have to parse argument manually,
 * for I'm not familiar with scala3 derive/macro stuff, maybe write a full-auto cli parser framework later)
 *
 * @author wildpants
 * */
trait Task {
  /**
   * == CliTask.name ==
   *
   * Name of the task.
   * */
  val name: String

  /**
   * == CliTask.usage ==
   *
   * Usage describing text, set up in your implementation.
   *
   * @example a very simple description of command usage.
   * {{{"freq <note-name> [standard-freq]"}}}
   * */
  val usage: TaskUsage

  /**
   * == CliTask.argSizeRange ==
   *
   * Possible argument size range.
   *
   * @see [[checkArgSize]]
   * */
  val argSizeRange: (Int, Int)

  /**
   * == CliTask.descShort ==
   *
   * The short description of your task.
   *
   * @example a very simple example.
   * {{{"freq calculates harmonic for you ^_^"}}}
   * */
  val descShort: String

  /**
   * == CliTask.descLong ==
   *
   * A longer, detailed description of your task.
   *
   * @example a very simple example.
   * {{{
   *          "freq calculates..."
   *          " - notice1: ******"
   *          " - notice2: ******"
   *          "  ...... "
   * }}}
   * */
  val descLong: Description

  /**
   * == CliTask.execute ==
   *
   * Main logic of your task.
   *
   * @param args the raw arguments in a [[Seq]] of [[String]].
   * @return an [[Either]], contains [[String]] on failure, otherwise a [[Unit]].
   * */
  def execute(args: Seq[String]): Either[String, Unit]

  /**
   * == CliTask.checkArgSize ==
   *
   * A util function that helps checking argument size.
   *
   * @note you could use in your [[execute]] implementation.
   * @param argSize the actual arg size.
   * @return an [[Either]], contains [[String]] on failure, otherwise a [[Unit]].
   * */
  def checkArgSize(argSize: Int): Either[String, Unit] = argSize match {
    case x if x < argSizeRange._1 => Left("too few arguments")
    case x if x > argSizeRange._2 => Left("too many arguments")
    case _ => Right(())
  }
}
