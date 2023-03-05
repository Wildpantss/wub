package com.wildpants.wub.cli
package parser

import CCParsing.*


class CCParsingTest extends TestBase:

  /* -------- Tests for arity = 3 case-class, with default values -------- */

  case class Foo(p1: Int, p2: Double = 2.0, p3: String = "String")


  val fooTestCases = Seq(
    Seq("1", "2.5", "str") -> Right(Foo(1, 2.5, "str")),
    Seq("1") -> Right(Foo(1, 2.0, "String")),
    Seq("1", "2.5", "str", "extra") -> Left(
      "1 to 3 arguments required, received 4"
    ),
    Seq() -> Left("1 to 3 arguments required, received 0"),
    Seq("a") -> Left("invalid given value 'a' for argument 'p1': not a 'Int'"),
    Seq("1", "a", "a") -> Left(
      "invalid given value 'a' for argument 'p2': not a 'Double'"
    ),
    Seq("1", "a", "a", "4") -> Left("1 to 3 arguments required, received 4")
  )


  test("parseCaseClass[T]: Foo(p1: Int, p2: Double=2.0, p3: String='String')") {
    fooTestCases.foreach { (input, supposed) =>
      parseCaseClass[Foo](input) should be(supposed)
    }
  }


  /* ---------------- Tests for arity = 0 case-class ---------------- */

  case class Bar()


  val barTestCases = Seq(
    Seq() -> Right(Bar),
    Seq("1") -> Left("no argument required, received 1")
  )


  test("parseCaseClass[T]: Bar()") {
    fooTestCases.foreach { (input, supposed) =>
      parseCaseClass[Foo](input) should be(supposed)
    }
  }

end CCParsingTest
