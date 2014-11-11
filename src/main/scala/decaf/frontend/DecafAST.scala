package decaf.frontend
import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 *
 * I have made a couple tweaks to the pretty-printing code ([[ASTNode.stringify stringify()]]) here.
 * Specifically, we've decided that floating-point  numbers ([[ASTDoubleConstant ASTDoubleConstant]])
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

  /**
   * Abstract class for nodes in the Decaf abstract syntax tree.
   * @param location an [[scala.Option Option]] on a [[scala.util.parsing.input.Position Position]] containing the line
   *                 number of the Decaf statements represented by this node in the input file. Some nodes
   *                 (i.e. [[StmtBlock]]) represent multiple lines and have no position, they will pass
   *                 [[scala.None None]] to the [[ASTNode]] constructor automagically.
   */
  abstract sealed class ASTNode(location: Position) extends Positional {
    val printLine = true
    var color: Boolean = false
    var state: Option[ScopeNode] = None
    var parent: ASTNode = null
    this.setPos(location)

    /**
     * Returns the name of this node type for printing.
     *
     * By default, this is the class name of the node. For some nodes, such as [[ASTIdentifier]], this should be
     * something else; those nodes can override this method.
     *
     * @return a String containing the name of this node type for printing
     */
     def getName: String = this.getClass.getSimpleName + ":"

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
     def stringify (indentLevel: Int, label: Option[String]=None): String = {
      val spaces = 3
      val result = new StringBuilder
      result += '\n'
      if (printLine)
        result ++= ("%" + spaces + "d").format(pos.line)
      else
        result ++= " "* spaces
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
     def stringifyChildren (indentLevel: Int): String
  }

  case class ASTIdentifier(loc: Position, name: String) extends ASTNode(loc) {

    override def getName = "Identifier: " + name
    def stringifyChildren(indentLevel: Int) = ""
  }

  case class Program(decls: List[Decl], loc: Position) extends ASTNode(loc) {
    override val printLine = false
    decls.foreach{d => d.parent = this}

    def stringifyChildren(indentLevel: Int): String = decls.foldLeft[String](""){
      (acc, decl) => acc + decl.stringify(indentLevel +1)
    } + "\n"
  }

  /*----------------------- Statements ----------------------------------------------------------------------------*/
  abstract class Stmt(locat: Position) extends ASTNode(locat)

  case class StmtBlock(decls: List[VarDecl],
                       stmts: List[Stmt],
                       loc: Position) extends Stmt(loc) {
    override val printLine=false
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

  abstract class ConditionalStmt(testExpr: Expr, body: Stmt) extends Stmt(testExpr.pos){
    override val printLine = false
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

  case class BreakStmt(loc: Position) extends Stmt(loc) {
    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ReturnStmt(loc: Position, expr: Option[Expr]) extends Stmt(loc) {
    if (expr.isDefined)
      expr.get.parent = this

    def stringifyChildren(indentLevel: Int): String = if (expr.isDefined) { expr.get.stringify(indentLevel + 1) } else {""}
  }

  case class PrintStmt(args: List[Expr], loc:Position) extends Stmt(loc) {
    override val printLine = false
    args.foreach{e => e.parent = this}

    def stringifyChildren(indentLevel: Int): String = args.foldLeft[String](""){
      (acc, expr) => acc + expr.stringify(indentLevel + 1, Some("(args)"))
    }
  }

  case class SwitchStmt(variable: Option[Expr], cases: List[CaseStmt], default: Option[DefaultCase], loc: Position) extends Stmt(loc) {
    override val printLine = false
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
  case class CaseStmt(value: Expr, body: List[Stmt], loc: Position) extends Stmt(loc) {
    override val printLine = false
    value.parent = this
    body.foreach{_.parent = this}

    override def getName = "Case:"

    def stringifyChildren(indentLevel: Int): String = {
      value.stringify(indentLevel + 1) + body.foldLeft[String](""){
        (acc, s) => acc + s.stringify(indentLevel + 1)
      }
    }
  }

  case class DefaultCase(body: List[Stmt], loc: Position) extends Stmt(loc) {
    override val printLine = false
    body.foreach{_.parent = this}

    override def getName = "Default:"

    def stringifyChildren(indentLevel: Int): String = {
      body.foldLeft[String](""){
        (acc, s) => acc + s.stringify(indentLevel + 1)
      }
    }
  }

  /*----------------------- Expressions ----------------------------------------------------------------------------*/
  abstract class Expr(where: Position) extends Stmt(where) {
    def typeof(scope: ScopeNode): Type
  }

  case class EmptyExpr(loc: Position) extends Expr(loc) {
    override val printLine = false
    override def getName = "Empty:"

    override def typeof(scope: ScopeNode): Type = VoidType(loc)

    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTIntConstant(loc: Position, value: Int) extends Expr(loc) {
    override def getName = "IntConstant: "

    override def typeof(scope: ScopeNode): Type = IntType(loc)

    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTDoubleConstant(loc: Position, value: Double) extends Expr(loc) {
    override def getName = "DoubleConstant: "

    override def typeof(scope: ScopeNode): Type = DoubleType(loc)

    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTBoolConstant(loc: Position, value: Boolean) extends Expr(loc) {
    override def getName = "BoolConstant: "

    override def typeof(scope: ScopeNode): Type = BoolType(loc)

    def stringifyChildren(indentLevel: Int): String = value.toString
  }

  case class ASTStringConstant(loc: Position, value: String) extends Expr(loc) {
    override def getName = "StringConstant: "

    override def typeof(scope: ScopeNode): Type = StringType(loc)

    def stringifyChildren(indentLevel: Int): String = value
  }

  case class ASTNullConstant(loc: Position) extends Expr(loc) {
    override def getName = "NullConstant: "

    override def typeof(scope: ScopeNode): Type = NullType(loc)

    def stringifyChildren(indentLevel: Int): String = ""
  }

  case class ASTOperator(loc: Position, token: String) extends ASTNode(loc) {
    override def getName = "Operator: "

    def stringifyChildren(indentLevel: Int): String = token
  }

  abstract class CompoundExpr(loc: Position,
                              protected val left: Option[Expr],
                              protected val op: ASTOperator,
                              protected val right: Expr) extends Expr(loc) {
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
      case (StringType(_), ASTOperator(_, "+"), _) => StringType(pos)
      case (_, ASTOperator(_, "+"), StringType(_)) => StringType(pos)
      case (IntType(_), _, IntType(_)) => IntType(pos)
      case (DoubleType(_), ASTOperator(_, oper), DoubleType(_)) if oper != "%" => DoubleType(pos)
      // type lifting int -> double
      case (IntType(_), ASTOperator(_, oper), DoubleType(_)) if oper != "%" => DoubleType(pos)
      case (DoubleType(_), ASTOperator(_, oper), IntType(_)) if oper != "%" => DoubleType(pos)
      case (left: Type, op: ASTOperator, right: Type) if !left.isInstanceOf[ErrorType] && !right.isInstanceOf[ErrorType] =>
        new ErrorType(" *** Incompatible operands: " + left.typeName + " " + op.token + " "  + right.typeName, pos)
      case (e: ErrorType,_,_) => e // Most specific error bubbles through
      case (_,_,e: ErrorType) => e // this is not as specified, but is more similar to the behaviour of Real Compilers
    }
  }

  case class RelationalExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs) {
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), rhs.typeof(scope)) match {
      case (e: ErrorType, _) => e
      case (_,e: ErrorType) => e
      case (IntType(_), IntType(_)) => BoolType(pos)
      case (IntType(_), DoubleType(_)) => BoolType(pos)
      case (DoubleType(_), IntType(_)) => BoolType(pos)
      case (DoubleType(_), DoubleType(_)) => BoolType(pos)
      case (l, r) => new ErrorType(s" *** Incompatible operands: ${l.typeName} ${this.o.token} ${r.typeName}", pos)
    }
  }

  case class EqualityExpr(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, Some(lhs), o, rhs) {
    override def typeof(scope: ScopeNode): Type = (lhs.typeof(scope), rhs.typeof(scope)) match {
      case (e: ErrorType, _) => e
      case (_,e: ErrorType) => e
      case (l: VoidType, r) => new ErrorType(s" *** Incompatible operands: ${l.typeName} ${this.o.token} ${r.typeName}", pos)
      case (l, r: VoidType) => new ErrorType(s" *** Incompatible operands: ${l.typeName} ${this.o.token} ${r.typeName}", pos)
      case (_,_) => BoolType(pos)
    }
  }

  case class PostfixExpr(l: Position, o: ASTOperator, rhs: Expr) extends CompoundExpr(l, None, o, rhs) {
    override def stringifyChildren(indentLevel: Int): String = {
      rhs.stringify(indentLevel + 1) + o.stringify(indentLevel + 1)
    }
    override def typeof(scope: ScopeNode): Type = rhs.typeof(scope) match {
      case e: ErrorType => e
      case IntType(_) => IntType(pos)
      case DoubleType(_) => DoubleType(pos)
      case _ => NullType(l)
    }
  }

  case class LogicalExpr(l: Position, lhs: Option[Expr], o: ASTOperator, rhs: Expr) extends CompoundExpr(l, lhs, o, rhs) {
    def this(l: Position, o: ASTOperator, rhs: Expr) = this(l, None, o, rhs)

    def this(l: Position, lhs: Expr, o: ASTOperator, rhs: Expr) = this(l, Some(lhs), o, rhs)
    override def typeof(scope: ScopeNode): Type = lhs match {
        case Some(leftHand) => (leftHand.typeof(scope), rhs.typeof(scope)) match {
        case (e: ErrorType, _) => e
        case (_, e: ErrorType) => e
        case (l: BoolType, r: BoolType) => BoolType(pos)
        case (l, r) => new ErrorType(s" *** Incompatible operand: ${l.typeName} ${o.token} ${r.typeName}", pos)
      }
      case None => rhs.typeof(scope) match {
        case e: ErrorType => e
        case BoolType(_) => BoolType(pos)
        case r => new ErrorType(s" *** Incompatible operand: ! ${r.typeName}", pos)
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
      case (_,_) => NullType(l)
    }
  }

  abstract class LValue(loc: Position) extends Expr(loc)

  case class This(loc: Position) extends Expr(loc) {
    def stringifyChildren(indentLevel: Int): String = ""
    override def typeof(scope: ScopeNode): Type = if (scope.table.chainContains("this")) {
      val t = scope.table.get("this").get
      t match {
        case c: ClassAnnotation => c.name
        case _ => new ErrorType("*** Keyword `this` expected class declaration, got variable or function.", loc)
      }
    } else {
      new ErrorType("*** Keyword `this` used outside of acceptable context.", loc)
    }
  }

  case class ArrayAccess(loc: Position, base: Expr, subscript: Expr) extends LValue(loc) {
    base.parent = this
    subscript.parent = this

    override def stringifyChildren(indentLevel: Int): String = {
      base.stringify(indentLevel + 1) +
        subscript.stringify(indentLevel + 1, Some("(subscript)"))
    }
    override def typeof(scope: ScopeNode): Type = this.base.typeof(scope) match {
      case e: ErrorType => e
      case a: ArrayType => a.elemType //TODO: Is this right?
      case _ => new ErrorType(" *** TypeError from ArrayAccess: ???", pos) //TODO: What string goes here?
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
    override def typeof(scope: ScopeNode): Type = base match {
        //TODO: Verify me
      case Some(b) => b.typeof(scope) match {
        case NamedType(name) => if (scope.table chainContains name.name) {
          scope.table.get(name.name).get match {
            case VariableAnnotation(_,where) => new ErrorType("*** EXTREMELY BAD PROBLEM: this should not happen ever" +
              "\n*** please contact the decaf implementors and I am sorry", where)
            case MethodAnnotation(_, _, where) => new ErrorType("*** EXTREMELY BAD PROBLEM: this should not happen ever" +
              "\n*** please contact the decaf implementors and I am sorry", where)
            case ClassAnnotation(_, _, _, classScope, where) => classScope get field.name match {
              case Some(thing) => thing match {
                case VariableAnnotation(t, _) => t
                case MethodAnnotation(_, _, _) => new ErrorType("*** Attempt to field access a method", where)
                case ClassAnnotation(_, _, _, _, _) => new ErrorType("*** Attempt to field access a class", where)
              }
              case None => UndeclaredType("*** No declaration for variable ‘" + field.name + "’ found.", where)
            }
          }
        } else {
          UndeclaredType("*** No declaration for class ‘" + name.name + "’ found", loc)
        }
        // We expect that "EXTREMELY BAD PROBLEM" should only occur if the parser has generated
        // something that should be impossible for it to generate.
        case _ => new ErrorType("*** EXTREMELY BAD PROBLEM: this should not happen ever" +
          "\n*** please contact the decaf implementors and I am sorry", loc)
      }

      case None => if (scope.table chainContains field.name) {
        scope.table.get(field.name).get match {
          case VariableAnnotation(t, _) => t
          case MethodAnnotation(_,_,where) => new ErrorType("*** Attempt to field access a method", where)
          case ClassAnnotation(_,_,_,_,where) => new ErrorType("*** Attempt to field access a class", where)
        }
      } else {
        UndeclaredType("*** No declaration for variable ‘" + field.name + "’ found.", loc)
      }
    }
  }

  case class Call(loc: Position, base: Option[Expr], field: ASTIdentifier, args: List[Expr]) extends Expr(loc) {
    def this(loc: Position, base: Expr, field: ASTIdentifier, args: List[Expr]) = this(loc, Some(base), field, args)

    def this(loc: Position, field: ASTIdentifier, args: List[Expr]) = this(loc, None, field, args: List[Expr])

    def stringifyChildren(indentLevel: Int): String = (if (base.isDefined) { base.get.stringify(indentLevel + 1) }
    else {
      ""
    }) +
      field.stringify(indentLevel + 1) + args.foldLeft[String](""){ (acc, expr) => acc + expr.stringify(indentLevel + 1, Some("(actuals)"))}
    override def typeof(scope: ScopeNode): Type = base match {
      case Some(e) => e.typeof(scope)
      case None =>
        if(scope.table.chainContains(field.name)) {
          TypeAnnotation t = scope.table.get(field.name).get
        }
        NullType(pos)
    }
  }

  case class NewExpr(loc: Position, cType: NamedType) extends Expr(loc) {
    cType.parent = this
    def stringifyChildren(indentLevel: Int): String = cType.stringify(indentLevel + 1)
    override def typeof(scope: ScopeNode): Type = NullType(loc) //TODO: NYI
  }

  case class NewArrayExpr(loc: Position, size: Expr, elemType: Type) extends Expr(loc) {
    size.parent = this
    elemType.parent = this
    def stringifyChildren(indentLevel: Int): String = size.stringify(indentLevel + 1) + elemType.stringify(indentLevel + 1)
    override def typeof(scope: ScopeNode): Type = NullType(loc) //TODO: NYI
  }

  case class ReadIntegerExpr(loc: Position) extends Expr(loc) {
    def stringifyChildren(indentLevel: Int): String = ""
    override def typeof(scope: ScopeNode): Type = NullType(loc) //TODO: NYI
  }

  case class ReadLineExpr(loc: Position) extends Expr(loc) {
    def stringifyChildren(indentLevel: Int): String = ""
    override def typeof(scope: ScopeNode): Type = NullType(loc) //TODO: NYI
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
  abstract class Type(val typeName: String, loc: Position) extends ASTNode(loc) {
    override val printLine = false
    override def getName = "Type: "
     def stringifyChildren(indentLevel: Int): String = typeName
  }
  // builtin classes for primitive types
  case class IntType(loc: Position) extends Type("int", loc)
  case class DoubleType(loc: Position) extends Type("double", loc)
  case class BoolType(loc: Position) extends Type("bool", loc)
  case class VoidType(loc: Position) extends Type("void", loc)
  case class NullType(loc: Position) extends Type("null", loc)
  case class StringType(loc: Position) extends Type("string", loc)
  class ErrorType(val message: String, where: Position) extends Type("error", where)
  case class UndeclaredType(m: String, w: Position) extends ErrorType(m, w)

  case class NamedType(name: ASTIdentifier) extends Type(name.name, name.pos) {
    override val printLine = true
    override def getName = "NamedType:"
    name.parent = this
    override def stringifyChildren(indentLevel: Int) = name.stringify(indentLevel +1)
  }

  case class ArrayType(locat: Position, elemType: Type) extends Type(elemType.typeName + " Array", locat) {
    override val printLine = true
    override def getName = "ArrayType:"
    elemType.parent = this
    override def stringifyChildren(indentLevel: Int) = elemType.stringify(indentLevel +1)
  }