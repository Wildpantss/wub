package com.wildpants.wub.cli
package parser

import cats.syntax.all.*

/** A type-class for reading a [[String]] into instance of type 'T'.
  *
  * Implement this type-class so the CLI-parser could parse your type from
  * string input.
  *
  * @example
  *   ```scala
  *   // example for impl the 'Read' type-class for type 'Short'
  *   given ShortRead: Read[Short] = (str: String) => str.toShortOption
  *   ```
  *
  * @tparam T
  *   the target type to read
  */
trait Read[T]:

  /** Trying to generate 'T' instance from [[String]].
    *
    * @param str
    *   the [[String]] to read
    * @return
    *   an [[Either]], as [[Left]] with message when failed, [[Right]] that
    *   contains 'T' instance on success
    */
  def rawRead(str: String): Either[String, T]

  /** Internal actual applied read function, a wrapper on [[rawRead]].
    *
    * @param str
    *   the [[String]] to read, as input
    * @return
    *   an [[Either]], as [[Left]] contains (inputVal, errorMsg) when failed,
    *   [[Right]] that contains instance of 'T' on success
    */
  private[parser] def read(str: String): Either[(String, String), T] =
    rawRead(str).leftMap(str -> _)

end Read

object Read:

  /** A shortcut for summoning instance of Read[T].
    *
    * @example
    *   ```scala
    *   val intRead = Read[Int] // equals to summon[Read[Int]]
    *   ```
    */
  def apply[T: Read]: Read[T] = summon

  /* ------------------------ Pre-defined instances ------------------------ */

  given ShortRead: Read[Short] = (str: String) =>
    Either.fromOption(str.toShortOption, s"not a 'Short'")

  given IntRead: Read[Int] = (str: String) =>
    Either.fromOption(str.toIntOption, s"not a 'Int'")

  given LongRead: Read[Long] = (str: String) =>
    Either.fromOption(str.toLongOption, s"not a 'Long'")

  given FloatRead: Read[Float] = (str: String) =>
    Either.fromOption(str.toFloatOption, s"not a 'Float'")

  given DoubleRead: Read[Double] = (str: String) =>
    Either.fromOption(str.toDoubleOption, s"not a 'Double'")

  given StringRead: Read[String] = (str: String) =>
    Right(str)

end Read
