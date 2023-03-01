package com.wildpants.wub
package app.freq

import NoteName.*
class NoteTest extends TestBase:

  /* ------------------------ Tests for Note.fund ------------------------ */

  val checkFundCases: Seq[Map[String, NoteName | Double]] = Seq(
    // A = 440 Hz test cases
    Map("note" -> C, "supposed" -> 262.0),
    Map("note" -> Cs, "supposed" -> 277.0),
    Map("note" -> D, "supposed" -> 294.0),
    Map("note" -> Ds, "supposed" -> 311.0),
    Map("note" -> E, "supposed" -> 330.0),
    Map("note" -> F, "supposed" -> 349.0),
    Map("note" -> Fs, "supposed" -> 370.0),
    Map("note" -> G, "supposed" -> 392.0),
    Map("note" -> Gs, "supposed" -> 415.0),
    Map("note" -> A, "supposed" -> 440.0),
    Map("note" -> As, "supposed" -> 466.0),
    Map("note" -> B, "supposed" -> 494.0),

    // A != 440 Hz test cases
    Map("note" -> C, "supposed" -> 257.0, "std" -> 432.0),
    Map("note" -> Cs, "supposed" -> 278.0, "std" -> 441.0),
    Map("note" -> D, "supposed" -> 340.0, "std" -> 510.0),
    Map("note" -> Ds, "supposed" -> 71.00, "std" -> 100.0),
    Map("note" -> E, "supposed" -> 249.0, "std" -> 332.0),
    Map("note" -> F, "supposed" -> 317.0, "std" -> 400.0),
    Map("note" -> Fs, "supposed" -> 336.0, "std" -> 400.0)
  )

  checkFundCases.foreach { tc =>
    val noteName = tc("note").asInstanceOf[NoteName]
    val supposed = tc("supposed").asInstanceOf[Double]
    val stdFreq = tc.getOrElse("std", 440.0).asInstanceOf[Double]
    test(f"[A=$stdFreq%.1fHz] Note.fund - ${noteName.displayName}") {
      Note(noteName, stdFreq).fund should be(supposed)
    }
  }

  /* ------------------------ Tests for Note.harmonics ------------------------ */

  val checkHarmonicsCases: Seq[(Double, Array[Double])] = Seq(
    262.0 -> Array(32.75, 65.50, 131.0, 262.0, 524.0, 1048.0, 2096.0, 4192.0, 8384.0, 16768.0),
    485.0 -> Array(30.31, 60.63, 121.25, 242.50, 485.0, 970.0, 1940.0, 3880.0, 7760.0, 15520.0),
    333.0 -> Array(20.81, 41.63, 83.25, 166.50, 333.0, 666.0, 1332.0, 2664.0, 5328.0, 10656.0),
    300.0 -> Array(37.50, 75.0, 150.0, 300.0, 600.0, 1200.0, 2400.0, 4800.0, 9600.0, 19200.0),
    318.0 -> Array(39.75, 79.50, 159.0, 318.0, 636.0, 1272.0, 2544.0, 5088.0, 10176.0)
  )

  checkHarmonicsCases.foreach { tc =>
    val fund = tc._1
    val supposed = tc._2
    test(f"[A=${fund}Hz] Note.harmonics") {
      val actual = Note(A, fund).harmonics.map(d => f"$d%.2f").toList
      val expected = supposed.map(d => f"$d%.2f").toList
      actual should be(expected)
    }
  }

end NoteTest
