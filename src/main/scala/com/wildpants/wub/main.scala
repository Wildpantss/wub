package com.wildpants.wub

import app.freq.{NoteName, freqTask}

import cli.console.ConsoleOps.*
import cli.parser.*
import cli.parser.syntax.*

@main
def main(args: String*): Unit =

  val builder = app {
    name { "wub" }
    version { "0.0.3" }
    author { "wildpants" }
    desc { "Wub is a CLI util-box for music production by Wildpants ^_^" }
    command { "freq" -> freqTask }
  }

  CliApp { builder } launch args

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
