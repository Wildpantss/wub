package com.wildpants.wub
package tasks

import freq.NoteName.*
import freq.{Note, NoteName}
import parser.Read.*
import parser.TaskUsage.*
import parser.{Description, Task, TaskLauncher, TaskUsage}

class FreqTask extends Task {

  override val name: String = "freq"
  override val argSizeRange: (Int, Int) = 1 -> 2
  override val descShort: String = "harmonic series calculation and displaying"
  override val usage: TaskUsage = TaskUsage { Builder() <<< "freq"
    <<< ("note-name", Required, "name of note to calculate harmonics")
    <<< ("standard-freq", Optional, "standard 'A' frequency as calculation reference")
  }
  override val descLong: Description = Description("freq is a task that calculates harmonic series of a note for you")


  override def execute(args: Seq[String]): Either[String, Unit] = {
    processArguments(args) match {
      case Left(message) => Left(message)
      case Right(noteName, standardFreq) => println(Note(noteName, standardFreq).displayFormat()); Right(())
    }
  }

  private def processArguments(args: Seq[String]): Either[String, (NoteName, Double)] = {
    checkArgSize(args.size) match {
      case Left(message) => Left(message) // arg size incorrect, fail with message
      case Right(_) => args.size match { // legal arg size
        case 1 | 2 =>
          val noteName = args.head.read[NoteName] match { // try to handle 1st arg
            case Left(message) => return Left(message) // 1st arg is illegal, fail with message
            case Right(v) => v // 1st arg is legal
          }
          val standardFreq = args.size match { // try to handle 2nd arg
            case 1 => freq.DEFAULT_A0_FREQ // set to default if missed
            case 2 => args(1).read[Double] match { // try to read 2nd arg
              case Left(message) => return Left(message) // 2nd arg is illegal, fail with message
              case Right(v) => v // 2nd arg is legal
            }
          }
          Right(noteName, standardFreq) // return validated args
      }
    }
  }
}
