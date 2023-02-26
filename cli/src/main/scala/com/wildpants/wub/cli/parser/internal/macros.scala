package com.wildpants.wub.cli
package parser
package internal

import scala.quoted.*
import scala.deriving.Mirror

/* ------------------------ Macros for cmd info inspection ------------------------ */

private[parser] inline def inspectCmd[T]: CmdInfo = ${ inspectCmdInfo[T] }

private[internal] def inspectCmdInfo[T: Type](using Quotes): Expr[CmdInfo] =
  import quotes.reflect.*
  val caseSym = TypeRepr.of[T].typeSymbol
  val cmdName: Expr[String] = Expr { caseSym.name }
  val info = processDocstring(Expr(caseSym.docstring.getOrElse("")))
  val (cmdDescValue, argDescsValue) = info.valueOrAbort
  val (cmdDesc, argDescs) = Expr { cmdDescValue } -> Expr { argDescsValue }

  val argInfoList: Expr[List[ArgInfo]] =
    caseSym.primaryConstructor.paramSymss match
      case Nil => Expr.ofList { List.empty[Expr[ArgInfo]] }
      case head :: tails =>
        val names = Expr.ofList(head.map(argSym => Expr(argSym.name)))
        val argDefaults = getDefaultsImpl[T]
        val descs = '{ $names.map(n => $argDescs.find(_._1 == n).map(_._2).getOrElse("")) }
        '{
          ($names lazyZip $argDefaults lazyZip $descs).map((name, default, desc) =>
            default match {
              case None           => ArgInfo.Req(name, desc)
              case Some(defaultV) => ArgInfo.Opt(name, desc)
            }
          )
        }
  '{ CmdInfo($cmdName, $cmdDesc, $argInfoList) }
end inspectCmdInfo

/* ------------------------ Macros for product parsing ------------------------ */

private[parser] inline def getDefaults[T]: List[Option[Any]] = ${ getDefaultsImpl[T] }

private[internal] def getDefaultsImpl[T: Type](using Quotes): Expr[List[Option[Any]]] =
  import quotes.reflect.*
  val typeSymbol = TypeRepr.of[T].typeSymbol
  val arity = typeSymbol.primaryConstructor.paramSymss.head.size
  val optList = (1 to arity).toList.map(idx =>
    typeSymbol
      .companionClass
      .declaredMethod(s"$$lessinit$$greater$$default$$$idx")
      .headOption
      .map(Select(Ref(TypeRepr.of[T].typeSymbol.companionModule), _))
      .map(_.asExpr)
  )
  val exprList = optList.map {
    _ match
      case None            => '{ None }
      case Some(exprValue) => '{ Some($exprValue) }
  }
  Expr.ofList(exprList)
end getDefaultsImpl

private[parser] inline def getCaseFieldNames[T]: List[String] = ${ getCaseFieldNamesImpl[T] }

private[internal] def getCaseFieldNamesImpl[T: Type](using Quotes): Expr[List[String]] =
  import quotes.reflect.*
  val names = TypeRepr.of[T].typeSymbol.caseFields.map(_.name)
  Expr(names)

/* ------------------------ Macros for docstring parsing ------------------------ */

/** A macro that turns a raw docstring into usefule informations.
  *
  * @param raw
  *   the input raw docstring
  * @return
  *   expression of (desc, List((param_name, param_desc)))
  */
private[internal] def processDocstring(raw: Expr[String])(using Quotes) =
  val singleLineExpr = docStringToSingleLine(raw)
  val (desc, paramDescs) = splitSingleLine(singleLineExpr)
  '{ $desc -> $paramDescs }

private[internal] def docStringToSingleLine(raw: Expr[String])(using Quotes): Expr[String] =
  Expr {
    raw.valueOrAbort
      .stripPrefix("/**")
      .stripSuffix("*/")
      .linesIterator.toList
      .map(_.strip)
      .map(_.stripPrefix("*"))
      .map(_.strip)
      .reduceOption(_ + " " + _)
      .getOrElse("")
  }

private[internal] def splitSingleLine(singleLine: Expr[String])(using Quotes) =
  singleLine.valueOrAbort.split("@param").toList match
    case Nil         => Expr("") -> Expr(Nil)
    case head :: Nil => Expr(head.strip) -> Expr(Nil)
    case head :: tails =>
      Expr(head.strip) -> Expr.ofList(tails.map(tail => handleParamString(Expr(tail))))

private[internal] def handleParamString(p: Expr[String])(using Quotes): Expr[(String, String)] =
  val words = p.valueOrAbort.strip.split(" ").toList
  words match
    case head :: Nil   => Expr(head.strip -> "")
    case head :: tails => Expr(head.strip -> tails.reduce(_ + " " + _).strip)
    case Nil           => '{ compiletime.error("invalid @param clause") }
