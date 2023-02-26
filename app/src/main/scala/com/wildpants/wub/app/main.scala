package com.wildpants.wub
package app

import cli.parser.*

@main
def main(args: String*): Unit =

  import CliApp.*

  /** aaa
    *
    * @param name
    *   nnnnnnn
    * @param score
    *   sssssssss
    */
  case class Wub1(name: String, score: Int = 5)

  /** bbb
    * @param name
    *   aaaaaaaaa
    */
  case class Wub2(name: String)

  val app = CliApp(
    "wub",
    "0.0.1",
    "wildpants",
    "desc for wub app",
    "wub1" -> task[Wub1](wub1 => Right(println(wub1))),
    "wub2" -> task[Wub2](wub2 => Right(println(wub2)))
  )

  app.launch(args)

end main
