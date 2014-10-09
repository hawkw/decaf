/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */


package decaf.frontend

import scala.util.parsing.combinator.{PackratParsers, Parsers}
import scala.util.parsing.input.Reader

/**
 * Syntactical analyzer for the Decaf programming language.
 *
 * The [[DecafSyntactical#parse parse()]] method takes a String containing the source code of a Decaf program and
 * outputs an [[scala.Option]] on a [[DecafAST]] representing the abstract syntax tree generated by that source code.
 * The top-level node in any [[DecafAST]] is [[DecafAST.Program Program]], with all other statements existing as
 * children of that node.
 *
 * ==Note on PackratParsers==
 *
 * This class makes heavy use of the [[scala.util.parsing.combinator.PackratParsers PackratParsers]] class.
 * These parsers function similarly to the Parser class from [[scala.util.parsing.combinator.Parsers Parsers]], but
 * with the addition of a memoization facility. This allows us to implement back-tracking, recursive-descent parsers,
 * but with the added guarantees of unlimited lookahead and linear parse time. PackratParsers also allows us to accept
 * left-recursive grammars without infinite recursion.
 *
 * This is very, very good, since the Decaf grammar specification is, to use a technical term, "super left-recursive".
 * While it is possible to transform a left-recursive grammar into a non-less-recursive grammar, we (the Decaf language
 * implementors) are lazy and did not wish to do this. Also, it means we'd have to significantly munge the abstract
 * syntax tree, which might result in us generating ASTs that don't match those generated by Prof. Jumadinova's
 * C implementation, which would invalidate our primary correctness-testing mechanism.
 *
 * If you're interested in packrat parsing, this technique was first described in a very interesting paper which can be
 * found [[http://scala-programming-language.1934581.n4.nabble.com/attachment/1956909/0/packrat_parsers.pdf here]].
 *
 * ==Interpretation of the Decaf Grammar==
 *
 * There are some cases where the Decaf grammar specification provided by Prof. Jumadinova was ambiguous or did not
 * fully specify how some language construct functions. In these cases, we as the language implementers have made our
 * own decisions on the correct course of action. These interpretation choices are documented in source code comments
 * in this file.
 *
 * @author Hawk Weisman
 * @author Max Clive
 *
 * Created by hawk on 9/28/14.
 */
class DecafSyntactical extends Parsers with DecafAST with DecafTokens with PackratParsers {
  type Tokens <: DecafLexical
  override type Elem = DecafToken
  val lexical = new DecafLexical
  type P[+T] = PackratParser[T]

  implicit def dkeyword(k: Keyword): Parser[Elem] = acceptIf(_.name == k.name)("`" + k.name + "' expected but " + _.name + " found")
  implicit def ddelimiter(d: Delimiter): Parser[Elem] = acceptIf(_.name == d.name)("`" + d.name + "' expected but " + _.name + " found")
  implicit def doperator(o: Operator): Parser[Elem] = acceptIf(_.name == o.name)("`" + o.name + "' expected but " + _.name + " found")

  def parse(source: String): Program = {
    val scan = new lexical.DecafScanner(source).asInstanceOf[Reader[DecafToken]]

    /**
     * Some debuggery using Print.
     */
    var dscan = scan;
    while(!dscan.atEnd) {
      System.out.println(dscan.first)
      dscan = dscan.rest
    }


    phrase(program)(scan) match {
      case Success(result, _) => result
      case x: Failure => throw new RuntimeException(x.toString())
      case x: Error => throw new RuntimeException(x.toString())
    }
  }

  lazy val program: PackratParser[Program] = (
    decl.+ ^^ {
      case decls => new Program(decls)
    })

  lazy val decl: PackratParser[Decl] = (
    ( variableDecl <~ Delimiter(";") )
    | functionDecl
    | classDecl
    | interfaceDecl
    )

  lazy val stmtBlock = Delimiter("{") ~ ((variableDecl <~ Delimiter(";"))| stmt).* ~ Delimiter("}") ^^ {
    case Delimiter("{") ~ stuff ~ Delimiter("}") => StmtBlock(
      stuff.filter(_.isInstanceOf[VarDecl]).asInstanceOf[List[VarDecl]],
      stuff.filter(_.isInstanceOf[Stmt]).asInstanceOf[List[Stmt]])
  }

  lazy val returnType: PackratParser[Type] = (
    Keyword("void") ^^^ VoidType()
    | typ
    )

  lazy val formals: PackratParser[List[VarDecl]] = (
    Delimiter("(") ~ Delimiter(")") ^^^ List()
    | Delimiter("(") ~ repsep(variableDecl, Delimiter(",")) ~ Delimiter(")") ^^ {case _ ~ stuff ~ _ => stuff}
    )

  lazy val fnargs: PackratParser[List[Expr]] = Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
    case _ ~ stuff ~ _ => stuff
  }

  lazy val functionDecl: PackratParser[Decl] = returnType ~ ident ~ formals ~ stmtBlock ^^{
    case rt ~ name ~ fs ~ body => new FnDecl(name, rt, fs, body)
  }

  lazy val stmt: PackratParser[Stmt] =(
    expr.? ~ Delimiter(";") ^^{
      case e ~ d => if (e.isDefined) {e.get.asInstanceOf[Stmt]} else {EmptyExpr()}
    }
      | Keyword("Print") ~ Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ~ Delimiter(";") ^^ { case _ ~ _ ~ e ~ _ ~ _ => PrintStmt(e)}
      | ifStmt
      | whileStmt
      | forStmt
      | breakStmt
      | stmtBlock
      | Keyword("return") ~ expr.? <~ Delimiter(";") ^^{
      case k ~ None => ReturnStmt(k.getPos, Some(EmptyExpr()))
      case k ~ Some(thing) => ReturnStmt(k.getPos, Some(thing))

    }
   )
  lazy val breakStmt: PackratParser[Stmt] = Keyword("break") ~ Delimiter(";") ^^{case k ~ Delimiter(";") => BreakStmt(k.getPos)}
  lazy val ifStmt: PackratParser[Stmt] =
    Keyword("if") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ~ (opt(Keyword("else") ~> stmt)) ^^{
      case Keyword("if") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ testbody ~ elsebody => IfStmt(test,testbody,elsebody)
    }
  lazy val whileStmt: PackratParser[Stmt] = Keyword("while") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ^^{
    case Keyword("while") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ loopbody => WhileStmt(test,loopbody)
  }
  lazy val forStmt: PackratParser[Stmt] =
    Keyword("for") ~ Delimiter("(") ~ opt(expr) ~ Delimiter(";") ~ expr ~ Delimiter(";") ~ opt(expr) ~ Delimiter(")") ~ stmt ^^{
      case k ~ _ ~ None ~ _ ~ t ~ _ ~ Some(step) ~ _ ~ b => ForStmt(Some(EmptyExpr()),t,Some(step),b)
      case k ~ _ ~ Some(init) ~ _ ~ t ~ _ ~ Some(step) ~ _ ~ b =>ForStmt(Some(init),t,Some(step),b)
      case k ~ _ ~ None ~ _ ~ t ~ _ ~ None ~ _ ~ b => ForStmt(Some(EmptyExpr()),t,Some(EmptyExpr()),b)
      case k ~ _ ~ Some(init) ~ _ ~ t ~ _ ~ None ~ _ ~ b =>ForStmt(Some(init),t,Some(EmptyExpr()),b)
    }


  lazy val expr: P[Expr] = ( indirect ||| logical )

  lazy val indirect: P[Expr] = (
      assign
      ||| arrayAccess
      ||| fieldAccess
      ||| call
    )

  lazy val call: P[Expr] = (
    (rexpr ||| fieldAccess ||| indirect) ~ Delimiter(".") ~ ident ~ fnargs ^^ { case base ~ _ ~ field ~ args => new Call(base.getPos, base, field, args) }
    ||| ident ~ fnargs ^^ { case field ~ args => new Call(field.getPos, field, args) }

      ||| Keyword("ReadLine") ~ Delimiter("(") ~ Delimiter(")") ^^{
        case k ~ Delimiter("(") ~ Delimiter(")") => ReadLineExpr(k.getPos)
      }
      ||| Keyword("ReadInteger") ~ Delimiter("(") ~ Delimiter(")") ^^{
        case k ~ Delimiter("(") ~ Delimiter(")") => ReadIntegerExpr(k.getPos)
      }
    )

  lazy val arrayAccess: P[Expr] = (
      (fieldAccess ||| indirect) ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^ {case first ~ _ ~ last ~ _ => ArrayAccess(first.getPos, first, last)}
    )

  lazy val assign: P[Expr] = (
      assignable ~ Operator("=") ~ expr ^^{
        case left ~ _ ~ right => AssignExpr(left.getPos, left, right)
      }
    )
  lazy val assignable: P[Expr] =  arrayAccess ||| fieldAccess

  lazy val fieldAccess: P[Expr] = (
    ident ^^ { case i => FieldAccess(i.getPos, None, i)}
    ||| ( arrayAccess
      ||| fieldAccess
      ||| call) ~ Delimiter(".") ~ ident ^^ { case base ~ _ ~ i => FieldAccess(base.getPos, Some(base), i) }
    ||| exprThis ~ Delimiter(".") ~ ident ^^ { case base ~ _ ~ i => FieldAccess(base.getPos, Some(base), i) }
    )

  lazy val exprThis: P[Expr] = Keyword("this") ^^ {
      case k => This(k.getPos)
    }

  lazy val exprNew: P[Expr] = (
      Keyword("new") ~ ident ^^
          { case Keyword("new") ~ i => NewExpr(i.getPos, NamedType(i)) }
      | Keyword("NewArray") ~ Delimiter("(") ~ expr ~ Delimiter(",") ~ typ ~ Delimiter(")") ^^
          { case Keyword("NewArray") ~ Delimiter("(") ~ e ~ Delimiter(",") ~ t ~ Delimiter(")") => NewArrayExpr(e.getPos,e,t) }
    )
/*
  lazy val tlmath: P[Expr] = (
      logical ||| relational ||| term ||| factor ||| unary ||| unaryRHS
    )
*/
  lazy val logical: P[Expr] = (
    (relational ||| logical ||| rexpr) ~ ( Operator("||") | Operator("&&"))  ~ relational ^^
        { case left ~ op ~ right => new LogicalExpr(left.getPos, left, ASTOperator(op.getPos, op.chars), right) }
    ||| relational
    )


  lazy val relational: P[Expr] = (
    (term ||| relational ||| rexpr) ~ ((
      Operator(">=") | Operator("==") | Operator("!=") | Operator("<=") | Operator(">") | Operator("<")
      ) ^^ {case o => ASTOperator(o.getPos, o.chars)}) ~ term ^^{
      case left ~ op ~ right => op match {
        case ASTOperator(_,"==") | ASTOperator(_,"!=") => EqualityExpr(left.getPos, left, op, right)
        case _ => RelationalExpr(left.getPos, left, op, right)
      }
    }
    ||| term
    )

  lazy val term: P[Expr] = (
    (factor ||| term ||| rexpr) ~ (Operator("-") | Operator("+") ) ~  factor ^^
          { case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, op.chars), right) }
    ||| factor
    )

  lazy val factor: P[Expr] =(
    ( (unary ||| factor ||| rexpr) ~ Operator("%") ~ unary ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "%"), right)
    }
    | (unary ||| factor ||| rexpr) ~ Operator("*") ~ unary ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "*"), right)
    }
    | (unary ||| factor ||| rexpr) ~ Operator("/") ~  unary ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "/"), right)
    } )
    ||| unary
    )


  lazy val unary: P[Expr] = (
    ( Operator("!") ~ unaryRHS ^^{ case op ~ e => LogicalExpr(op.getPos, None, ASTOperator(op.getPos, "!"), e)}
    | Operator("-") ~ unaryRHS ^^{
      case op ~ e => ArithmeticExpr(op.getPos, ASTIntConstant(op.getPos, 0), ASTOperator(op.getPos, "-"), e)} )
    /*
     * The correct handling for the unary minus operator is not documented in the reference implementation
     * as there are no sample programs with correct output for the unary minus expression. Therefore, we've
     * decided to handle it as an arithmetic expression, considering that a unary minus operation is basically the same
     * as subtracting the number from zero. This will make semantic analysis easier as we don't need to special-case
     * unary minus and we can just handle it as any other subtraction operation.
     */
    ||| unaryRHS
    )

  lazy val unaryRHS: P[Expr] = (
    rexpr ||| (const
      |  exprThis
      | exprNew
      | indirect)
    )

  lazy val rexpr: P[Expr] = ( Delimiter("(") ~> expr <~ Delimiter(")") )
  lazy val const: PackratParser[Expr] = (
    elem("intConst", _.isInstanceOf[IntConstant])
      | elem("doubleConst", _.isInstanceOf[DoubleConstant])
      | elem("BoolConst", _.isInstanceOf[BoolConstant])
      | elem("StringConst", _.isInstanceOf[StringConstant])
      | Keyword("null")
    )^^{
    case i: IntConstant => ASTIntConstant(i.getPos, i.value)
    case d: DoubleConstant => ASTDoubleConstant(d.getPos, d.value)
    case b: BoolConstant => ASTBoolConstant(b.getPos, b.value)
    case s: StringConstant => ASTStringConstant(s.getPos, s.value)
    case n: Keyword => ASTNullConstant(n.getPos)
  }

  lazy val variableDecl: PackratParser[VarDecl] = typ ~ ident ^^ {
    case t ~ e => VarDecl(e, t)
  }

  lazy val ident: PackratParser[ASTIdentifier] = (
      _ident ^^{ case i: Identifier => ASTIdentifier(Some(i.getPos), i.value)}
    )

  lazy val _ident: PackratParser[Elem] = acceptIf(_.isInstanceOf[Identifier])("Identifier token expected but " + _ + " found")

  lazy val atyp: PackratParser[Type] = (
      /* I don't know if Decaf supports multidimensional arrays,
         but if you leave in this typ here at the top, we can do that.
         Otherwise, take it out.
       */
      typ
      | Keyword("int") ^^^ IntType()
      | Keyword("double") ^^^ DoubleType()
      | Keyword("bool") ^^^ BoolType()
      | Keyword("string") ^^^ StringType()
      | ident ^^ { case i => NamedType(i)}
    )

  lazy val typ: PackratParser[Type] = (
      atyp ~ Delimiter("[]") ^^ { case t ~ dims => ArrayType(Some(dims.getPos), t)}
      | atyp
    )

  lazy val classDecl: P[Decl] =
    Keyword("class") ~> ident ~ opt(extendPart) ~ opt(implementsPart) ~ Delimiter("{") ~ rep(field) ~ Delimiter("}") ^^{
      case name ~ ext ~ imp ~ Delimiter("{") ~ fields ~ Delimiter("}") => ClassDecl(name, ext, imp.getOrElse(Nil), fields)
    }
  lazy val extendPart: P[NamedType] = Keyword("extends") ~> className
  lazy val implementsPart: P[List[NamedType]] = Keyword("implements") ~> repsep(className, Delimiter(","))
  lazy val field: P[Decl] = ( variableDecl <~ Delimiter(";") ) | functionDecl
  lazy val className: P[NamedType] = ident ^^{ case i=> NamedType(i) }

  lazy val interfaceDecl: P[Decl] =
    Keyword("interface") ~> ident ~ Delimiter("{") ~ prototype.* ~ Delimiter("}") ^^{
      case name ~ Delimiter("{") ~ members ~ Delimiter("}") => InterfaceDecl(name, members)
    }
  lazy val prototype: P[Decl] = returnType ~ ident ~ formals <~ Delimiter(";") ^^{
      case rt ~ name ~ args => new FnDecl(name, rt, args)
    }
} /*


  def eexpr: Parser[Expr] = (
      const
    )

  def expr: Parser[Expr] = (

    )

  def call: Parser[Call] = (
    ident ~  Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case field ~ Delimiter("(") ~ args ~ Delimiter(")")  => new Call(field.getPos, field, args)
    }
    | expr ~ Delimiter(".") ~ ident ~ Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case base ~ Delimiter(".") ~ field ~ Delimiter("(") ~ args ~ Delimiter(")")  =>
        new Call(base.getPos, base, field, args)
    }
    )
  def functionDecl: Parser[Decl] = returnType ~ ident ~ Delimiter("(") ~ formals.? ~ Delimiter(")") ~ stmtBlock ^^{
      case rt ~ name ~ _ ~ fs ~ _ ~ body => FnDecl( name, rt, fs.getOrElse(Nil), body)
    }
  def classDecl: Parser[Decl] =
    Keyword("class") ~> ident ~ extendPart.? ~ implementsPart ~ Delimiter("{") ~ rep(field) ~ Delimiter("}") ^^{
      case name ~ ext  ~ imp ~ Delimiter("{") ~ fields ~ Delimiter("}") => ClassDecl(name, ext, imp, fields)
    }
  def extendPart: Parser[NamedType] = Keyword("extends") ~> className
  def implementsPart: Parser[List[NamedType]] = Keyword("implements") ~> repsep(className, Delimiter(","))
  def field: Parser[Decl] = (variableDecl | functionDecl)

  def className: Parser[NamedType] = ident ^^{ case i=> NamedType(i) }

  */
