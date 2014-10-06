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

import scala.collection.mutable
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.Reader

/**
 * Syntactical analyzer for the Decaf programming language
 *
 * @author Hawk Weisman
 *         Created by hawk on 9/28/14.
 */
class DecafSyntactical extends Parsers with DecafAST with DecafTokens {
  type Tokens <: DecafLexical
  override type Elem = DecafToken
  val lexical = new DecafLexical

  implicit def dkeyword (k: Keyword): Parser[Elem] = acceptIf(_.name == k.name)("`"+k+"' expected but " + _ + " found")
  implicit def ddelimiter (d: Delimiter): Parser[Elem] = acceptIf(_.name == d.name)("`"+d+"' expected but " + _ + " found")

  def parse(source: String): Option[Program] = {
    val scan = new lexical.DecafScanner(source).asInstanceOf[Reader[DecafToken]]
    phrase(program)(scan) match {
      case Success(result, _) => Some(result)
      case _ => None
    }
  }
  def program: Parser[Program] = positioned(
    decl.+ ^^{
      case decls => new Program(decls)
    })
  def decl: Parser[Decl] = (
    variableDecl
    | functionDecl
    //| classDecl
    //| interfaceDecl
    )
  def formals = repsep(variableDecl, Delimiter(","))
  def stmtBlock = Delimiter("{") ~ (variableDecl | (eexpr ~ Delimiter(";"))).* ~ Delimiter("}") ^^{
    case Delimiter("{") ~ stuff ~ Delimiter("}") => StmtBlock(
      List(),//stuff.filter(_.isInstanceOf[VarDecl]).asInstanceOf[List[VarDecl]],
      List()//stuff.filter(_.isInstanceOf[Stmt]).asInstanceOf[List[Stmt]])
      )
  }

  //def bigstmt = stmt | variableDecl

  def stmt: Parser[Stmt] =(
    eexpr.? ~ Delimiter(";") ^^{
      case e ~ d => if (e.isDefined) {e.asInstanceOf[Stmt]} else {EmptyExpr(d.getPos)}
    }
    //| ifStmt
    //| whileStmt
    //| forStmt
    //| breakStmt
    //| Keyword("return") ~ expr.? ~ Delimiter(";") ^^{ case k ~ thing ~ _ => ReturnStmt(k.getPos, thing)}
    )
  def ifStmt: Parser[Stmt] =
    Keyword("if") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ~ opt(Keyword("else") ~> stmt) ^^{
      case Keyword("if") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ testbody ~ elsebody => IfStmt(test,testbody,elsebody)
    }
  def whileStmt: Parser[Stmt] = Keyword("while") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ^^{
    case Keyword("while") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ loopbody => WhileStmt(test,loopbody)
  }
  def forStmt: Parser[Stmt] =
    Keyword("for") ~ Delimiter("(") ~ opt(expr) ~ Delimiter(";") ~ expr ~ Delimiter(";") ~ opt(expr) ~ Delimiter(")") ~ stmt ^^{
      case Keyword("for") ~ Delimiter("(") ~ i ~ Delimiter(";") ~ t ~ Delimiter(";") ~ s ~ Delimiter(")") ~ b =>
        ForStmt(i,t,s,b)
    }
  def breakStmt: Parser[Stmt] = Keyword("break") ~ Delimiter(";") ^^{case k ~ Delimiter(";") => BreakStmt(k.getPos)}
  def const: Parser[Expr] = (
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

  def expr: Parser[Expr] = (
      const
    )

  def eexpr: Parser[ASTNode] = (
    //lValue ~ Operator ("=") ~ expr ^^ {case left ~ _ ~ right => AssignExpr(left.getPos, left, right)}
    //lValue ~ Operator("=") ~ expr ^^{ case left ~ Operator("=") ~ right => AssignExpr(left.getPos, left, right)}
    expr
    | lValue
    //| call
    | Keyword("this") ^^{ case k => This(k.getPos) }
    | Delimiter("(") ~ eexpr ~ Delimiter(")") ^^{ case Delimiter("(") ~ e ~ Delimiter(")") => e }
    | Keyword("Print") ~ Delimiter("(") ~ repsep(eexpr, Delimiter(",")) ~ Delimiter(")") ^^ { case _ ~ _ ~ e ~ _ => PrintStmt(e)}
    //| expr ~ Operator("+") ~ expr ^^{
    //  case left ~ Operator("+") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "+"), right)
    //}
    //| expr ~ Operator("-") ~ expr ^^{
    //  case left ~ Operator("-") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "-"), right)
    //}
    /*| expr ~ Operator("*") ~ expr ^^{
      case left ~ Operator("*") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "*"), right)
    }
    | expr ~ Operator("%") ~ expr ^^{
      case left ~ Operator("%") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "%"), right)
    }
    | expr ~ Operator("/") ~ expr ^^{
      case left ~ Operator("/") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "/"), right)
    }
    | expr ~ Operator(">") ~ expr ^^{
      case left ~ Operator(">") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, ">"), right)
    }
    | expr ~ Operator(">=") ~ expr ^^{
      case left ~ Operator(">=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, ">="), right)
    }
    | expr ~ Operator("<=") ~ expr ^^{
      case left ~ Operator("<=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "<="), right)
    }
    | expr ~ Operator("<") ~ expr ^^{
      case left ~ Operator("<") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "<"), right)
    }
    | expr ~ Operator("==") ~ expr ^^{
      case left ~ Operator("==") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "=="), right)
    }
    | expr ~ Operator("!=") ~ expr ^^{
      case left ~ Operator("!=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "!="), right)
    }
    | expr ~ Operator("&&") ~ expr ^^{
      case left ~ Operator("&&") ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "&&"), right)
    }
    | expr ~ Operator("||") ~ expr ^^{
      case left ~ Operator("||") ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "||"), right)
    }
    | Operator("!") ~ expr ^^{
      case Operator("!") ~ right => new LogicalExpr(right.getPos, ASTOperator(right.getPos, "!"), right)
    }
    | Keyword("ReadInteger") ~ Delimiter("(") ~ Delimiter(")") ^^{
      case k ~ Delimiter("(") ~ Delimiter(")") => ReadIntegerExpr(k.getPos)
    }
    | Keyword("ReadLine") ~ Delimiter("(") ~ Delimiter(")") ^^{
      case k ~ Delimiter("(") ~ Delimiter(")") => ReadLineExpr(k.getPos)
    }
    | Keyword("new") ~ ident ^^{
      case Keyword("new") ~ i => NewExpr(i.getPos, NamedType(i))
    }
    | Keyword("NewArray") ~ Delimiter("(") ~ expr ~ Delimiter(",") ~ typ ~ Delimiter(")") ^^{
      case Keyword("NewArray") ~ Delimiter("(") ~ e ~ Delimiter(",") ~ t ~ Delimiter(")") => NewArrayExpr(e.getPos,e,t)
    }*/
    )
  def lValue: Parser[LValue] = (
    ident ^^{ case i => FieldAccess(i.getPos, None, i)}
    | expr.? ~ Delimiter(".") ~ ident ^^{case e ~ Delimiter(".") ~ i => FieldAccess(i.getPos, e, i)
    }
    //| expr ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^{ case first ~ Delimiter("[") ~ last ~ Delimiter("]") =>
    //  ArrayAccess(first.getPos, first, last)
    //  }
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
  def variableDecl: Parser[VarDecl] = typ ~ ident <~ Delimiter(";") ^^{
    case t ~ e  => VarDecl(e, t)
  }
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
  def returnType: Parser[Type] = (
    typ ^^{case t => t}
    | Keyword("void") ^^^ VoidType()
    )
  def className: Parser[NamedType] = ident ^^{ case i=> NamedType(i) }
  def ident: Parser[ASTIdentifier] = elem("ident", _.isInstanceOf[Identifier]) ^^{
    case i:Identifier => ASTIdentifier(Some(i.getPos), i.value)
  }
  def typ: Parser[Type] = (
    _typ
    | _typ ~ Delimiter("[]") ^^{ case t ~ dims => ArrayType(Some(dims.getPos), t)}
    )
  def _typ: Parser[Type] = (
    Keyword("int") ^^^ IntType()
    | Keyword("double") ^^^ DoubleType()
    | Keyword("bool") ^^^ BoolType()
    | Keyword("string") ^^^ StringType()
    | ident ^^{case i  => NamedType(i) }
    )
}
