package com.wildpants.wub
package cli
package console

/** A series of console commonly-used operations.
  *
  * @note
  *   'import *' or 'mix-in' this trait to use the methods inside.
  */
trait ConsoleOps {

  /** Prints an item to std-out using its toString method.
    *
    * @param x
    *   the item to print.
    */
  inline def print(x: Any): Unit = {
    scala.Console.print(x.toString)
  }

  /** Prints an item to std-out using its toString method. (with line separator
    * at end)
    *
    * @param x
    *   the item to print.
    */
  inline def printLine(x: Any): Unit = {
    scala.Console.println(x.toString)
  }

  /** Prints an item to err-out using its toString method.
    *
    * @param x
    *   the item to print.
    */
  inline def printError(x: Any): Unit = {
    scala.Console.err.print(x.toString)
  }

  /** Prints an item to err-out using its toString method. (with line separator
    * at end)
    *
    * @param x
    *   the item to print.
    */
  inline def printLineError(x: Any): Unit = {
    scala.Console.err.println(x.toString)
  }
}
