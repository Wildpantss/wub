package com.wildpants.wub.cli
package parser

import scala.collection.mutable.ListBuffer

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
    val words = ListBuffer.empty[String]
    val indexes = str.zipWithIndex.filter(_._1.isUpper).map(_._2)
    var curIdx = 0
    indexes.foreach { i =>
      words.addOne(str.slice(curIdx, i)); curIdx = i
    }
    words.addOne(str.slice(curIdx, str.length))
    if words.head == "" then words.remove(0)
    words.map(_.toLowerCase).reduceOption(_ + "-" + _).getOrElse("")

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
      case head :: Nil => head
      case head :: tails =>
        head ++ tails.map(w => w.updated(0, w(0).toUpper)).reduce(_ + _)
      case _ => throw IllegalStateException()

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
