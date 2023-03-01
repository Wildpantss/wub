package com.wildpants.wub
package app
package freq

import cli.parser.task
import cli.console.ConsoleOps.*

val freqTask = task[Freq] {
  freq => Right(printLine(Note(freq.noteName, freq.standardFreq).displayFormat()))
}
