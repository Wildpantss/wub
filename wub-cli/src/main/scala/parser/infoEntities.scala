package com.wildpants.wub.cli
package parser

import NameFormatUtils.*
import console.Style
import console.Style.*

/* ---------------- Private Type Aliases ---------------- */

private type Str = String
private type Cmds = List[CmdInfo]
private type Args = List[ArgInfo]

/* ---------------- Application Info Entities ---------------- */

case class AppInfo(name: Str, ver: Str, author: Str, desc: Str)
case class CmdInfo(name: Str, desc: Str, args: Args)
case class ArgInfo(name: Str, desc: Str, required: Boolean)

extension (self: CmdInfo)
  def usage: (String, String) = (camelOrPascalToKebab(self.name), self.desc)

extension (self: ArgInfo)
  def dispName: String = self.required match
    case true  => s"<${camelOrPascalToKebab(self.name)}>" <<< REQ_ARG_STYLE
    case false => s"[${camelOrPascalToKebab(self.name)}]" <<< OPT_ARG_COLOR
