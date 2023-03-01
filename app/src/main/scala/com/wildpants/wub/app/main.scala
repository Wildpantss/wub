package com.wildpants.wub
package app

import cli.parser.*
import cli.parser.syntax.*
import cli.console.ConsoleOps.*

import freq.{NoteName, freqTask}

@main
def main(args: String*): Unit =

  val builder = app {
    name { "wub" }
    version { "0.0.1" }
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
