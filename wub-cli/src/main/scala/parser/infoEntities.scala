package com.wildpants.wub.cli
package parser

import NameFormatUtils.*
import console.Style
import console.Style.*

/* ---------------- Application Info Entities ---------------- */

private case class AppInfo(name: String, ver: String, author: String, desc: String)
private case class CmdInfo(name: String, desc: String, args: List[ArgInfo])
private case class ArgInfo(name: String, desc: String, required: Boolean)

extension (self: CmdInfo)
  def usage: (String, String) = (camelOrPascalToKebab(self.name), self.desc)

extension (self: ArgInfo)
  def dispName: String = self.required match
    case true  => s"<${camelOrPascalToKebab(self.name)}>" <<< REQ_ARG_STYLE
    case false => s"[${camelOrPascalToKebab(self.name)}]" <<< OPT_ARG_COLOR
