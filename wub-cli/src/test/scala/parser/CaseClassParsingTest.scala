package com.wildpants.wub.cli
package parser

import CaseClassParsing.*

class CaseClassParsingTest extends TestBase:

  import Result.{Success, ArgSizeFailure, ReadFailure}

  /* -------- Tests for arity = 3 case-class, with default values -------- */

  case class Foo(p1: Int, p2: Double = 2.0, p3: String = "String")

  val fooTestCases = Seq(
    Seq("1", "2.5", "str") -> Success(Foo(1, 2.5, "str")),
    Seq("1") -> Success(Foo(1, 2.0, "String")),
    Seq("1", "2.5", "str", "extra") -> ArgSizeFailure(4, 1 -> 3),
    Seq() -> ArgSizeFailure(0, 1 -> 3),
    Seq("a") -> ReadFailure("p1", "a", "invalid input for {Int}"),
    Seq("1", "a", "a") -> ReadFailure("p2", "a", "invalid input for {Double}"),
    Seq("1", "a", "a", "4") -> ArgSizeFailure(4, 1 -> 3)
  )

  test("parseCaseClass[T]: Foo(p1: Int, p2: Double=2.0, p3: String='String')") {
    fooTestCases.foreach { (input, supposed) =>
      parseCaseClass[Foo](input) should be(supposed)
    }
  }

  /* ---------------- Tests for arity = 0 case-class ---------------- */

  case class Bar()

  val barTestCases = Seq(
    Seq() -> Success(Bar),
    Seq("1") -> ArgSizeFailure(1, 0 -> 0)
  )

  test("parseCaseClass[T]: Bar()") {
    fooTestCases.foreach { (input, supposed) =>
      parseCaseClass[Foo](input) should be(supposed)
    }
  }

end CaseClassParsingTest
