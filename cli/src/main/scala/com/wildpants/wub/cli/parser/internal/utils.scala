package com.wildpants.wub.cli
package parser
package internal

import scala.deriving.Mirror.ProductOf as Prd
import scala.compiletime.*

private[parser] object NameFormatUtils:

  /** Transform a legal camel-case / pascal-case symbol name into kebab-case.
    *
    * @example
    *   ```scala
    *   val kebab = "a-name-shit"
    *   val camel = "aNameShit"
    *   val pascal = "ANameShit"
    *
    *   assert(camelOrPascalToKebab(camel) == kebab)
    *   assert(camelOrPascalToKebab(pascal) == kebab)
    *   ```
    *
    * @param str
    *   the camel-case / pascal-case symbol name
    * @return
    *   the kebab-case name
    */
  def camelOrPascalToKebab(str: String): String =
    str.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")
      .map(_.toLowerCase())
      .reduce(_ + "-" + _)

  /** Transform a legal kebab-case symbol name into camel-case.
    *
    * @example
    *   ```scala
    *   val kebab = "a-name-shit"
    *   val camel = "aNameShit"
    *
    *   assert(kebabToCamel(kebab) == camel)
    *   ```
    *
    * @param str
    *   the kebab-case symbol name
    * @return
    *   the camel-case name
    */
  def kebabToCamel(str: String): String =
    str.split("-").toList match
      case head :: Nil   => head
      case head :: tails => head ++ tails.map(w => w.updated(0, w(0).toUpper)).reduce(_ + _)
      case _             => throw Exception()

  /** Transform a legal kebab-case symbol name into pascal-case.
    *
    * @example
    *   ```scala
    *   val kebab = "a-name-shit"
    *   val pascal = "ANameShit"
    *
    *   assert(kebabToPascal(kebab) == pascal)
    *   ```
    *
    * @param str
    *   the kebab-case symbol name
    * @return
    *   the pascal-case name
    */
  def kebabToPascal(str: String): String =
    str.split("-").toList match
      case Nil  => throw Exception()
      case list => list.map(w => w.updated(0, w(0).toUpper)).reduce(_ + _)

end NameFormatUtils

private[parser] object ProductParsingUtils:

  /** Parse the input arguments into 'T' instance.
    *
    * @tparam T
    *   the [[Product]] type to tranform raw arguments into
    * @param args
    *   the arguments from input
    * @return
    *   an [[Either]], [[Left]] that contains error message when failed to parse, otherwise a
    *   [[Right]] contains the 'T' instace
    */
  inline def readProduct[T](args: Seq[String])(using m: Prd[T]): Either[String, T] =
    // get Read tc instances and default values for arguments
    type elemTypes = m.MirroredElemTypes
    val reads = summonReads[elemTypes]
    val defaults = getDefaults[T]
    val argNames = getCaseFieldNames[T]

    // check argument list size, return err or actual instance
    checkArgLength(args.size, defaults, reads.size) match
      case Some(errMsg) => Left(errMsg)
      case None =>
        val partitioned = (args.toList lazyZip reads lazyZip argNames)
          .map((arg, read, name) => read.read(arg) -> name)
          .partition { case (Left(_), _) => true; case (Right(_), _) => false }
        val readLefts = partitioned._1.asInstanceOf[List[(Left[String, _], String)]]
        val readRights = partitioned._2.asInstanceOf[List[(Right[String, _], String)]]
        readLefts match
          case head :: next =>
            Left(s"unable to parse argument '${head._2}', reason: ${head._1.swap.getOrElse("")}")
          case Nil =>
            val processedVals = defaults.zipAll(readRights, None, (Right(""), "")).map {
              case None -> r    => r._1.getOrElse("")
              case Some(d) -> r => r._1 match { case Right("") => d; case Right(v) => v }
            }
            Right(m.fromProduct(this.SeqProduct(processedVals)))
  end readProduct

  /** Summon a list of Read[?] for the input tuple of types.
    *
    * @tparam T
    *   a tuple of types generated by a [[Mirror.ProductOf]]
    * @return
    *   the [[Read]] list
    */
  private inline def summonReads[T <: Tuple]: List[Read[_]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[Read[t]] :: summonReads[ts]

  /** Check the length of input arguments.
    * @param len
    *   the actual received argument counts
    * @param defVal
    *   the default values
    * @param arity
    *   the arity of product to check
    * @return
    *   an [[Option]], contains a [[String]] as error message if counts of input arguments are
    *   invalid
    */
  private inline def checkArgLength(len: Int, defVal: Seq[Option[_]], arity: Int): Option[String] =
    val defaultCnt = defVal.filter(_.isDefined).size
    val cntLb = arity - defaultCnt
    val cntUb = arity
    if len >= (arity - defaultCnt) && len <= arity then None
    else if cntLb == cntUb then Some(s"$cntLb arguments required, actual received: $len")
    else Some(s"$cntLb to $cntUb arguments required, actual received: $len")

  /** A transformer for [[Seq]] to [[Product]], the API caller should never use this directly.
    *
    * Since we're using deriving to get information from the [[Product]] you use to configurate
    * input arguments, there are a lot of details are implemented by `inline` methods. the
    * [[SeqProduct]] is required in the intermediate processes, it is neccessary for it to be
    * visible at the `inline` callsite.
    */
  class SeqProduct(seq: Seq[Any]) extends Product:
    override def canEqual(that: Any): Boolean = false
    override def productArity: Int = seq.size
    override def productElement(n: Int): Any = seq(n)

end ProductParsingUtils