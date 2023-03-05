package com.wildpants.wub.cli
package parser

import NameFormatUtils.*

class NameFormatUtilsTest extends TestBase {

  val testCases = Seq(
    Map(
      "kebab" -> "single",
      "camel" -> "single",
      "pascal" -> "Single"
    ),
    Map(
      "kebab" -> "two-word",
      "camel" -> "twoWord",
      "pascal" -> "TwoWord"
    ),
    Map(
      "kebab" -> "three-word-shit",
      "camel" -> "threeWordShit",
      "pascal" -> "ThreeWordShit"
    )
  )

  // kebab -> camel
  testCases.foreach { tc =>
    val (kebab, camel) = tc("kebab") -> tc("camel")
    val testName = s"kebab -> camel ('$kebab' -> '$camel')"
    test(testName) { kebabToCamel(kebab) should be(camel) }
  }

  // kebab -> pascal
  testCases.foreach { tc =>
    val (kebab, pascal) = tc("kebab") -> tc("pascal")
    val testName = s"kebab -> pascal ('$kebab' -> '$pascal')"
    test(testName) { kebabToPascal(kebab) should be(pascal) }
  }

  // camel -> kebab
  testCases.foreach { tc =>
    val (camel, kebab) = tc("camel") -> tc("kebab")
    val testName = s"camel -> kebab ('$camel' -> '$kebab')"
    test(testName) { camelOrPascalToKebab(camel) should be(kebab) }
  }

  // pascal -> kebab
  testCases.foreach { tc =>
    val (pascal, kebab) = tc("pascal") -> tc("kebab")
    val testName = s"pascal -> kebab ('$pascal' -> '$kebab')"
    test(testName) { camelOrPascalToKebab(pascal) should be(kebab) }
  }
}
