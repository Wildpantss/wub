package com.wildpants.wub
package app
package freq

import cli.console.Style.*

import scala.collection.immutable.LazyList as LL
import scala.util.Properties.lineSeparator as endl

/** Represents a note in frequency calculation.
  *
  * @param name
  *   the name of note, a [[NoteName]] instance.
  * @param stdFreq
  *   the standard 'A' frequency as reference of frequency calculation.
  * @param fund
  *   the fundamental frequency of note.
  * @param harmonics
  *   an [[Iterable]] of frequencies that represents the harmonics in [20, 20000]Hz.
  *
  * @author
  *   wildpants
  */
case class Note(name: NoteName, stdFreq: Double, fund: Double, harmonics: Iterable[Double]):

  /** Get the prettified string representation for console output. (with ANSI color)
    *
    * @example
    *   a simple example of A# with A = 432Hz
    *   ```text
    *   ---- Harmony series of 'A#'
    *   ---- Standard A0 = [432 Hz]
    *
    *        [28.62             Hz]
    *        [57.25             Hz]
    *        [114.50            Hz]
    *        [229.00            Hz]
    *   >>   [458.00            Hz]
    *        [916.00            Hz]
    *        [1832.00           Hz]
    *        [3664.00           Hz]
    *        [7328.00           Hz]
    *        [14656.00          Hz]
    *   ```
    */
  def displayFormat(): String =
    StringBuilder(endl)
      .append {
        "---- Harmonic series of '%s'".format(this.name.displayName.underlined.yellow.bold)
      }
      .append { endl }
      .append { "---- Standard A = [%s Hz]".format(this.stdFreq.toString.bold.yellow) }
      .append { endl * 2 }
      .append {
        this.harmonics
          .map(freq =>
            freq match
              case this.fund => ">>   [%sHz]".format(f"$freq%-19.2f").underlined.bold.yellow
              case _         => "     [%sHz]".format(f"$freq%-19.2f")
          )
          .reduce(_ + endl + _)
      }
      .append { endl }
      .toString

end Note

object Note:

  /** Construct a new instance of [[Note]].
    *
    * @note
    *   the input [[NoteName]] is an ''ADT'', hence all inputs are valid.
    *
    * @param name
    *   a [[NoteName]] as name of the note.
    * @param stdFreq
    *   the standard 'A' frequency.
    */
  def apply(name: NoteName, stdFreq: Double = 440.0): Note =
    val fund = calcFundamental(name, stdFreq)
    val harmonics = calcHarmonics(fund)
    new Note(name, stdFreq, fund, harmonics)

  /* -------------------------------- Package Internal Stuff -------------------------------- */

  private def calcFundamental(noteName: NoteName, stdFreq: Double): Double =
    if stdFreq == DEFAULT_A0_FREQ then DEFAULT_A0_FUND_TABLE.toList(noteName.ordinal)
    else (0 to 12).map(i => (stdFreq * Math.pow(ST_RATIO, i - 9)).round).toList(noteName.ordinal)

  private def calcHarmonics(fund: Double): Iterable[Double] =
    val lPart = LL.iterate(fund / 2)(_ / 2).takeWhile(_ >= FREQ_LB).reverse
    val rPart = LL.iterate(fund)(_ * 2).takeWhile(_ <= FREQ_UB)
    lPart #::: rPart

end Note
