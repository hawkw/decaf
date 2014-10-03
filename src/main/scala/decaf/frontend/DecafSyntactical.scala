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

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.Reader

/**
 * Syntactical analyzer for the Decaf programming language
 *
 * @author Hawk Weisman
 *         Created by hawk on 9/28/14.
 */
class DecafSyntactical extends Parsers with DecafAST with DecafTokens {
  type Tokens <: DecafToken
  override type Elem = DecafToken

  val lexical = new DecafLexical
  def parse(source: String): Program = phrase(program)(new lexical.Scanner(source).asInstanceOf[Input]).get
  def program: Parser[Program] = rep(decl) ^^{case decls => new Program(decls.asInstanceOf[List[Decl]])}
  def decl = (
    variableDecl
    | functionDecl
    //| classDecl
    //| interfaceDecl
    )
  def functionDecl = (
    typ ~ ident ~ Delimiter("(") ~ formals ~ Delimiter(")") ~ stmtBlock
    | Keyword("void") ~ Delimiter("(") ~ formals ~ Delimiter(")") ~ stmtBlock
    )
  def formals = repsep(typ ~ ident, Delimiter(",")).?
  def stmtBlock = Delimiter("{") ~ (variableDecl | stmt).* ~ Delimiter("}") ^^{
    case Delimiter("{") ~ stuff ~ Delimiter("}") => StmtBlock(
      stuff.filter(_.isInstanceOf[VarDecl]).asInstanceOf[List[VarDecl]],
      stuff.filter(_.isInstanceOf[Stmt]).asInstanceOf[List[Stmt]])
  }
  def stmt =( expr.? ~ Delimiter(";")
    //| ifStmt // TODO: Implement
    //| whileStmt // TODO: Implement
    //| forStmt // TODO: Implement
    //| breakStmt // TODO: Implement
    | Keyword("return") ~ expr.? ~ Delimiter(";") ^^{ case k ~ thing ~ _ => ReturnStmt(k.getPos, thing)}
    )
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
    lValue ~ Operator("=") ~ expr ^^{ case left ~ Operator("=") ~ right => AssignExpr(left.getPos, left, right)}
    | const
    | lValue
    | Keyword("this") ^^{ case k => This(k.getPos) }
    | call
    | Delimiter("(") ~ expr ~ Delimiter(")") ^^{ case Delimiter("(") ~ e ~ Delimiter(")") => e }
    | expr ~ Operator("+") ~ expr ^^{ case left ~ Operator("+") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "+"), right) }
    | expr ~ Operator("-") ~ expr ^^{ case left ~ Operator("-") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "-"), right) }
    | expr ~ Operator("*") ~ expr ^^{ case left ~ Operator("*") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "*"), right) }
    | expr ~ Operator("%") ~ expr ^^{ case left ~ Operator("%") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "%"), right) }
    | expr ~ Operator("/") ~ expr ^^{ case left ~ Operator("/") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "/"), right) }
    | expr ~ Operator(">") ~ expr ^^{ case left ~ Operator(">") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, ">"), right) }
    | expr ~ Operator(">=") ~ expr ^^{ case left ~ Operator(">=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, ">="), right) }
    | expr ~ Operator("<=") ~ expr ^^{ case left ~ Operator("<=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "<="), right) }
    | expr ~ Operator("<") ~ expr ^^{ case left ~ Operator("<") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "<"), right) }
    | expr ~ Operator("==") ~ expr ^^{ case left ~ Operator("==") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "=="), right) }
    | expr ~ Operator("!=") ~ expr ^^{ case left ~ Operator("!=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "!="), right) }
    | expr ~ Operator("&&") ~ expr ^^{ case left ~ Operator("&&") ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "&&"), right) }
    | expr ~ Operator("||") ~ expr ^^{ case left ~ Operator("||") ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "||"), right) }
    | Operator("!") ~ expr ^^{ case Operator("!") ~ right => new LogicalExpr(right.getPos, ASTOperator(right.getPos, "!"), right) }
    | Keyword("ReadInteger") ~ Delimiter("(") ~ Delimiter(")") ^^{ case k ~ Delimiter("(") ~ Delimiter(")") => ReadIntegerExpr(k.getPos) }
    | Keyword("ReadLine") ~ Delimiter("(") ~ Delimiter(")") ^^{ case k ~ Delimiter("(") ~ Delimiter(")") => ReadLineExpr(k.getPos) }
    | Keyword("new") ~ ident ^^{ case Keyword("new") ~ i => NewExpr(i.getPos, NamedType(i))}
    | Keyword("NewArray") ~ Delimiter("(") ~ expr ~ Delimiter(",") ~ typ ~ Delimiter(")") ^^{ case Keyword("NewArray") ~ Delimiter("(") ~ e ~ Delimiter(",") ~ t ~ Delimiter(")") => NewArrayExpr(e.getPos,e,t)}
    )
  def lValue: Parser[LValue] = (
    ident ^^{ case i => FieldAccess(i.getPos, None, i)}
    | expr.? ~ Delimiter(".") ~ ident ^^{case e ~ Delimiter(".") ~ i => FieldAccess(i.getPos, e, i)
    }
    | expr ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^{ case first ~ Delimiter("[") ~ last ~ Delimiter("]") =>
      ArrayAccess(first.getPos, first, last)
      }
    )
  def call: Parser[Call] = (
    ident ~  Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case field ~ Delimiter("(") ~ args ~ Delimiter(")") => new Call(field.getPos, field, args)
    }
    | expr ~ Delimiter(".") ~ ident ~  Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case base ~ Delimiter(".") ~ field ~ Delimiter("(") ~ args ~ Delimiter(")") => new Call(base.getPos, base, field, args)
    }
    )
  def variableDecl = typ ~ ident ~ Delimiter(";") ^^{
    case t ~ e => VarDecl(e.asInstanceOf[ASTIdentifier], t.asInstanceOf[Type])
  }
  def ident = elem("ident", _.isInstanceOf[Identifier]) ^^{ case i:Identifier => ASTIdentifier(Some(i.getPos), i.value)}
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
