package com.wildpants.wub.cli
package parser


import cats.syntax.all.*
import scala.quoted.*
import scala.deriving.Mirror.ProductOf as Prd
import scala.compiletime.*


/** A group of common-used macros. (package-private inside `parser`)
  */
private object Macros:

  /** Entrances of the macro inside [[Macros]].
    *
    * (Interfaces for outer scope)
    */
  object Entr:

    /** Inspect the static type name.
      *
      * @note
      *   static types only, unable to get a name of generic type.
      *
      * @tparam T
      *   the type to inspect name
      * @return
      *   A [[String]] as the name of target type
      */
    inline def inspectTypeName[T]: String =
      ${ Impl.inspectTypeName[T] }


    /** Check is the target type a case-class.
      *
      * @note
      *   the macro expansion will be terminated and an error will be report is
      *   check not passed.
      *
      * @tparam T
      *   the type to check
      */
    inline def checkIsCaseClass[T]: Unit =
      ${ Impl.checkIsCaseClass[T] }


    /** Get the arity of a case-class in compile-time.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect arity
      * @return
      *   A [[Int]] that represents the arity
      */
    inline def inspectArity[T]: Int =
      ${ Impl.inspectArity[T] }


    /** Get the default values of target case-class's fields in the definition
      * order. (Fill with [[None]] if the field has no default value)
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect default fields
      * @return
      *   A [[List]] of [[Option]] that contains the default value of [[None]]
      */
    inline def inspectDefaults[T]: List[Option[?]] =
      ${ Impl.inspectDefaults[T] }


    /** Get the field names of target case-class. (in definition order)
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect field names
      * @return
      *   A [[List]] of [[String]] that contains the field names of [[None]]
      */
    inline def inspectFieldNames[T]: List[String] =
      ${ Impl.inspectFieldNames[T] }


    /** Summon [[Read]] instances for all fields of the target case-class in the
      * order of definition of fields.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class into summon Read instances
      * @return
      *   A [[List]] of [[Read]]
      */
    inline def summonReads[T]: List[Read[?]] =
      ${ Impl.summonReads[T] }


    /** Check whether the target case-class's default params are all at trailing
      * position.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to check
      */
    inline def checkIsDefaultsTrailing[T]: Unit =
      ${ Impl.checkIsDefaultsTrailing[T] }

  end Entr


  /** Implementations of the macro inside [[Macros]].
    *
    * (Interfaces for inner scope)
    */
  object Impl:

    /** Inspect the static type name.
      *
      * @note
      *   static types only, unable to get a name of generic type.
      *
      * @tparam T
      *   the type to inspect name
      * @return
      *   this macro expands into a [[String]] as the name of target type
      */
    def inspectTypeName[T: Type](using Quotes): Expr[String] =
      import quotes.reflect.*
      Expr(TypeRepr.of[T].typeSymbol.name)


    /** Check is the target type a case-class.
      *
      * @note
      *   the macro expansion will be terminated and an error will be report is
      *   check not passed.
      *
      * @tparam T
      *   the type to check
      * @return
      *   this macro expands into [[Unit]], receiver is not required
      */
    def checkIsCaseClass[T: Type](using Quotes): Expr[Unit] =
      import quotes.reflect.*

      val tSym = TypeRepr.of[T].typeSymbol
      val tName = tSym.name

      tSym.flags.is(Flags.Case) match
        case false => report.errorAndAbort(s"type '$tName' is not a case-class")
        case true  => '{}


    /** Get the arity of a case-class in compile-time.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect arity
      * @return
      *   this macro expands into a [[Int]] that represents the arity
      */
    def inspectArity[T: Type](using Quotes): Expr[Int] =
      import quotes.reflect.*
      checkIsCaseClass[T]

      val typeSym = TypeRepr.of[T].typeSymbol
      Expr(typeSym.primaryConstructor.paramSymss.head.size)


    /** Get the default values of target case-class's fields in the definition
      * order. (Fill with [[None]] if the field has no default value)
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect default fields
      * @return
      *   this macro expands into a [[List]] of [[Option]] that contains the
      *   default value of [[None]]
      */
    def inspectDefaults[T: Type](using Quotes): Expr[List[Option[?]]] =
      import quotes.reflect.*
      checkIsCaseClass[T]

      val exprList = getDefaultExprs[T].map {
        _ match
          case None       => '{ None }
          case Some(expr) => '{ Some($expr) }
      }
      Expr.ofList(exprList)


    /** Get the field names of target case-class. (in definition order)
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to inspect field names
      * @return
      *   this macro expands into a [[List]] of [[String]] that contains the
      *   field names of [[None]]
      */
    def inspectFieldNames[T: Type](using Quotes): Expr[List[String]] =
      import quotes.reflect.*
      checkIsCaseClass[T]
      Expr(TypeRepr.of[T].typeSymbol.caseFields.map(_.name))


    /** Summon [[Read]] instances for all fields of the target case-class in the
      * order of definition of fields.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class into summon Read instances
      * @return
      *   this macro expands to a [[List]] of [[Read]]
      */
    def summonReads[T: Type](using Quotes): Expr[List[Read[?]]] =
      import quotes.reflect.*
      checkIsCaseClass[T]

      val fieldTypes = TypeRepr.of[T].typeSymbol.caseFields
        .map(_.tree.symbol.tree)
        .map(_.asInstanceOf[ValDef])
        .map(_.tpt.tpe)
        .map(_.asType)
      val readExprs = fieldTypes
        .map { case '[t] => inspectTypeName[t] -> Expr.summon[Read[t]] }
        .map {
          case (name, None) => report.errorAndAbort(s"Read[$name] not found")
          case (_, Some(r)) => r
        }
      Expr.ofList(readExprs)


    /** Check whether the target case-class's default params are all at trailing
      * position.
      *
      * @note
      *   if the type 'T' is not a case-class, the macro expansion will be
      *   terminated and report an error message.
      *
      * @tparam T
      *   the type of case-class to check
      * @return
      *   this macro expands into [[Unit]]
      */
    def checkIsDefaultsTrailing[T: Type](using Quotes): Expr[Unit] =
      import quotes.reflect.*
      checkIsCaseClass[T]

      val exprOptList = getDefaultExprs[T]
      val firstSomeIdx = exprOptList.indexWhere(_.isDefined)
      val errMsg = "params with defaults should be placed in tail of param list"
      val valid = firstSomeIdx match
        case -1 => true
        case i  => !exprOptList.slice(i, exprOptList.size).contains(None)
      valid match
        case true  => '{}
        case false => report.errorAndAbort(errMsg)


    /* ------------------------ Private Functions ------------------------ */


    // Get default expr list of target type, Some on default val exists
    private def getDefaultExprs[T: Type](using Quotes): List[Option[Expr[?]]] =
      import quotes.reflect.*
      val typeSym = TypeRepr.of[T].typeSymbol
      val companionSym = typeSym.companionClass
      val arity = inspectArity[T].valueOrAbort
      val declPattern = "$lessinit$greater$default$%s"
      (1 to arity)
        .toList
        .map(idx => companionSym.declaredMethod(declPattern.format(idx)))
        .map(_.headOption)
        .map(_.map(x => Select(Ref(typeSym.companionModule), x)))
        .map(_.map(x => x.asExpr))

  end Impl

end Macros


/** A group of functions for case-class parsing. (package-private inside
  * `parser`)
  */
private object CCParsing:

  /** Parse the given arguments into a case-class instance.
    *
    * @note
    *   this function will give a compile-time error if the input type 'T' is
    *   not a case-class
    *
    * @tparam T
    *   the target case-class type
    * @return
    *   an [[Either]] , as a [[Right]] that contains an instance of type 'T'
    *   when parsed successfully, otherwise as a [[Left]] that contains error
    *   message
    */
  inline def parseCaseClass[T](args: Seq[String])(using m: Prd[T]) =
    Macros.Entr.checkIsDefaultsTrailing[T]
    checkArgSize[T](args).flatMap(_ => genInst[T](args))


  /* ------------------------ Private Functions ------------------------ */


  private inline def checkArgSize[T](args: Seq[String]): Either[String, Unit] =
    import Macros.Entr.*

    val defaultsCount = inspectDefaults[T].filter(_.isDefined).size
    val arity = inspectArity[T]
    val range: (Int, Int) = (arity - defaultsCount) -> arity

    (args.size, range._1, range._2) match
      case (s, l, r) if l to r contains s => Right(())
      case (s, 0, 0)           => Left(s"no argument required, received $s")
      case (s, 1, 1)           => Left(s"1 argument required, received $s")
      case (s, l, r) if l == r => Left(s"$l arguments required, received $s")
      case (s, l, r) => Left(s"$l to $r arguments required, received $s")


  private inline def genInst[T](args: Seq[String])(using m: Prd[T]) =
    import Macros.Entr.*

    val reads = summonReads[T]
    val defaults = inspectDefaults[T]
    val fieldNames = inspectFieldNames[T]
    val argsPad = args.map(_.some).padTo(inspectArity[T], None).toList
    val readErrPattern = "invalid given value '%s' for argument '%s': %s"

    val argResults = (fieldNames lazyZip argsPad lazyZip reads lazyZip defaults)
      .map {
        case (n, None, r, Some(d)) => Right(d)
        case (n, Some(a), r, _) => r.read(a).swap.map((i, e) => (n, i, e)).swap
        case (_, None, _, None) => throw IllegalStateException()
      }

    argResults.sequence match
      case Right(r)        => Right(m.fromProduct(toProd(r)).asInstanceOf[T])
      case Left((n, i, e)) => Left(readErrPattern.format(i, n, e))


  private def toProd(seq: Seq[?]): Product = Tuple.fromArray(seq.toArray)

end CCParsing
