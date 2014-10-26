package decaf.frontend
import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 *
 * I have made a couple tweaks to the pretty-printing code ([[DecafAST#ASTNode.stringify stringify()]]) here.
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

  type ScopeTable = ForkTable[ASTIdentifier, TypeAnnotation]

  abstract class TypeAnnotation {
    def matches(that: TypeAnnotation): Boolean
  }
  case class MethodAnnotation(returnType: Type, formals: List[Type]) extends TypeAnnotation {
    override def matches(that: TypeAnnotation): Boolean = that match {
      case MethodAnnotation(rt, f) => rt == returnType && f == formals
      case _ => false
    }
  }
  case class ClassAnnotation(ext: Option[ClassAnnotation],
                             implements: List[ScopeTable],
                             members: ScopeTable) extends TypeAnnotation {
    override def matches(that: TypeAnnotation): Boolean = that match {
      // matches if the that is a subclass of this
      case ClassAnnotation(e,i,m) => this == that || (if (e.isDefined) {e.get.matches(this)} else { false } )
      case _ => false
    }
  }

  case class VariableAnnotation(t: Type) extends TypeAnnotation {
    override def matches(that: TypeAnnotation): Boolean = that match {
      case VariableAnnotation(typ) => typ == t
      case _ => false
    }
  }

  case class ScopeNode(table: ScopeTable, parent: Option[ScopeNode] = None) {
    var children = List[ScopeNode]()
    def child: ScopeNode = {
      val c = new ScopeNode(table.fork(), Some(this))
      children = children :+ c
      c
    }
  }

  /**
   * Gets the base type of an array type
   * @param t
   * @return
   */
  def baseType(t: Type): Type = t match {
    case ArrayType(_, elem) => baseType(elem)
    case _ => _
  }

  /**
   * Abstract class for nodes in the Decaf abstract syntax tree.
   * @param location an [[scala.Option Option]] on a [[scala.util.parsing.input.Position Position]] containing the line
   *                 number of the Decaf statements represented by this node in the input file. Some nodes
   *                 (i.e. [[StmtBlock]]) represent multiple lines and have no position, they will pass
   *                 [[scala.None None]] to the [[ASTNode]] constructor automagically.
   */
  abstract sealed class ASTNode(val location: Option[Position]) extends Positional {
    var color: Boolean = false
    var state: Option[ScopeNode] = None
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

  abstract class ConditionalStmt(testExpr: Expr, body: Stmt) extends Stmt(None){
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

  case class SwitchStmt(variable: Option[Expr], cases: List[CaseStmt], default: Option[DefaultCase]) extends Stmt(None) {
    if (variable.isDefined) { variable.get.parent = this }
    cases.foreach{c => c.parent = this}
    if (default.isDefined) { default.get.parent = this }
    def stringifyChildren(indentLevel: Int): String = {
      (if (variable.isDefined) { variable.get.stringify(indentLevel + 1) } else {""}) +
      cases.foldLeft[String](""){
        (acc, c) => acc + c.stringify(indentLevel + 1)
      } +
      (if (default.isDefined) { default.get.stringify(indentLevel + 1) } else {""})
    }
  }
  case class CaseStmt(value: Expr, body: List[Stmt]) extends Stmt(None) {

    value.parent = this
    body.foreach{_.parent = this}

    override def getName = "Case:"

    def stringifyChildren(indentLevel: Int): String = {
      value.stringify(indentLevel + 1) + body.foldLeft[String](""){
        (acc, s) => acc + s.stringify(indentLevel + 1)
      }
    }
  }

  case class DefaultCase(body: List[Stmt]) extends Stmt(None) {

    body.foreach{_.parent = this}

    override def getName = "Default:"

    def stringifyChildren(indentLevel: Int): String = {
      body.foldLeft[String](""){
        (acc, s) => acc + s.stringify(indentLevel + 1)
      }
    }
  }

  /*----------------------- Expressions ----------------------------------------------------------------------------*/
  abstract class Expr(where: Option[Position]) extends Stmt(where) {
    def typeof(scope: ScopeNode): Type
  }

  case class EmptyExpr() extends Expr(None) {
    override def getName = "Empty:"

    override def typeof(scope: ScopeNode): Type = VoidType()

    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTIntConstant(loc: Position, value: Int) extends Expr(Some(loc)) {
    override def getName = "IntConstant: "

    override def typeof(scope: ScopeNode): Type = IntType()

     def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTDoubleConstant(loc: Position, value: Double) extends Expr(Some(loc)) {
    override def getName = "DoubleConstant: "

    override def typeof(scope: ScopeNode): Type = DoubleType()

    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTBoolConstant(loc: Position, value: Boolean) extends Expr(Some(loc)) {
    override def getName = "BoolConstant: "

    override def typeof(scope: ScopeNode): Type = BoolType()

    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTStringConstant(loc: Position, value: String) extends Expr(Some(loc)) {
    override def getName = "StringConstant: "

    override def typeof(scope: ScopeNode): Type = StringType()

    def stringifyChildren(indentLevel: Int): String = value
  }

  case class ASTNullConstant(loc: Position) extends Expr(Some(loc)) {
    override def getName = "NullConstant: "

    override def typeof(scope: ScopeNode): Type = NullType()

    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTOperator(loc: Position, val token: String) extends ASTNode(Some(loc)) {
    override def getName = "Operator: "

    //TODO: Is this even correct?
    //override def typeof(scope: ScopeNode): Type = ???

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


  case class ArithmeticExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs) {
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), o, rhs.typeof(scope)) match {
      case (String, ASTOperator(_, "+"), _) => StringType()
      case (_, ASTOperator(_, "+"), String) => StringType()
      case (IntType(), _, IntType()) => IntType()
      case (DoubleType(), ASTOperator(_, op), DoubleType()) if op != "%" => DoubleType()
        // type lifting int -> double
      case (IntType(), ASTOperator(_, op), DoubleType()) if op != "%" => DoubleType()
      case (DoubleType(), ASTOperator(_, op), IntType()) if op != "%" => DoubleType()
      case (left: Type, op: ASTOperator, right: Type) if !left.isInstanceOf[ErrorType] && !right.isInstanceOf[ErrorType] =>
        ErrorType(" *** Incompatible operands: " + left.typeName + " " + op.token + " "  + right.typeName)
      case (e: ErrorType,_,_) => e // Most specific error bubbles through
      case (_,_,e: ErrorType) => e // this is not as specified, but is more similar to the behaviour of Real Compilers
    }
  }

  case class RelationalExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs) {
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), rhs.typeof(scope)) match {
      case (e: ErrorType, _) => e //TODO: Stub
      case (_,e: ErrorType) => e
      case (_,_) => NullType() // butts are null
    }
  }

  case class EqualityExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs) {
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), rhs.typeof(scope)) match {
      case (e: ErrorType, _) => e //TODO: Stub
      case (_,e: ErrorType) => e
      case (_,_) => NullType()
    }
  }

  case class PostfixExpr(l: Position, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, None, o, rhs) {
    override def stringifyChildren(indentLevel: Int): String = {
      rhs.stringify(indentLevel + 1) + o.stringify(indentLevel + 1)
    }
    override def typeof(scope: ScopeNode): Type = rhs.typeof(scope) match {
      case e: ErrorType => e //TODO: Stub
      case _ => NullType()
    }
  }

  case class LogicalExpr(l: Position, lhs: Option[Expr], o: ASTOperator, rhs: Expr) extends CompoundExpr(l, lhs, o, rhs) {
    def this(l: Position, o: ASTOperator, rhs: Expr) = this(l, None, o, rhs)

    def this(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) = this(l, Some(lhs), o, rhs)
    override def typeof(scope: ScopeNode): Type = lhs match {
      case Some(l) => (l.typeof(scope), rhs.typeof(scope)) match {
        case (e: ErrorType, _) => e //TODO: Stub
        case (_, e: ErrorType) => e
        case (_, _) => NullType()
      }
      case None => rhs.typeof match {
        case e: ErrorType => e
        case _: NullType()
      }
    }

  }

  case class AssignExpr(l: Position, lhs: Expr, rhs: Expr) extends CompoundExpr(l, lhs, ASTOperator(l, "="), rhs) {
    override def stringifyChildren(indentLevel: Int): String = {
     right.stringify(indentLevel + 1) + op.stringify(indentLevel + 1) + (if (left.isDefined) { left.get.stringify(indentLevel + 1) }
     else {
       ""
     })
    }
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), rhs.typeof(scope)) match {
      case (e: ErrorType, _) => e //TODO: Stub
      case (_,e: ErrorType) => e
      case (_,_) => NullType()
    }
  }

  abstract class LValue(loc: Position) extends Expr(Some(loc))

  case class This(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
     override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class ArrayAccess(loc: Position, base: Expr, subscript: Expr) extends LValue(loc) {
    base.parent = this
    subscript.parent = this

    override def stringifyChildren(indentLevel: Int): String = {
      base.stringify(indentLevel + 1) +
      subscript.stringify(indentLevel + 1, Some("(subscript)"))
    }
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
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
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class Call(loc: Position, base: Option[Expr], field: ASTIdentifier, args: List[Expr]) extends Expr(Some(loc)) {
    def this(loc: Position, base: Expr, field: ASTIdentifier, args: List[Expr]) = this(loc, Some(base), field, args)

    def this(loc: Position, field: ASTIdentifier, args: List[Expr]) = this(loc, None, field, args: List[Expr])

     def stringifyChildren(indentLevel: Int): String = (if (base.isDefined) { base.get.stringify(indentLevel + 1) }
    else {
      ""
    }) +
      field.stringify(indentLevel + 1) + args.foldLeft[String](""){ (acc, expr) => acc + expr.stringify(indentLevel + 1, Some("(actuals)"))}
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class NewExpr(loc: Position, cType: NamedType) extends Expr(Some(loc)) {
    cType.parent = this
     def stringifyChildren(indentLevel: Int): String = cType.stringify(indentLevel + 1)
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class NewArrayExpr(loc: Position, size: Expr, elemType: Type) extends Expr(Some(loc)) {
    size.parent = this
    elemType.parent = this
     def stringifyChildren(indentLevel: Int): String = size.stringify(indentLevel + 1) + elemType.stringify(indentLevel + 1)
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class ReadIntegerExpr(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
  }

  case class ReadLineExpr(loc: Position) extends Expr(Some(loc)) {
     def stringifyChildren(indentLevel: Int): String = ""
    override def typeof(scope: ScopeNode): Type = NullType() //TODO: NYI
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
      name.stringify(indentLevel +1) +
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
                    body: Option[StmtBlock]) extends Decl(name) {
    def this (nam: ASTIdentifier, rt: Type, fnargs: List[VarDecl]) = this(nam, rt, fnargs, None)
    def this (nam: ASTIdentifier, rt: Type, fnargs: List[VarDecl], bod: StmtBlock) = this(nam, rt, fnargs, Some(bod))
    name.parent = this
    returnType.parent = this
    if (body.isDefined) body.get.parent = this
    formals.foreach { d => d.parent = this}

    def stringifyChildren(indentLevel: Int) = returnType.stringify(indentLevel + 1, Some("(return type)")) +
      name.stringify(indentLevel + 1) +
      formals.foldLeft[String](""){ (acc, decl) => acc + decl.stringify(indentLevel + 1, Some("(formals)"))} +
      (if (body.isDefined) {
        body.get.stringify(indentLevel + 1, Some("(body)"))
      } else {
        ""
      })

    override def getName: String = "FnDecl:"
  }

  /*----------------------- Types ---------------------------------------------------------------------------------*/
  abstract class Type(val typeName: String, loc: Option[Position]) extends ASTNode(loc) {
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
  case class ErrorType(message: String) extends Type("error", None)

  case class NamedType(name: ASTIdentifier) extends Type(name.getName, Some(name.getPos)) {
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
