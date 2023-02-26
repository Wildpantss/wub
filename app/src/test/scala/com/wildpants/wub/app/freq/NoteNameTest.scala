package com.wildpants.wub
package app
package freq

import cli.parser.Read

class NoteNameTest extends TestBase:

  import NoteName.*

  /* ------------------------ Test NoteName.displayName ------------------------ */

  val displayNameCases: Seq[(NoteName, String)] = Seq(
    C -> "C",
    Cs -> "C#",
    D -> "D",
    Ds -> "D#",
    E -> "E",
    F -> "F",
    Fs -> "F#",
    G -> "G",
    Gs -> "G#",
    A -> "A",
    As -> "A#"
  )

  displayNameCases.foreach { tc =>
    val noteName = tc._1
    val supposed = tc._2
    test(s"NoteName.display - $noteName") { noteName.displayName should be(supposed) }
  }

  /* ------------------------ Test NoteName.ReadNoteName ------------------------ */

  val errMessageTmpl = "input '%s' is not a valid note-name."

  val readNoteNameSuccessCases: Seq[(String, Either[String, NoteName])] = Seq(
    Seq("C", "c").map(_ -> Right(C)),
    Seq("C#", "c#", "Cs", "cs", "CS", "cS").map(_ -> Right(Cs)),
    Seq("D", "d").map(_ -> Right(D)),
    Seq("D#", "d#", "Ds", "ds", "DS", "dS").map(_ -> Right(Ds)),
    Seq("E", "e").map(_ -> Right(E)),
    Seq("F", "f").map(_ -> Right(F)),
    Seq("F#", "f#", "Fs", "fs", "FS", "fS").map(_ -> Right(Fs)),
    Seq("G", "g").map(_ -> Right(G)),
    Seq("G#", "g#", "Gs", "gs", "GS", "gS").map(_ -> Right(Gs)),
    Seq("A", "a").map(_ -> Right(A)),
    Seq("A#", "a#", "As", "as", "AS", "aS").map(_ -> Right(As)),
    Seq("B", "b").map(_ -> Right(B)),
    Seq("CR", "#$", "x#", "Xs", "f!", "ZS").map(x => x -> Left(errMessageTmpl format x))
  ).flatten

  readNoteNameSuccessCases.foreach { tc =>
    val input = tc._1
    val supposed = tc._2
    test(s"NoteName.ReadNoteName - '$input'") { Read[NoteName].read(input) should be(supposed) }
  }

end NoteNameTest
