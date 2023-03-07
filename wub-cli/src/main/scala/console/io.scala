package com.wildpants.wub.cli
package console

import Style.*

/** Alias for line-separator
  */
inline def NL: String = scala.util.Properties.lineSeparator

/** Standard output (Supports chain call and `<<` syntax)
  *
  * @example
  *   ```scala
  *   /* chain call support */
  *   STDOUT.print("hello").print("world").println("!")
  *
  *   /* `<<` syntax support */
  *   STDOUT << "hello" << s"yeah ${1 + 1}" << NL
  *   ```
  */
object STDOUT:

  /** Prints an item to std-out using its toString method.
    *
    * @param item
    *   the item to print.
    */
  inline def print(item: Any): STDOUT.type =
    scala.Console.print(item.toString)
    this

  /** Prints an item to std-out using its toString method. (with line
    * separatorat end)
    *
    * @param item
    *   the item to print.
    */
  inline def println(item: Any): STDOUT.type =
    scala.Console.println(item.toString)
    this

  /** Alias of [[print]], prints an item to std-out using its toString method.
    *
    * @param item
    *   the item to print.
    */
  def <<(item: Any): STDOUT.type =
    this.print(item)

end STDOUT

/** Error output (Supports chain call and `<<` syntax)
  *
  * @example
  *   ```scala
  *   /* chain call support */
  *   ERROUT.print("hello").print("world").println("!")
  *
  *   /* `<<` syntax support */
  *   ERROUT << "hello" << s"yeah ${1 + 1}" << NL
  *   ```
  */
object ERROUT:

  /** Prints an item to err-out using its toString method.
    *
    * @param item
    *   the item to print.
    */
  inline def print(item: Any): ERROUT.type =
    scala.Console.err.print(item.toString)
    this

  /** Prints an item to err-out using its toString method. (with line separator
    * at end)
    *
    * @param item
    *   the item to print.
    */
  inline def println(item: Any): ERROUT.type =
    scala.Console.err.println(item.toString)
    this

  /** Alias of [[print]], prints an item to err-out using its toString method.
    *
    * @param item
    *   the item to print.
    */
  def <<(item: Any): ERROUT.type =
    this.print(item)

end ERROUT

def errMsg(content: String): String =
  s"${"ERROR:" <<< Red} ${content <<< Red}"

def warnMsg(content: String): String =
  s"${"WARN:" <<< Yellow} ${content <<< Yellow}"

def okMsg(content: String): String =
  s"${"OK:" <<< Green} $content]"
