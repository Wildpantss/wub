package com.wildpants.wub.cli
package parser

import InfoInspection.*

class InfoInspectionTest extends TestBase:

  /** Desc of foo
    *
    * @param a1
    *   the desc of a1
    * @param a2
    *   the desc of a2
    */
  case class Foo(a1: Int, a2: Boolean = false)

  val suppFooInfo = CmdInfo(
    "Foo",
    "Desc of foo",
    List(
      ArgInfo("a1", "the desc of a1", true),
      ArgInfo("a2", "the desc of a2", false)
    )
  )

  test("inspectCmdInfo") {
    inspectCmdInfo[Foo] should be(suppFooInfo)
  }

end InfoInspectionTest
