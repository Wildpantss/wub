package com.wildpants.wub
package app.freq

import cli.parser.Read

/** An enum as ''ADT'' for possible names of notes. (currently 12-tone equal
  * temperament only)
  *
  * @note
  *   this enumeration has implemented [[Read]] type-class, so it can be read
  *   from [[String]].
  * @author
  *   wildpants
  */
enum NoteName(val displayName: String):
  case C extends NoteName("C")
  case Cs extends NoteName("C#")
  case D extends NoteName("D")
  case Ds extends NoteName("D#")
  case E extends NoteName("E")
  case F extends NoteName("F")
  case Fs extends NoteName("F#")
  case G extends NoteName("G")
  case Gs extends NoteName("G#")
  case A extends NoteName("A")
  case As extends NoteName("A#")
  case B extends NoteName("B")
end NoteName

object NoteName:
  given ReadNoteName: Read[NoteName] = (str: String) =>
    str.toLowerCase match
      case "c"         => Right(C)
      case "c#" | "cs" => Right(Cs)
      case "d"         => Right(D)
      case "d#" | "ds" => Right(Ds)
      case "e"         => Right(E)
      case "f"         => Right(F)
      case "f#" | "fs" => Right(Fs)
      case "g"         => Right(G)
      case "g#" | "gs" => Right(Gs)
      case "a"         => Right(A)
      case "a#" | "as" => Right(As)
      case "b"         => Right(B)
      case _           => Left(s"input '$str' is not a valid note-name.")
  end ReadNoteName
end NoteName
