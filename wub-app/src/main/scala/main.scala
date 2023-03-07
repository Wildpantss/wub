package com.wildpants.wub
package app

import cli.parser.*
import cli.console.*
import freq.*

@main
def main(args: String*): Unit =

  /* ---------------- Task argument entities ---------------- */

  /** Harmonic series calculation and displaying
    *
    * @param noteName
    *   Name of note to calculate harmonics
    * @param standardFreq
    *   Standard 'A' frequency as calculation reference
    */
  case class Freq(noteName: NoteName, standardFreq: Double = 440.0)

  val FREQ_TASK = (noteName: NoteName, standardFreq: Double) =>
    Right { STDOUT << Note(noteName, standardFreq).displayFormat() << NL; () }

  CliAppBuilder()
    .name("wub")
    .author("wildpants")
    .version("0.1.0")
    .desc("Wub is a CLI util-box for music production by Wildpants ^_^")
    .cmd[Freq](f => FREQ_TASK(f.noteName, f.standardFreq))
    .build
    .launch(args)

end main
