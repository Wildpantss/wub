package com.wildpants.wub
package app
package freq

/** Lower bound of harmonic to calculate, for any frequency < 20.0 Hz is almost unable to recognized
  * by human ear.
  */
inline val FREQ_LB = 20.0

/** Upper bound of harmonic to calculate, for any frequency > 20000.0 Hz is almost unable to
  * recognized by human ear.
  */
inline val FREQ_UB = 20000.0

/** The frequency ratio between two adjacent semitones.
  *
  * Since frequency ratio between to adjacent octave is 2, and one octave is divided by 12
  * semitones, we got:
  *
  * **1.059463 ^ 12 => [ST_RATIO] = 1.059463**
  */
inline val ST_RATIO = 1.059463

/** Default fundamental of note A0 = 440.0 Hz
  */
inline val DEFAULT_A0_FREQ = 440.0

/** Default fundamental frequency table for A0 = 440 Hz.
  */
val DEFAULT_A0_FUND_TABLE: Iterable[Double] = Array(
  262.0, 277.0, 294.0, 311.0, 330.0, 349.0, 370.0, 392.0, 415.0, 440.0, 466.0, 494.0
)
