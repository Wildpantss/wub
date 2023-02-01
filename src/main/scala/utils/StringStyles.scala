package com.wildpants.wub
package utils

import scala.annotation.unused
import scala.io.AnsiColor.*

/**
 * = StringStyles =
 *
 * A group of extension methods that turns a [[String]] into correspond ansi-colored string.
 *
 * @see [[scala.io.AnsiColor]]
 */
object StringStyles {

  extension (self: String) {
    /** Decorate a string with foreground color for ANSI black */
    @unused def black       : String = BLACK      + self + RESET
    /** Decorate a string with foreground color for ANSI red */
    @unused def red         : String = RED        + self + RESET
    /** Decorate a string with foreground color for ANSI green */
    @unused def green       : String = GREEN      + self + RESET
    /** Decorate a string with foreground color for ANSI yellow */
    @unused def yellow      : String = YELLOW     + self + RESET
    /** Decorate a string with foreground color for ANSI blue */
    @unused def blue        : String = BLUE       + self + RESET
    /** Decorate a string with foreground color for ANSI magenta */
    @unused def magenta     : String = MAGENTA    + self + RESET
    /** Decorate a string with foreground color for ANSI cyan */
    @unused def cyan        : String = CYAN       + self + RESET
    /** Decorate a string with foreground color for ANSI white */
    @unused def white       : String = WHITE      + self + RESET

    /** Decorate a string with foreground color for ANSI backgrounded black */
    @unused def blackBg     : String = BLACK_B    + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded red */
    @unused def redBg       : String = RED_B      + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded green */
    @unused def greenBg     : String = GREEN_B    + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded yellow */
    @unused def yellowBg    : String = YELLOW_B   + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded blue */
    @unused def blueBg      : String = BLUE_B     + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded magenta */
    @unused def magentaBg   : String = MAGENTA_B  + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded cyan */
    @unused def cyanBg      : String = CYAN_B     + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded white */
    @unused def whiteBg     : String = WHITE_B    + self + RESET

    /** Decorate a string with foreground color for ANSI bold */
    @unused def bold        : String = BOLD       + self + RESET
    /** Decorate a string with foreground color for ANSI underlined */
    @unused def underlined  : String = UNDERLINED + self + RESET
    /** Decorate a string with foreground color for ANSI blink */
    @unused def blink       : String = BLINK      + self + RESET
    /** Decorate a string with foreground color for ANSI reversed */
    @unused def reversed    : String = REVERSED   + self + RESET
    /** Decorate a string with foreground color for ANSI invisible */
    @unused def invisible   : String = INVISIBLE  + self + RESET
  }
}
