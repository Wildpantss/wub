package com.wildpants.wub
package app

import cli.parser.*
import CliApp.*
import cli.console.ConsoleOps.*
import freq.{NoteName, Note}

@main
def main(args: String*): Unit =

  val app = CliApp(
    name = "wub",
    version = "0.0.3",
    author = "wildpants",
    desc = "Wub is a CLI util-box for music production by Wildpants ^_^",
    "freq" -> task[Freq](freq =>
      Right(printLine(Note(freq.noteName, freq.standardFreq).displayFormat()))
    )
  )

  app.launch(args)

end main

/* ---------------- Task argument entities ---------------- */

/** harmonic series calculation and displaying
  *
  * @param noteName
  *   name of note to calculate harmonics
  * @param standardFreq
  *   standard 'A' frequency as calculation reference
  */
case class Freq(noteName: NoteName, standardFreq: Double = 440.0)
