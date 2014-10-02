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
  def formals = repsep(typ ~ ident, Delimiter(","))?
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
    //| call // TODO: Implement
    | Delimiter("(") ~ expr ~ Delimiter(")") ^^{ case Delimiter("(") ~ e ~ Delimiter(")") => e }
    )
  def lValue: Parser[LValue] = (
    //ident ^^{ case i => i } //TODO: Figure out
    expr.? ~ Delimiter(".") ~ ident ^^{case e ~ Delimiter(".") ~ i => FieldAccess(i.getPos, e, i)
    }
    | expr ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^{ case first ~ Delimiter("[") ~ last ~ Delimiter("]") =>
      ArrayAccess(first.getPos, first, last)
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
