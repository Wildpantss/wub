package com.wildpants.wub.cli.parser

/** Implement this type-class so the CLI-parser could handle your type from
  * string input.
  */
trait Read[T] {

  /** Trying to generate 'T' instance from [[String]].
    *
    * @tparam T
    *   the type to generate instance
    * @param str
    *   the [[String]] to read
    * @return
    *   an [[Either]], being [[Left]] and contains error message when failed,
    *   [[Right]] and contains 'T' instance when success
    */
  def read(str: String): Either[String, T]
}

object Read {

  /** A shortcut for summoning instance of Read[T].
    *
    * @example
    *   ```scala
    *   val intRead = Read[Int] // equals to summon[Read[Int]]
    *   ```
    */
  def apply[T: Read]: Read[T] = summon

  given ShortRead: Read[Short] = { (str: String) =>
    str.toShortOption match
      case Some(i) => Right(i)
      case None    => Left(s"invalid input '$str' for short-int")
  }

  given IntRead: Read[Int] = { (str: String) =>
    str.toIntOption match
      case Some(i) => Right(i)
      case None    => Left(s"invalid input '$str' for int")
  }

  given LongRead: Read[Long] = { (str: String) =>
    str.toLongOption match
      case Some(i) => Right(i)
      case None    => Left(s"invalid input '$str' for long-int")
  }

  given FloatRead: Read[Float] = { (str: String) =>
    str.toFloatOption match
      case Some(i) => Right(i)
      case None    => Left(s"invalid input '$str' for float")
  }

  given DoubleRead: Read[Double] = { (str: String) =>
    str.toDoubleOption match
      case Some(i) => Right(i)
      case None    => Left(s"invalid input '$str' for double-precision-float")
  }

  given StringRead: Read[String] = { (str: String) =>
    Right(str)
  }

}
