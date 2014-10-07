package decaf.frontend

import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 *
 * I have made a couple tweaks to the pretty-printing code ([[DecafAST#ASTNode.stringify stringify()]] here.
 * Specifically, we've decided that floating-point  numbers ([[DecafAST#ASTDoubleConstant ASTDoubleConstant]])
 * without fractional parts should print out with a trailing "aesthetic" zero, as this indicates the number's identity
 * as a floating-point number.
 *
 * For example, the constant `1d` should pretty-print as `DoubleConstant: 1.0`, rather than `DoubleConstant: 1`.
 * I've modified the corresponding sample output to make this behaviour correct.
 *
 * @author Hawk Weisman
 *
 * Created by hawk on 9/30/14.
 */
trait DecafAST {

  /**
   * Abstract class for nodes in the Decaf abstract syntax tree.
   * @param location an [[scala.Option Option]] on a [[scala.util.parsing.input.Position Position]] containing the line
   *                 number of the Decaf statements represented by this node in the input file. Some nodes
   *                 (i.e. [[StmtBlock]]) represent multiple lines and have no position, they will pass
   *                 [[scala.None None]] to the [[ASTNode]] constructor automagically.
   */
  abstract sealed class ASTNode(val location: Option[Position]) extends Positional {
    protected[DecafAST] var parent: ASTNode = null
    this.setPos(location.getOrElse(NoPosition))
    def getPos = this.location.getOrElse(null)

    /**
     * Returns the name of this node type for printing.
     *
     * By default, this is the class name of the node. For some nodes, such as [[ASTIdentifier]], this should be
     * something else; those nodes can override this method.
     *
     * @return a String containing the name of this node type for printing
     */
    protected[DecafAST] def getName: String = this.getClass.getSimpleName + ":"

    /**
     * Returns a String representation of the tree with this node as the root node.
     * @return a String representation of the the with this node as the root node.
     */
    override def toString = stringify(0)

    /**
     * Returns a String representation of this node and it's leaves suitable for pretty-printing at the desired
     * indentation level.
     *
     * This is distinct from [[ASTNode.toString toString()]] because it takes arguments for the indentation level and
     * the label, because to properly pretty-print the tree, we need information to be passed from other nodes.
     * Therefore, this method is for internal use only; if you want a string representation of a node, call its'
     * [[ASTNode.toString toString()]] method instead.
     *
     * @param indentLevel the level to indent the node's name
     * @param label an optional label to attach to the node
     * @return a String containing the pretty-print representation of the node
     */
    protected[DecafAST] def stringify (indentLevel: Int, label: Option[String]=None): String = {
      val spaces = 3
      val result = new StringBuilder
      result += '\n'
      if (location.isDefined)
        result ++= ("%" + spaces + "d").format(location.get.line)
      else result ++= " "* spaces
      result ++= " " * (indentLevel*spaces) + (label match { case None => "" case Some(s) => s + " "}) + getName
      result ++= stringifyChildren(indentLevel)

      result.toString()
    }

    /**
     * Returns pretty-printable String representations of this node's children at the desired indent level, or epsilon
     * if this node has no children.
     *
     * This method is for internal use only; if you want a string representation of a node, call its'
     * [[ASTNode.toString toString()]] method instead.
     *
     * @param indentLevel the level to indent the children of this node; this should be the indentlevel of this node + 1
     * @return pretty-printable String representations of this node's children
     */
    protected[DecafAST] def stringifyChildren (indentLevel: Int): String
  }

  case class ASTIdentifier(loc: Option[Position], name: String) extends ASTNode(loc) {
    def this (name: String)  = this(None, name)
    def this (loc: Position, name:String) = this (Some(loc), name)

    override def getName = "Identifier: " + name
    def stringifyChildren(indentLevel: Int) = ""
  }

  case class Program(decls: List[Decl]) extends ASTNode(None) {
    decls.foreach{d => d.parent = this}

    def stringifyChildren(indentLevel: Int): String = decls.foldLeft[String](""){
      (acc, decl) => acc + decl.stringify(indentLevel +1)
    } + "\n"
  }

  /*----------------------- Statements ----------------------------------------------------------------------------*/
  abstract class Stmt(locat: Option[Position]) extends ASTNode(locat)

  case class StmtBlock(decls: List[Decl],
                       stmts: List[Stmt]) extends Stmt(None) {

    decls.foreach(d => d.parent = this)
    stmts.foreach(s => s.parent = this)

    def stringifyChildren(indentLevel: Int): String = {
      decls.foldLeft[String](""){
        (acc, decl) => acc + decl.stringify(indentLevel + 1)
      } + stmts.foldLeft[String](""){
        (acc, stmt) => acc + stmt.stringify(indentLevel + 1)
      }
    }
  }

  abstract class ConditionalStmt(testExpr: Expr, body: Stmt) extends Stmt(None) {
    testExpr.parent = this
    body.parent = this
  }

  abstract class LoopStmt(te: Expr, b: Stmt) extends ConditionalStmt(te, b)

  case class ForStmt(init: Option[Expr],
                     test: Expr,
                     step: Option[Expr],
                     loopBody: Stmt) extends LoopStmt(test, loopBody) {
    if (init.isDefined) init.get.parent = this
    if (step.isDefined) step.get.parent = this
    loopBody.parent = this

     def stringifyChildren(indentLevel: Int): String = {
        ( if (init.isDefined) { init.get.stringify(indentLevel + 1, Some("(init)")) } else {""} ) +
        test.stringify(indentLevel + 1, Some("(test)")) +
        ( if (step.isDefined) { step.get.stringify(indentLevel + 1, Some("(step)")) } else {""} ) +
        loopBody.stringify(indentLevel + 1, Some("(body)"))
    }
  }

  case class WhileStmt(test: Expr, loopBody: Stmt) extends LoopStmt(test, loopBody) {
     def stringifyChildren(indentLevel: Int): String = test.stringify(indentLevel + 1, Some("(test)")) +
      loopBody.stringify(indentLevel + 1, Some("(body)"))
  }

  case class IfStmt(test: Expr, testBody: Stmt, elseBody: Option[Stmt]) extends ConditionalStmt(test, testBody) {
    def this(test: Expr, testBody: Stmt, elseBody: Stmt)  = this(test, testBody, Some(elseBody))
    def this(test: Expr, testBody: Stmt) = this(test, testBody, None)
    if (elseBody.isDefined) {elseBody.get.parent = this}
     def stringifyChildren(indentLevel: Int): String = {
      test.stringify(indentLevel + 1, Some("(test)")) +
        testBody.stringify(indentLevel + 1, Some("(then)")) +
        (if (elseBody.isDefined) { elseBody.get.stringify(indentLevel + 1, Some("(else)")) } else { "" })
    }
  }

  case class BreakStmt(loc: Position) extends Stmt(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ReturnStmt(loc: Position, expr: Option[Expr]) extends Stmt(Some(loc)) {
    if (expr.isDefined)
      expr.get.parent = this

    def stringifyChildren(indentLevel: Int): String = (if (expr.isDefined) { expr.get.stringify(indentLevel + 1) } else {""})
  }

  case class PrintStmt(args: List[Expr]) extends Stmt(None) {
    args.foreach{e => e.parent = this}

     def stringifyChildren(indentLevel: Int): String = args.foldLeft[String](""){
      (acc, expr) => acc + expr.stringify(indentLevel + 1, Some("(args)"))
    }
  }

  /*----------------------- Expressions ----------------------------------------------------------------------------*/
  abstract class Expr(where: Option[Position]) extends Stmt(where) {}

  case class EmptyExpr() extends Expr(None) {
    override def getName = "Empty:"

     def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTIntConstant(loc: Position, value: Int) extends Expr(Some(loc)) {
    override def getName = "IntConstant: "
     def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTDoubleConstant(loc: Position, value: Double) extends Expr(Some(loc)) {
    override def getName = "DoubleConstant: "
    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTBoolConstant(loc: Position, value: Boolean) extends Expr(Some(loc)) {
    override def getName = "BoolConstant: "
    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTStringConstant(loc: Position, value: String) extends Expr(Some(loc)) {
    override def getName = "StringConstant: "
    def stringifyChildren(indentLevel: Int): String = value
  }

  case class ASTNullConstant(loc: Position) extends Expr(Some(loc)) {
    override def getName = "NullConstant: "
    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTOperator(loc: Position, token: String) extends Expr(Some(loc)) {
    override def getName = "Operator: "
    def stringifyChildren(indentLevel: Int): String = token
  }

  abstract class CompoundExpr(loc: Position,
                              protected val left: Option[Expr],
                              protected val op: ASTOperator,
                              protected val right: Expr) extends Expr(Some(loc)) {
    def this(loc: Position, right: Expr, op: ASTOperator) = this(loc, None, op, right)

    def this(loc: Position, right: Expr, op: ASTOperator, left: Expr) = this(loc, Some(left), op, right)

    op.parent = this
    right.parent = this
    if (left.isDefined) left.get.parent = this

    def stringifyChildren(indentLevel: Int): String = {
      (if (left.isDefined) { left.get.stringify(indentLevel + 1) }
      else {
        ""
      }) + op.stringify(indentLevel + 1) + right.stringify(indentLevel + 1)
    }
  }


  case class ArithmeticExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs)

  case class RelationalExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs)

  case class EqualityExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs)

  case class LogicalExpr(l: Position, lhs: Option[Expr], o: ASTOperator, rhs: Expr) extends CompoundExpr(l, lhs, o, rhs) {
    def this(l: Position, o: ASTOperator, rhs: Expr) = this(l, None, o, rhs)

    def this(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) = this(l, Some(lhs), o, rhs)
  }

  case class AssignExpr(l: Position, lhs: Expr, rhs: Expr) extends CompoundExpr(l, lhs, ASTOperator(l, "="), rhs) {
    override def stringifyChildren(indentLevel: Int): String = {
     right.stringify(indentLevel + 1) + op.stringify(indentLevel + 1) + (if (left.isDefined) { left.get.stringify(indentLevel + 1) }
     else {
       ""
     })
    }
  }

  abstract class LValue(loc: Position) extends Expr(Some(loc))

  case class This(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ArrayAccess(loc: Position, base: Expr, subscript: Expr) extends LValue(loc) {
    base.parent = this
    subscript.parent = this

    def stringifyChildren(indentLevel: Int): String = {
      base.stringify(indentLevel + 1)
      subscript.stringify(indentLevel + 1, Some("(subscript"))
    }
  }

  case class FieldAccess(loc: Position, base: Option[Expr], field: ASTIdentifier) extends LValue(loc) {
    def this(loc: Position, base: Expr, field: ASTIdentifier) = this(loc, Some(base), field)

    def this(loc: Position, field: ASTIdentifier) = this(loc, None, field)

    field.parent = this
    if (base.isDefined) base.get.parent = this

     def stringifyChildren(indentLevel: Int): String = {
       (if (base.isDefined) { base.get.stringify(indentLevel + 1) }
      else {
        ""
      }) +
        field.stringify(indentLevel + 1)
    }
  }

  case class Call(loc: Position, base: Option[Expr], field: ASTIdentifier, args: List[Expr]) extends Expr(Some(loc)) {
    def this(loc: Position, base: Expr, field: ASTIdentifier, args: List[Expr]) = this(loc, Some(base), field, args)

    def this(loc: Position, field: ASTIdentifier, args: List[Expr]) = this(loc, None, field, args: List[Expr])

     def stringifyChildren(indentLevel: Int): String = (if (base.isDefined) { base.get.stringify(indentLevel + 1) }
    else {
      ""
    }) +
      field.stringify(indentLevel + 1) + args.foldLeft[String](""){ (acc, expr) => acc + expr.stringify(indentLevel + 1, Some("(actuals)"))}
  }

  case class NewExpr(loc: Position, cType: NamedType) extends Expr(Some(loc)) {
    cType.parent = this
     def stringifyChildren(indentLevel: Int): String = cType.stringify(indentLevel + 1)
  }

  case class NewArrayExpr(loc: Position, size: Expr, elemType: Type) extends Expr(Some(loc)) {
    size.parent = this
    elemType.parent = this
     def stringifyChildren(indentLevel: Int): String = size.stringify(indentLevel + 1) + elemType.stringify(indentLevel + 1)
  }

  case class ReadIntegerExpr(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ReadLineExpr(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
  }
  /*----------------------- Declarations ---------------------------------------------------------------------------*/
  abstract class Decl(id: ASTIdentifier) extends ASTNode(id.loc) {
    id.parent = this
  }

  case class VarDecl(n: ASTIdentifier, t: Type) extends Decl(n) {
    t.parent = this
    def stringifyChildren(indentLevel: Int) = {t.stringify(indentLevel +1) + n.stringify(indentLevel+1)}
  }

  case class ClassDecl(name: ASTIdentifier,
                       extnds: Option[NamedType] = None,
                       implements: List[NamedType],
                       members: List[Decl]) extends Decl(name) {
    def this(name: ASTIdentifier,
             ext: NamedType,
             implements: List[NamedType],
             members: List[Decl]) = this(name, Some(ext),implements,members)
    def this(name: ASTIdentifier,
             implements: List[NamedType],
             members: List[Decl]) = this(name, None, implements,members)

    if (extnds.isDefined)
      extnds.get.parent = this
    implements.foreach{nt => nt.parent = this}
    members.foreach{d => d.parent = this}

    def stringifyChildren(indentLevel: Int) = {
      (if (extnds.isDefined) {
        extnds.get.stringify(indentLevel+1, Some("(extends)"))
      } else {""}) + implements.foldLeft[String](""){
        (acc, nt) => acc + nt.stringify(indentLevel + 1, Some("(implements)"))
      } + members.foldLeft[String](""){
        (acc, decl) => acc + decl.stringify(indentLevel + 1)
      }
    }
  }

  case class InterfaceDecl(name: ASTIdentifier, members: List[Decl]) extends Decl(name) {
    members.foreach { d => d.parent = this}

    def stringifyChildren(indentLevel: Int) = name.stringify(indentLevel + 1) + members.foldLeft[String](""){
      (acc, decl) => acc + decl.stringify(indentLevel + 1)
    }

    override def getName: String = "InterfaceDecl:"
  }

  case class FnDecl(name: ASTIdentifier,
                    returnType: Type,
                    formals: List[VarDecl],
                    body: StmtBlock) extends Decl(name) {
    name.parent = this
    returnType.parent = this
    body.parent = this
    formals.foreach { d => d.parent = this}

    def stringifyChildren(indentLevel: Int) = returnType.stringify(indentLevel + 1, Some("(return type)")) +
      name.stringify(indentLevel + 1) +
      formals.foldLeft[String](""){ (acc, decl) => acc + decl.stringify(indentLevel + 1, Some("(formals)"))} +
      (if (body != null) {
        body.stringify(indentLevel + 1, Some("(body)"))
      } else {
        ""
      })

    override def getName: String = "FnDecl:"
  }

  /*----------------------- Types ---------------------------------------------------------------------------------*/
  abstract class Type(typeName: String, loc: Option[Position]) extends ASTNode(None) {
    override def getName = "Type: "
    protected[DecafAST] def stringifyChildren(indentLevel: Int): String = typeName
  }
  // builtin classes for primitive types
  case class IntType() extends Type("int", None)
  case class DoubleType() extends Type("double", None)
  case class BoolType() extends Type("bool", None)
  case class VoidType() extends Type("void", None)
  case class NullType() extends Type("null", None)
  case class StringType() extends Type("string", None)
  case class ErrorType() extends Type("error", None)

  case class NamedType(name: ASTIdentifier) extends Type(name.getName, name.loc) {
    override def getName = "NamedType:"
    name.parent = this
    override def stringifyChildren(indentLevel: Int) = name.stringify(indentLevel +1)
  }

  case class ArrayType(locat: Option[Position], elemType: Type) extends Type("", locat) {
    override def getName = "ArrayType:"
    elemType.parent = this
    override def stringifyChildren(indentLevel: Int) = elemType.stringify(indentLevel +1)
  }

}
