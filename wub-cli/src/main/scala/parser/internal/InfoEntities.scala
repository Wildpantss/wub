package com.wildpants.wub.cli
package parser.internal

import console.Style
import console.Style.*

import NameFormatUtils.*

import scala.util.Properties.lineSeparator as endl

/*
 * Internal representations for Cmd & Arg.
 *
 * Useful in help & error output generation
 */

/** Information of a CLI-Command.
  *
  * @param name
  *   the name of command
  * @param desc
  *   the description of command
  * @param a
  *   [[List]] of [[ArgInfo]]
  */
case class CmdInfo(name: String, desc: String, args: List[ArgInfo]):

  /** Get the usage information of this command.
    *
    * This methods returns an intermediate representation, so the call-site is
    * able to control the format more freely.
    *
    * Comparing to [[usageColored]], this method returns elements withou color.
    *
    * @return
    *   a [[Tuple3]], (name, argList, desc)
    */
  def usage: (String, String) =
    val argList = args.map(_.nameDisp).reduceOption(_ + " " + _).getOrElse("")
    (camelOrPascalToKebab(name), desc)

  /** Get the usage information of this command.
    *
    * This methods returns an intermediate representation, so the call-site is
    * able to control the format more freely.
    *
    * Comparing to [[usage]], this method returns elements with color.
    *
    * @return
    *   a [[Tuple3]], (name, argList, desc)
    */
  def usageColored: (String, String) =
    val argListColored =
      args.map(_.nameDispColored).reduceOption(_ + " " + _).getOrElse("")
    (camelOrPascalToKebab(name) <<< CMD_NAME_STYLE, desc)

end CmdInfo

/** Information of a ClI-Argument.
  *
  * Sub-types:
  *   - [[ArgInfo.Req]] -> a required argument
  *   - [[ArgInfo.Opt]] -> a optional argument
  */
enum ArgInfo(val name: String, val desc: String):

  /* ---------------- Sub-types ---------------- */

  /** Information of a required CLI-Argument.
    * @param name
    *   the name of argument
    * @param desc
    *   the description of argument
    */
  case Req(override val name: String, override val desc: String)
      extends ArgInfo(name, desc)

  /** Information of a optional CLI-Argument.
    * @param name
    *   the name of argument
    * @param desc
    *   the description of argument
    */
  case Opt(override val name: String, override val desc: String)
      extends ArgInfo(name, desc)

  /* ---------------- Enum Methods ---------------- */

  /** Get the displaying format of the current argument's name.
    *
    * @example
    *   - for [[Req]]: <`arg-name`>
    *   - for [[Opt]]: [`arg-name`]
    *
    * @return
    *   a [[String]] in kebab-case
    */
  def nameDisp: String = this match
    case Req(name, _) => s"<${camelOrPascalToKebab(name)}>"
    case Opt(name, _) => s"[${camelOrPascalToKebab(name)}]"

  /** Get the displaying format of the current argument's name with color.
    *
    * @example
    *   - for [[Req]]: <`arg-name`> (in color)
    *   - for [[Opt]]: [`arg-name`] (in color)
    *
    * @return
    *   a [[String]] in kebab-case
    */
  def nameDispColored: String = this match
    case Req(name, _) => s"<${camelOrPascalToKebab(name)}>" <<< REQ_ARG_STYLE
    case Opt(name, _) => s"[${camelOrPascalToKebab(name)}]" <<< OPT_ARG_COLOR

end ArgInfo
