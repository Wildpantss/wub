package com.wildpants.wub.cli
package parser

import cats.syntax.all.*

import scala.quoted.*
import scala.compiletime.*
import scala.deriving.Mirror.ProductOf as Prd

/* ------------------------------------------------------------------------- */
/* -------------------- Some low-level reflection utils -------------------- */
/* ------------------------------------------------------------------------- */

/** A group of compile-time ops for retrieving case-class meta-info.
  *
  * This is private for package `parser`, internal usage only.
  */
private[parser] object CaseClassInspect:

  /* ------------------------ Macro entrances ------------------------ */

  /** Get the arity of a case-class in compile-time.
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the type of target case-class
    * @return
    *   the arity of target type
    */
  inline def getArity[T]: Int =
    ${ getArityImpl[T] }

  /** Get the field names of a case-class as a [[List]] of [[String]] in order.
    *
    * The size of returned list shall be the same as arity of type 'T'
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the type of target case-class
    * @return
    *   the field name list
    */
  inline def getFieldNames[T]: List[String] =
    ${ getFieldNamesImpl[T] }

  /** Get the default pram values of a case-class as a [[List]] of [[Option]] in
    * order.
    *
    * The size of returned list shall be the same as arity of type 'T'
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the type of target case-class
    * @return
    *   the default value options
    */
  inline def getDefaults[T]: List[Option[?]] =
    ${ getDefaultsImpl[T] }

  /** Get the Read type-class instances for a case-class's params. (in order)
    *
    * If any of the [[Read]] instance cannot be summon, this function will give
    * a compile-time error.
    *
    * The size of returned list shall be the same as arity of type 'T'
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the type of target case-class
    * @return
    *   the [[List]] of [[Read]] instances
    */
  inline def getReadInstances[T]: List[Read[?]] =
    ${ getReadInstancesImpl[T] }

  /** Check whether a case-class's default param are all at trailing position.
    *
    * This function returns nothing, but will give a compile-time error if check
    * not passed.
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the type of target case-class
    */
  inline def checkDefaultsAsTrailing[T]: Unit =
    ${ checkDefaultsAsTrailingImpl[T] }

  /* ------------------------ Macro implementations ------------------------ */

  private def getArityImpl[T: Type](using Quotes): Expr[Int] =
    import quotes.reflect.*
    checkCaseClass[T]
    val typeSym = TypeRepr.of[T].typeSymbol
    Expr(typeSym.primaryConstructor.paramSymss.head.size)

  private def getFieldNamesImpl[T: Type](using Quotes): Expr[List[String]] =
    import quotes.reflect.*
    checkCaseClass[T]
    Expr(TypeRepr.of[T].typeSymbol.caseFields.map(_.name))

  private def getDefaultsImpl[T: Type](using Quotes): Expr[List[Option[?]]] =
    import quotes.reflect.*
    checkCaseClass[T]
    val exprOptList = getDefaultExprs[T]
    val exprList = exprOptList.map {
      _ match
        case None       => '{ None }
        case Some(expr) => '{ Some($expr) }
    }
    Expr.ofList(exprList)

  private def getReadInstancesImpl[T: Type](using Quotes): Expr[List[Read[?]]] =
    import quotes.reflect.*

    checkCaseClass[T]
    val fieldTypes = getFieldTypes[T]
    val readExprs = fieldTypes
      .map { case '[t] => getTypeName[t] -> Expr.summon[Read[t]] }
      .map {
        case (name, None) => report.errorAndAbort(s"Read[$name] not found")
        case (_, Some(r)) => r
      }
    Expr.ofList(readExprs)

  private def checkDefaultsAsTrailingImpl[T: Type](using Quotes): Expr[Unit] =
    import quotes.reflect.*
    checkCaseClass[T]
    val exprOptList = getDefaultExprs[T]
    val firstSomeIdx = exprOptList.indexWhere(_.isDefined)
    val errMsg = "params with defaults should be placed in tail of param list"
    val valid = firstSomeIdx match
      case -1 => true
      case i  => !exprOptList.slice(i, exprOptList.size).contains(None)
    valid match
      case true  => '{}
      case false => report.errorAndAbort(errMsg)

  /* ------------------------ Internal macros ------------------------ */

  private def getTypeName[T: Type](using Quotes): String =
    import quotes.reflect.*
    TypeRepr.of[T].typeSymbol.name

  private def checkCaseClass[T: Type](using Quotes): Unit =
    import quotes.reflect.*

    val typeSym = TypeRepr.of[T].typeSymbol
    val typeName = getTypeName[T]

    if typeSym.flags.is(Flags.Case) == false then
      report.errorAndAbort(s"type '$typeName' is not a case-class")

  private def getDefaultExprs[T: Type](using Quotes): List[Option[Expr[?]]] =
    import quotes.reflect.*
    val typeSym = TypeRepr.of[T].typeSymbol
    val companionSym = typeSym.companionClass
    val arity = getArityImpl[T].valueOrAbort
    val declPattern = "$lessinit$greater$default$%s"
    (1 to arity)
      .toList
      .map(idx => companionSym.declaredMethod(declPattern.format(idx)))
      .map(_.headOption)
      .map(_.map(x => Select(Ref(typeSym.companionModule), x)))
      .map(_.map(x => x.asExpr))

  private def getFieldTypes[T: Type](using Quotes): List[Type[?]] =
    import quotes.reflect.*
    val typeSym = TypeRepr.of[T].typeSymbol
    typeSym.caseFields
      .map(_.tree.symbol.tree)
      .map(_.asInstanceOf[ValDef])
      .map(_.tpt.tpe)
      .map(_.asType)

end CaseClassInspect

/** A group of ops that transform a [[Seq]] of [[String]] into case-class
  * instance.
  *
  * (These functions are considered to be safe, including all kinds of error
  * situation)
  *
  * This is private for package `parser`, internal usage only.
  */
private[parser] object CaseClassParsing:

  enum Result[T]:
    case Success(value: T)
    case ReadFailure(argName: String, input: String, errMsg: String)
    case ArgSizeFailure(actual: Int, supposed: (Int, Int))

  /** Parse the given arguments into a case-class instance.
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the target case-class type
    * @return
    *   a [[Result]] , as a [[Success]] that contains an instance of type 'T'
    *   when parsed successfully, otherwise as a [[ReadFailure]] or
    *   [[ArgSizeFailure]] that contains correspond cause info
    */
  inline def parseCaseClass[T](args: Seq[String])(using m: Prd[T]): Result[T] =
    CaseClassInspect.checkDefaultsAsTrailing[T]
    checkArgSize[T](args) match
      case Left(failure) => failure
      case _             => genInst[T](args)

  /* ------------------------ Internal utils ------------------------ */

  // simple type-alias for following functions
  private type checkArgRes[T] = Either[Result.ArgSizeFailure[T], Unit]

  private inline def checkArgSize[T](args: Seq[String]): checkArgRes[T] =
    import CaseClassInspect.*

    val defaultsCount = getDefaults[T].filter(_.isDefined).size
    val arity = getArity[T]
    val range: (Int, Int) = (arity - defaultsCount) -> arity

    args match
      case x if x.size < range._1 => Left(Result.ArgSizeFailure(x.size, range))
      case x if x.size > range._2 => Left(Result.ArgSizeFailure(x.size, range))
      case _                      => Right(())

  private inline def genInst[T](args: Seq[String])(using m: Prd[T]): Result[T] =
    import CaseClassInspect.*

    val reads = getReadInstances[T]
    val defaults = getDefaults[T]
    val fieldNames = getFieldNames[T]
    val argsPad = args.map(_.some).padTo(getArity[T], None).toList

    val argResults = (fieldNames lazyZip argsPad lazyZip reads lazyZip defaults)
      .map {
        case (n, None, r, Some(d)) => Right(d)
        case (n, Some(a), r, _) => r.read(a).swap.map((i, e) => (n, i, e)).swap
        case (_, None, _, None) => throw IllegalStateException()
      }

    argResults.sequence match
      case Left((name, input, errM)) => Result.ReadFailure(name, input, errM)
      case Right(r) => Result.Success(m.fromProduct(toProd(r)).asInstanceOf[T])

  private def toProd(seq: Seq[?]): Product =
    Tuple.fromArray(seq.toArray)

end CaseClassParsing
