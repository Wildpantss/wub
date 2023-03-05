package com.wildpants.wub.cli
package parser

import CaseClassInspect.*

class CaseClassInspectTest extends TestBase:

  /* ------------ Case class types to inspect ------------ */

  case class Foo(p1: Int)
  case class Fax(p1: Int, p2: String)
  case class Bar(p1: Int, p2: String = "default string")
  case class Bax(p1: Int = 1, p2: String = "default string")
  case class Baz()

  /* ---------------- Test definitions ---------------- */

  test("getArity[T]") {
    getArity[Foo] should be(1)
    getArity[Fax] should be(2)
    getArity[Bar] should be(2)
    getArity[Bax] should be(2)
    getArity[Baz] should be(0)
  }

  test("getFieldNames[T]") {
    getFieldNames[Foo] should be(List("p1"))
    getFieldNames[Fax] should be(List("p1", "p2"))
    getFieldNames[Bar] should be(List("p1", "p2"))
    getFieldNames[Bax] should be(List("p1", "p2"))
    getFieldNames[Baz] should be(Nil)
  }

  test("getDefaults[T]") {
    getDefaults[Foo] should be(List(None))
    getDefaults[Fax] should be(List(None, None))
    getDefaults[Bar] should be(List(None, Some("default string")))
    getDefaults[Bax] should be(List(Some(1), Some("default string")))
    getDefaults[Baz] should be(Nil)
  }

end CaseClassInspectTest
