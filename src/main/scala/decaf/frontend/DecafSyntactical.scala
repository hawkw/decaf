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

import scala.util.parsing.combinator.syntactical.TokenParsers

/**
 * Syntactical analyzer for the Decaf programming language
 *
 * @author Hawk Weisman
 *         Created by hawk on 9/28/14.
 */
class DecafSyntactical extends TokenParsers with DecafAST with DecafTokens {
  override type Elem = DecafToken
  type Tokens = DecafToken
  val lexical = new DecafLexical

  def program: Program = rep(decl) ^^{case decls => new Program(decls)}
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
    case Delimiter("{") ~ stuff ~ Delimiter("}") => StmtBlock(stuff.filter(_.isInstanceOf[VarDecl]), stuff.filter(_.isInstanceOf[Stmt]))
  }
  def stmt =( expr.? ~ Delimiter(";")
    | ifStmt
    | whileStmt
    | forStmt
    | breakStmt
    | Keyword("return") ~ expr.? ~ Delimiter(";") ^^{ case k ~ thing ~ _ => ReturnStmt(k.getPos, Some(thing.asInstanceOf[Expr]))}
    )
  def const = (
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
  def expr = (
    lValue ~ Operator("=") ~ expr ^^{ case left ~ Operator("=") ~ right => AssignExpr(left.getPos, left, right)}
    | const
    | lValue
    | Keyword("this")
    | call
    | Delimiter("(") ~ expr ~ Delimiter(")")
    )
  def lValue = (
    ident
    | expr ~ Delimiter(".") ~ ident ^^{case e ~ Delimiter(".") ~ i => FieldAccess(i.getPos, Some(e.asInstanceOf[Expr]), i.asInstanceOf[ASTIdentifier]) }
    | expr ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^{ case e }
    )
  def variableDecl = typ ~ ident ~ Delimiter(";") ^^{
    case t ~ e => VarDecl(e.asInstanceOf[ASTIdentifier], t.asInstanceOf[Type])
  }
  def ident = elem("ident", _.isInstanceOf[Identifier]) ^^{ case i:Identifier => ASTIdentifier(Some(i.getPos), i.value)}
  def typ = (_typ | _typ ~ Delimiter("[]") ^^{ case t ~ dims => ArrayType(Some(dims.getPos), t.asInstanceOf[Type])})
  def _typ = (
    Keyword("int") ^^{ case k => IntType() }
    | Keyword("double") ^^{ case k => DoubleType() }
    | Keyword("bool") ^^{ case k => BoolType() }
    | Keyword("string") ^^{ case k => StringType() }
    | ident
    )
}
