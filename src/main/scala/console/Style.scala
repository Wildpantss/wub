package com.wildpants.wub
package console

import scala.annotation.{targetName, unused}
import scala.io.AnsiColor.*

/**
 * = Style =
 *
 * The [[String]]'s console output style for extension method [[Style.style]].
 *
 * Styles are composable, you could call method [[compose]] or use operator [[+]] to compose styles.
 *
 * @example composing 'blue' and 'bold'
 *          {{{
 *          import style.*
 *          println("123".style(Blue + Bold))
 *          println("456" <<< (Blue + Bold))
 *          println("456" <<< Blue <<< Bold))
 *          }}}
 * @author wildpants
 * */
//noinspection ScalaWeakerAccess
class Style (val decorator: String => String) {
  /**
   * == Style.compose ==
   * 
   * Compose the current [[Style]] with another.
   * 
   * @example compose 'blue' with 'underline'
   *          {{{
   *          import Style.*
   *          val style = Blue + Underline
   *          }}}
   *          
   * @param other the other [[Style]] to compose with this.
   * @return a new [[Style]] composed from this and other.
   * */
  def compose(other: Style): Style = Style(this.decorator compose other.decorator)

  /**
   * Alias for [[compose]]
   * */
  @targetName("+") // target-name not required for an alias.
  def + : Style => Style = compose
}

//noinspection ScalaUnusedSymbol,ScalaWeakerAccess
object Style {
  /**
   * A [[Style]] that represents the ANSI black.
   * */
  object Black      extends Style(BLACK       + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI red.
   * */
  object Red        extends Style(RED         + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI green.
   * */
  object Green      extends Style(GREEN       + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI yellow.
   * */
  object Yellow     extends Style(YELLOW      + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI blue.
   * */
  object Blue       extends Style(BLUE        + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI magenta.
   * */
  object Magenta    extends Style(MAGENTA     + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI cyan.
   * */
  object Cyan       extends Style(CYAN        + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI white.
   * */
  object White      extends Style(WHITE       + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI black background.
   * */
  object BlackB     extends Style(BLACK_B     + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI red background.
   * */
  object RedB       extends Style(RED_B       + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI green background.
   * */
  object GreenB     extends Style(GREEN_B     + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI yellow background.
   * */
  object YellowB    extends Style(YELLOW_B    + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI blue background.
   * */
  object BlueB      extends Style(BLUE_B      + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI magenta background.
   * */
  object MagentaB   extends Style(MAGENTA_B   + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI cyan background.
   * */
  object CyanB      extends Style(CYAN_B      + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI white background.
   * */
  object WhiteB     extends Style(WHITE_B     + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI bold.
   * */
  object Bold       extends Style(BOLD        + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI underline.
   * */
  object Underline  extends Style(UNDERLINED  + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI blink.
   * */
  object Blink      extends Style(BLINK       + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI reverse.
   * */
  object Reverse    extends Style(REVERSED    + _ + RESET)
  /**
   * A [[Style]] that represents the ANSI invisible.
   * */
  object Invisible  extends Style(INVISIBLE   + _ + RESET)


  extension (self: String) {
    
    /**
     * Decorate a string with parameterized [[Style]].
     * */
    def style(s: Style): String = s.decorator(self)

    /**
     * Alias for [[style]].
     * */
    @targetName("<<<") // target-name not required for an alias.
    def <<< : Style => String = style


    /* --- Shortcuts for specific single style --- */

    /** Decorate a string with foreground color for ANSI black */
    def black     : String = BLACK      + self + RESET
    /** Decorate a string with foreground color for ANSI red */
    def red       : String = RED        + self + RESET
    /** Decorate a string with foreground color for ANSI green */
    def green     : String = GREEN      + self + RESET
    /** Decorate a string with foreground color for ANSI yellow */
    def yellow    : String = YELLOW     + self + RESET
    /** Decorate a string with foreground color for ANSI blue */
    def blue      : String = BLUE       + self + RESET
    /** Decorate a string with foreground color for ANSI magenta */
    def magenta   : String = MAGENTA    + self + RESET
    /** Decorate a string with foreground color for ANSI cyan */
    def cyan      : String = CYAN       + self + RESET
    /** Decorate a string with foreground color for ANSI white */
    def white     : String = WHITE      + self + RESET

    /** Decorate a string with foreground color for ANSI backgrounded black */
    def blackBg   : String = BLACK_B    + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded red */
    def redBg     : String = RED_B      + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded green */
    def greenBg   : String = GREEN_B    + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded yellow */
    def yellowBg  : String = YELLOW_B   + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded blue */
    def blueBg    : String = BLUE_B     + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded magenta */
    def magentaBg : String = MAGENTA_B  + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded cyan */
    def cyanBg    : String = CYAN_B     + self + RESET
    /** Decorate a string with foreground color for ANSI backgrounded white */
    def whiteBg   : String = WHITE_B    + self + RESET

    /** Decorate a string with foreground color for ANSI bold */
    def bold      : String = BOLD       + self + RESET
    /** Decorate a string with foreground color for ANSI underlined */
    def underlined: String = UNDERLINED + self + RESET
    /** Decorate a string with foreground color for ANSI blink */
    def blink     : String = BLINK      + self + RESET
    /** Decorate a string with foreground color for ANSI reversed */
    def reversed  : String = REVERSED   + self + RESET
    /** Decorate a string with foreground color for ANSI invisible */
    def invisible: String = INVISIBLE   + self + RESET
  }
}
