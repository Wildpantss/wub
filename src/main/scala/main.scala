package com.wildpants.wub

import parser.TaskLauncher.Builder
import parser.{Description, TaskLauncher}
import parser.Description.*
import tasks.*

@main
def main(args: String*): Unit = TaskLauncher {
  val appName = "Wub"
  val appVersion = "0.0.1"
  val description = Description("Wub is a CLI util-box for music production by Wildpants ^_^")
  Builder() <<< (appName, appVersion) <<< description <<< FreqTask()
}.launch(args)