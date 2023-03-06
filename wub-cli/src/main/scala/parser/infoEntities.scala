package com.wildpants.wub.cli
package parser

/* ---------------- Private Type Aliases ---------------- */

private type Str = String
private type Cmds = List[CmdInfo]
private type Args = List[ArgInfo]

/* ---------------- Application Info Entities ---------------- */

case class AppInfo(name: Str, ver: Str, author: Str, desc: Str, cmds: Cmds)
case class CmdInfo(name: Str, desc: Str, args: Args)
case class ArgInfo(name: Str, desc: Str, required: Boolean)
