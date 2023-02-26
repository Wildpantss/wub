package com.wildpants.wub.cli
package parser
package internal

enum TaskResult:
  case Success(cmd: String)
  case Failure(cmd: String, reason: String)
  case NoCmdArgError
  case CmdNotFoundError(cmd: String)
