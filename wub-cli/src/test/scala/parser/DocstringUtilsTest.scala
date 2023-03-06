package com.wildpants.wub.cli
package parser

import DocstringUtils.*

class DocstringUtilsTest extends TestBase:

  val rawDocstrings = Seq(
    """|/**
       | * main paragraph
       | * @param p1 the desc of p1
       | * @param p2 the desc of p2 
       | */""".stripMargin,
    """|/** 
       |main paragraph
       |
       |@param p1 the desc of p1
       |@param p2 the desc of p2
       |*/""".stripMargin,
    """/**
       * main paragraph
       @param p1 the
       desc of p1
       @param
       * p2
       the desc of
       p2
       */""".stripMargin
  )

  /* ------------------------ `deNoise` tests ------------------------ */

  val supposedDeNoised =
    "main paragraph @param p1 the desc of p1 @param p2 the desc of p2"

  rawDocstrings.zipWithIndex.foreach { (docstring, idx) =>
    test(s"deNoise - $idx") {
      assert(deNoise(docstring) == supposedDeNoised)
    }
  }

  /* ------------------------ `extractInfo` tests ------------------------ */

  val supposedInfo =
    "main paragraph" -> Map("p1" -> "the desc of p1", "p2" -> "the desc of p2")

  test("extractInfo") {
    assert(extractInfo(supposedDeNoised) == supposedInfo)
  }

  /* ---------------------- processDocstring ---------------------*/
  rawDocstrings.zipWithIndex.foreach { (docstring, idx) =>
    test(s"processDocstring - $idx") {
      assert(processDocstring(docstring) == supposedInfo)
    }
  }

end DocstringUtilsTest
