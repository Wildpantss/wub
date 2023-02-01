package com.wildpants.wub
package utils

import scala.util.{Failure, Success, Try}

/**
 * = Read =
 *
 * [[Read]] is a type-class for transforming a [[String]] into target type's instance.
 *
 * == Pre-defined instances ==
 *  - [[Short]] => [[typeclass.Read.ReadShort]]
 *  - [[Int]] => [[typeclass.Read.ReadInt]]
 *  - [[Long]] => [[typeclass.Read.ReadLong]]
 *  - [[Float]] => [[typeclass.Read.ReadFloat]]
 *  - [[Double]] => [[typeclass.Read.ReadDouble]]
 *  - [[String]] => [[typeclass.Read.ReadString]]
 *
 * == Examples for using this type-class to parse [[String]] ==
 *
 * === 1. Extension Method ===
 * {{{
 * import Read.*
 *
 * val d = "123.0".read[Double]
 * assert(d == Left(123.0))
 * }}}
 *
 * === 2. Call `read` from type-class instance ===
 * {{{
 * import Read.*
 *
 * val i = ReadInt.read("1")
 * assert(i == Left(1))
 * }}}
 *
 * === 3. in using parameter ===
 * {{{
 * import Read.*
 *
 * def readFoo[T](str: String)(using r: Read[T]): T = r.read(str)
 * }}}
 *
 * @note use {{{import Read.*}}} to import all pre-defined given type-class instances.
 * @author wildpants
 * */
trait Read[T] {
  def read(s: String): Either[String, T]
}

object Read {

  extension (s: String) {
    /**
     * == String.read ==
     *
     * Trying to deserialize the [[String]] as instance of type T.
     * */
    def read[T: Read]: Either[String, T] = implicitly[Read[T]].read(s)
  }


  /* ---------------- Pre-defined Type-class Implementations ---------------- */

  /**
   * Type-class [[Read]] implementation for [[Short]].
   * */
  given ReadShort: Read[Short] with
    override def read(s: String): Either[String, Short] = Try(s.toShort) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(s"input $s is not a valid short-int")
    }

  /**
   * Type-class [[Read]] implementation for [[Int]].
   * */
  given ReadInt: Read[Int] with
    def read(s: String): Either[String, Int] = Try(s.toInt) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(s"input $s is not a valid int")
    }

  /**
   * Type-class [[Read]] implementation for [[Long]].
   * */
  given ReadLong: Read[Long] with
    def read(s: String): Either[String, Long] = Try(s.toLong) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(s"input $s is not a valid long-int")
    }

  /**
   * Type-class [[Read]] implementation for [[Float]].
   * */
  given ReadFloat: Read[Float] with
    override def read(s: String): Either[String, Float] = Try(s.toFloat) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(s"input $s is not a valid float")
    }

  /**
   * Type-class [[Read]] implementation for [[Double]].
   * */
  given ReadDouble: Read[Double] with
    def read(s: String): Either[String, Double] = Try(s.toDouble) match {
      case Success(value) => Right(value)
      case Failure(_) => Left(s"input $s is not a valid double-float")
    }

  /**
   * Type-class [[Read]] implementation for [[String]].
   * */
  given ReadString: Read[String] with
    override def read(s: String): Either[String, String] = Right(s)
}
