package decaf.frontend

import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 * Created by hawk on 9/30/14.
 */
trait DecafAST {

  abstract sealed class ASTNode(val location: Option[Position]) extends Positional {
    protected[DecafAST] var parent: ASTNode = null
    this.setPos(location.getOrElse(NoPosition))

    def getName: String = this.getClass.getSimpleName

    override def toString = print(0)

    /**
     * Prettyprint the node, given an indentation level and optionally a label
     * @param indentLevel the level to indent the node's name
     * @param label an optional label to attach to the node's
     * @return a String containing the prettyprint representation of the node
     */
    protected[DecafAST] def print (indentLevel: Int, label: Option[String]=None): String = {
      val spaces = 3
      val result = new StringBuilder
      result += '\n'
      if (location.isDefined)
        result ++= ("%" + spaces + "d").format(location.get.line)
      else result ++= " "* spaces
      result ++= " " * (indentLevel*spaces) + label.getOrElse("") + getName
      result ++= printChildren(indentLevel)

      result.toString()
    }
    protected[DecafAST] def printChildren (indentLevel: Int): String
  }

  case class Identifier(loc: Option[Position], name: String) extends ASTNode(loc) {
    def this (name: String)  = this(None, name)
    def this (loc: Position, name:String) = this (Some(loc), name)

    override def getName = name
    def printChildren(indentLevel: Int) = getName
  }

  case class Program(decls: List[Decl]) extends ASTNode(None) {
    decls.foreach{d => d.parent = this}

    def printChildren(indentLevel: Int): String = decls.foldLeft[String](""){
      (acc, decl) => acc + decl.print(indentLevel +1)
    } + "\n"
  }

  /*----------------------- Statements ----------------------------------------------------------------------------*/
  abstract class Stmt(locat: Option[Position]) extends ASTNode(locat)

  case class StmtBlock(decls: List[Decl],
                       stmts: List[Stmt]) extends Stmt(None) {

    decls.foreach(d => d.parent = this)
    stmts.foreach(s => s.parent = this)

    def printChildren(indentLevel: Int): String = {
      decls.foldLeft[String](""){
        (acc, decl) => acc + decl.print(indentLevel + 1)
      } + stmts.foldLeft[String](""){
        (acc, stmt) => acc + stmt.print(indentLevel + 1)
      }
    }
  }

  abstract class ConditionalStmt(testExpr: Expr, body: Stmt) extends Stmt(None) {
    testExpr.parent = this
    body.parent = this
  }

  abstract class LoopStmt(te: Expr, b: Stmt) extends ConditionalStmt(te, b)

  case class ForStmt(init: Expr, test: Expr, step: Expr, loopBody: Stmt) extends LoopStmt(test, loopBody) {
    init.parent = this
    step.parent = this
    loopBody.parent = this

     def printChildren(indentLevel: Int): String = {
      init.print(indentLevel + 1, Some("(init)")) +
        test.print(indentLevel + 1, Some("(test)")) +
        step.print(indentLevel + 1, Some("(step)")) +
        loopBody.print(indentLevel + 1, Some("(body)"))
    }
  }

  case class WhileStmt(test: Expr, loopBody: Stmt) extends LoopStmt(test, loopBody) {
     def printChildren(indentLevel: Int): String = test.print(indentLevel + 1, Some("(test)")) +
      loopBody.print(indentLevel + 1, Some("(body)"))
  }

  case class IfStmt(test: Expr, testBody: Stmt, elseBody: Option[Stmt]) extends ConditionalStmt(test, testBody) {
    def this(test: Expr, testBody: Stmt, elseBody: Stmt)  = this(test, testBody, Some(elseBody))
    def this(test: Expr, testBody: Stmt) = this(test, testBody, None)
    if (elseBody.isDefined) {elseBody.get.parent = this}
     def printChildren(indentLevel: Int): String = {
      test.print(indentLevel + 1, Some("(test)")) +
        testBody.print(indentLevel + 1, Some("(body)")) +
        (if (elseBody.isDefined) elseBody.get.print(indentLevel + 1) else "")
    }
  }

  case class BreakStmt(loc: Position) extends Stmt(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }

  case class ReturnStmt(loc: Position, expr: Expr) extends Stmt(Some(loc)) {
    expr.parent = this
     def printChildren(indentLevel: Int): String = expr.print(indentLevel + 1)
  }

  case class PrintStmt(args: List[Expr]) extends Stmt(None) {
    args.foreach{e => e.parent = this}

     def printChildren(indentLevel: Int): String = args.foldLeft[String](""){
      (acc, expr) => acc + expr.print(indentLevel + 1)
    }
  }

  /*----------------------- Expressions ----------------------------------------------------------------------------*/
  abstract class Expr(where: Option[Position]) extends Stmt(where) {}

  case class EmptyExpr(loc: Position) extends Expr(Some(loc)) {
    override def getName = "Empty"

     def printChildren(indentLevel: Int): String = ""
  }

  case class IntConstant(loc: Position, value: Int) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = value.toString
  }

  case class DoubleConstant(loc: Position, value: Double) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = value.toString
  }

  case class BoolConstant(loc: Position, value: Boolean) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = value.toString
  }

  case class StringConstant(loc: Position, value: String) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = value
  }

  case class NullConstant(loc: Position, value: Boolean) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }

  case class Operator(loc: Position, token: String) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = token
  }

  abstract class CompoundExpr(loc: Position, right: Expr, op: Operator, left: Option[Expr]) extends Expr(Some(loc)) {
    def this(loc: Position, right: Expr, op: Operator) = this(loc, right, op, None)

    def this(loc: Position, right: Expr, op: Operator, left: Expr) = this(loc, right, op, Some(left))

    op.parent = this
    right.parent = this
    if (left.isDefined) left.get.parent = this

    def printChildren(indentLevel: Int): String = {
      if (left.isDefined) left.get.print(indentLevel + 1)
      else {
        ""
      } + op.print(indentLevel + 1) + right.print(indentLevel + 1)
    }
  }

  case class ArithmeticExpr(l: Position, rhs: Expr, o: Operator, lhs: Expr) extends CompoundExpr(l, rhs, o, lhs)

  case class RelationalExpr(l: Position, rhs: Expr, o: Operator, lhs: Expr) extends CompoundExpr(l, rhs, o, lhs)

  case class EqualityExpr(l: Position, rhs: Expr, o: Operator, lhs: Expr) extends CompoundExpr(l, rhs, o, lhs)

  case class LogicalExpr(l: Position, rhs: Expr, o: Operator, lhs: Option[Expr]) extends CompoundExpr(l, rhs, o, lhs) {
    def this(l: Position, rhs: Expr, o: Operator) = this(l, rhs, o, None)

    def this(l: Position, rhs: Expr, o: Operator, lhs: Expr) = this(l, rhs, o, Some(lhs))
  }

  case class AssignExpr(l: Position, rhs: Expr, o: Operator, lhs: Expr) extends CompoundExpr(l, rhs, o, lhs)

  case class LValue(loc: Position) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }

  case class This(loc: Position) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }

  case class ArrayAccess(loc: Position, base: Expr, subscript: Expr) extends Expr(Some(loc)) {
    base.parent = this
    subscript.parent = this

    def printChildren(indentLevel: Int): String = {
      base.print(indentLevel + 1)
      subscript.print(indentLevel + 1, Some("(subscript"))
    }
  }

  case class FieldAccess(loc: Position, base: Option[Expr], field: Identifier) extends Expr(Some(loc)) {
    def this(loc: Position, base: Expr, field: Identifier) = this(loc, Some(base), field)

    def this(loc: Position, field: Identifier) = this(loc, None, field)

    field.parent = this
    if (base.isDefined) base.get.parent = this

     def printChildren(indentLevel: Int): String = {
      if (base.isDefined) base.get.print(indentLevel + 1)
      else {
        ""
      } +
        field.print(indentLevel + 1)
    }
  }

  case class Call(loc: Position, base: Option[Expr], field: Identifier, args: List[Expr]) extends Expr(Some(loc)) {
    def this(loc: Position, base: Expr, field: Identifier, args: List[Expr]) = this(loc, Some(base), field, args)

    def this(loc: Position, field: Identifier, args: List[Expr]) = this(loc, None, field, args: List[Expr])

     def printChildren(indentLevel: Int): String = if (base.isDefined) base.get.print(indentLevel + 1)
    else {
      ""
    } +
      field.print(indentLevel + 1) + args.foldLeft[String](""){ (acc, expr) => acc + expr.print(indentLevel + 1, Some("(actuals)"))}
  }

  case class NewExpr(loc: Position, cType: NamedType) extends Expr(Some(loc)) {
    cType.parent = this
     def printChildren(indentLevel: Int): String = cType.print(indentLevel + 1)
  }

  case class NewArrayExpr(loc: Position, size: Expr, elemType: Type) extends Expr(Some(loc)) {
    size.parent = this
    elemType.parent = this
     def printChildren(indentLevel: Int): String = size.print(indentLevel + 1) + elemType.print(indentLevel + 1)
  }

  case class ReadIntegerExpr(loc: Position) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }

  case class ReadLineExpr(loc: Position) extends Expr(Some(loc)) {
     def printChildren(indentLevel: Int): String = ""
  }
  /*----------------------- Declarations ---------------------------------------------------------------------------*/
  abstract class Decl(id: Identifier) extends ASTNode(id.loc) {
    id.parent = this
  }

  case class VarDecl(n: Identifier, t: Type) extends Decl(n) {
    t.parent = this
    def printChildren(indentLevel: Int) = {t.print(indentLevel +1) + n.print(indentLevel+1)}
  }

  case class ClassDecl(name: Identifier,
                       extnds: Option[NamedType] = None,
                       implements: List[NamedType],
                       members: List[Decl]) extends Decl(name) {
    def this(name: Identifier,
             ext: NamedType,
             implements: List[NamedType],
             members: List[Decl]) = this(name, Some(ext),implements,members)
    def this(name: Identifier,
             implements: List[NamedType],
             members: List[Decl]) = this(name, None, implements,members)

    if (extnds.isDefined)
      extnds.get.parent = this
    implements.foreach{nt => nt.parent = this}
    members.foreach{d => d.parent = this}

    def printChildren(indentLevel: Int) = {
      if (extnds.isDefined) {
        extnds.get.print(indentLevel+1, Some("(extends)"))
      } else {""} + implements.foldLeft[String](""){
        (acc, nt) => acc + nt.print(indentLevel + 1, Some("(implements)"))
      } + members.foldLeft[String](""){
        (acc, decl) => acc + decl.print(indentLevel + 1)
      }
    }
  }

  case class InterfaceDecl(name: Identifier, members: List[Decl]) extends Decl(name) {
    members.foreach { d => d.parent = this}

    def printChildren(indentLevel: Int) = name.print(indentLevel + 1) + members.foldLeft[String](""){
      (acc, decl) => acc + decl.print(indentLevel + 1)
    }

    override def getName: String = "InterfaceDecl"
  }

  case class FnDecl(name: Identifier,
                    returnType: Type,
                    formals: List[VarDecl]) extends Decl(name) {
    name.parent = this
    returnType.parent = this
    formals.foreach { d => d.parent = this}
    private var body: Stmt = null

    def setFunctionBody(b: Stmt) {
      this.body = b; b.parent = this
    }

    def printChildren(indentLevel: Int) = returnType.print(indentLevel + 1, Some("(return type)")) +
      name.print(indentLevel + 1) +
      formals.foldLeft[String](""){ (acc, decl) => acc + decl.print(indentLevel + 1, Some("(formals)"))} +
      (if (body != null) {
        body.print(indentLevel + 1, Some("(body)"))
      } else {
        ""
      })

    override def getName: String = "FnDecl"
  }

  /*----------------------- Types ---------------------------------------------------------------------------------*/
  abstract class Type(typeName: String, loc: Option[Position]) extends ASTNode(None) {
    override def getName = "Type"
    protected[DecafAST] def printChildren(indentLevel: Int): String = typeName
  }
  // builtin classes for primitive types
  case class IntType() extends Type("int", None)
  case class DoubleType() extends Type("double", None)
  case class BoolType() extends Type("bool", None)
  case class VoidType() extends Type("void", None)
  case class NullType() extends Type("null", None)
  case class StringType() extends Type("string", None)
  case class ErrorType() extends Type("error", None)

  case class NamedType(name: Identifier) extends Type(name.getName, name.loc) {
    override def getName = "NamedType"
    name.parent = this
    override def printChildren(indentLevel: Int) = name.print(indentLevel +1)
  }

  case class ArrayType(locat: Option[Position], elemType: Type) extends Type("", locat) {
    override def getName = "ArrayType"
    elemType.parent = this
    override def printChildren(indentLevel: Int) = elemType.print(indentLevel +1)
  }

}
