package com.wildpants.wub
package app
package freq

import cli.console.ConsoleOps.*
import cli.parser.task

val freqTask = task[Freq] {
  freq =>
    Right(printLine(Note(freq.noteName, freq.standardFreq).displayFormat()))
}
