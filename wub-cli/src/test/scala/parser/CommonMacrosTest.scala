package com.wildpants.wub.cli
package parser

import CommonMacros.Entr.*

class CommonMacrosTest extends TestBase:

  /* ------------ Case class types to inspect ------------ */

  case class Foo(p1: Int)
  case class Fax(p1: Int, p2: String)
  case class Bar(p1: Int, p2: String = "default string")
  case class Bax(p1: Int = 1, p2: String = "default string")
  case class Baz()

  /* ---------------- Test definitions ---------------- */

  test("inspectTypeName[T]") {
    inspectTypeName[Foo] should be("Foo")
    inspectTypeName[Fax] should be("Fax")
    inspectTypeName[Bar] should be("Bar")
    inspectTypeName[Bax] should be("Bax")
    inspectTypeName[Baz] should be("Baz")
  }

  test("inspectArity[T]") {
    inspectArity[Foo] should be(1)
    inspectArity[Fax] should be(2)
    inspectArity[Bar] should be(2)
    inspectArity[Bax] should be(2)
    inspectArity[Baz] should be(0)
  }

  test("inspectDefaults[T]") {
    inspectDefaults[Foo] should be(List(None))
    inspectDefaults[Fax] should be(List(None, None))
    inspectDefaults[Bar] should be(List(None, Some("default string")))
    inspectDefaults[Bax] should be(List(Some(1), Some("default string")))
    inspectDefaults[Baz] should be(Nil)
  }

  test("inspectRequired[T]") {
    inspectRequired[Foo] should be(List(true))
    inspectRequired[Fax] should be(List(true, true))
    inspectRequired[Bar] should be(List(true, false))
    inspectRequired[Bax] should be(List(false, false))
    inspectRequired[Baz] should be(Nil)
  }

  test("inspectFieldNames[T]") {
    inspectFieldNames[Foo] should be(List("p1"))
    inspectFieldNames[Fax] should be(List("p1", "p2"))
    inspectFieldNames[Bar] should be(List("p1", "p2"))
    inspectFieldNames[Bax] should be(List("p1", "p2"))
    inspectFieldNames[Baz] should be(Nil)
  }

end CommonMacrosTest
