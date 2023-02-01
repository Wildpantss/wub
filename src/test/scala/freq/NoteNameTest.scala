package com.wildpants.wub
package freq

class NoteNameTest extends TestBase {

  import NoteName.*
  import NoteName.ReadNoteName
  import utils.Read.read

  /* ------------------------ Test NoteName.displayName ------------------------ */

  val displayNameCases: Seq[(NoteName, String)] = Seq(
    C  -> "C",
    Cs -> "C#",
    D  -> "D",
    Ds -> "D#",
    E  -> "E",
    F  -> "F",
    Fs -> "F#",
    G  -> "G",
    Gs -> "G#",
    A  -> "A",
    As -> "A#",
  )

  displayNameCases.foreach { tc =>
    val noteName = tc._1
    val supposed = tc._2
    test(s"NoteName.display - $noteName") { noteName.displayName should be (supposed) }
  }

  /* ------------------------ Test NoteName.ReadNoteName ------------------------ */

  val errMessageTmpl = "input %s is not a valid note-name."

  val readNoteNameSuccessCases: Seq[(String, Either[String, NoteName])] = Seq(
    "C"  -> Right(C), "c"   -> Right(C),
    "C#" -> Right(Cs), "c#" -> Right(Cs), "Cs" -> Right(Cs), "cs" -> Right(Cs), "CS" -> Right(Cs), "cS" -> Right(Cs),
    "D"  -> Right(D), "d"   -> Right(D),
    "D#" -> Right(Ds), "d#" -> Right(Ds), "Ds" -> Right(Ds), "ds" -> Right(Ds), "DS" -> Right(Ds), "dS" -> Right(Ds),
    "E"  -> Right(E), "e"   -> Right(E),
    "F"  -> Right(F), "f"   -> Right(F),
    "F#" -> Right(Fs), "f#" -> Right(Fs), "Fs" -> Right(Fs), "fs" -> Right(Fs), "FS" -> Right(Fs), "fS" -> Right(Fs),
    "G"  -> Right(G), "g"   -> Right(G),
    "G#" -> Right(Gs), "g#" -> Right(Gs), "Gs" -> Right(Gs), "gs" -> Right(Gs), "GS" -> Right(Gs), "gS" -> Right(Gs),
    "A"  -> Right(A), "a"   -> Right(A),
    "A#" -> Right(As), "a#" -> Right(As), "As" -> Right(As), "as" -> Right(As), "AS" -> Right(As), "aS" -> Right(As),
    "B"  -> Right(B), "b"   -> Right(B),

    "CR" -> Left(errMessageTmpl format "CR"),
    "#$" -> Left(errMessageTmpl format "#$"),
    "x#" -> Left(errMessageTmpl format "x#"),
    "Xs" -> Left(errMessageTmpl format "Xs"),
    "f!" -> Left(errMessageTmpl format "f!"),
    "ZS" -> Left(errMessageTmpl format "ZS"),
  )

  readNoteNameSuccessCases.foreach { tc =>
    val input = tc._1
    val supposed = tc._2
    test(s"NoteName.ReadNoteName - '$input'") { input.read[NoteName] should be (supposed) }
  }
}
