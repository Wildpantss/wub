package com.wildpants.wub
package tasks

import freq.*
import console.Style.*
import freq.NoteName.*
import parser.Read.*
import parser.TaskUsage.*
import parser.{Description, Task, TaskLauncher, TaskUsage}

class FreqTask extends Task {

  override val argSizeRange: (Int, Int) = 1 -> 2
  
  override val name     : String = "freq"
  override val descShort: String = "harmonic series calculation and displaying"
  override val descLong : Description = Description("freq is a task that calculates harmonic series of a note for you")
  override val usage    : TaskUsage = {
    val desc1 = "name of note to calculate harmonics                { required, e.g. 'a', 'as', 'a#', case-ignored }"
    val desc2 = "standard 'A' frequency as calculation reference    { optional, number }"
    TaskUsage { Builder() <<< "freq" <<< ("note-name", Required, desc1) <<< ("standard-freq", Optional, desc2) }
  }

  override def execute(args: Seq[String]): Either[String, Unit] = {
    processArguments(args) match {
      case Left(message) => Left(message)
      case Right(noteName, standardFreq) => println(Note(noteName, standardFreq).displayFormat()); Right(())
    }
  }
  
  /* ------------------------ private helper method ------------------------ */
  private def processArguments(args: Seq[String]): Either[String, (NoteName, Double)] = {
    checkArgSize(args.size).flatMap { _ =>                                // after size-check, the size could be 1 or 2
      args.head.read[NoteName].flatMap { noteName => args.size match      // flatmap on legal 1st arg
        case 1 => Right((noteName, DEFAULT_A0_FREQ))                      // only 1 arg, 2nd set to default
        case 2 => args(1).read[Double].flatMap { _ match                  // flatmap on legal 2nd arg
          case v if v > FREQ_UB || v < FREQ_LB => Left {
            s"frequency of 'A' should in range ${ "[20, 20000]" <<< Green } Hz"
          }
          case v => Right((noteName, v))
        }
      }
    }
  }
}
