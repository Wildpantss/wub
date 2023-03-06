package com.wildpants.wub.cli
package parser

import scala.collection.mutable.ListBuffer

private object NameFormatUtils:

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

private object DocstringUtils:

  /** Composition of [[deNoise]] and [[extractInfo]].
    *
    * @param docstring
    *   the input raw docstring
    * @return
    *   (command-desc, Map(arg-name -> arg-desc))
    */
  def processDocstring(docstring: String): (String, Map[String, String]) =
    (deNoise andThen extractInfo)(docstring)

  /** Strip the noisy '*' syntaxes, get the actual docstring content as a single
    * line [[String]] separated with blank-space.
    *
    * @param docstring
    *   the input raw docstring
    * @return
    *   the processed single-line [[String]]
    */
  def deNoise(docstring: String): String = docstring
    .strip
    .stripPrefix("/**")
    .stripSuffix("*/")
    .linesIterator
    .map(_.strip)
    .map(_.stripPrefix("*"))
    .map(_.strip)
    .filter(_ != "")
    .reduceOption(_ + " " + _)
    .getOrElse("")

  /** Extract information from de-noised docstring.
    *
    * @param deNoised
    *   the de-noised docstring
    * @return
    *   (command-desc, Map(arg-name -> arg-desc))
    */
  def extractInfo(deNoised: String): (String, Map[String, String]) =
    deNoised.split("@param").map(_.strip).toList match
      case head :: tails => (head, tails.map(parseParamString).toMap)
      case Nil           => ("", Map.empty)

  /* ---------------- Private Functions ----------------*/

  private def parseParamString(paramString: String): (String, String) =
    val errMsg = "paramString should contain a name and an optional description"
    val exeception = IllegalStateException(errMsg)
    paramString.split(" ").map(_.strip).toList match
      case p :: desc => (p, desc.reduceOption(_ + " " + _).getOrElse(""))
      case Nil       => throw exeception

end DocstringUtils
