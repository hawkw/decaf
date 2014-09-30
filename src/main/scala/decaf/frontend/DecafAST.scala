package decaf.frontend

import scala.util.parsing.input.Position

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 * Created by hawk on 9/30/14.
 */
trait DecafAST {

  abstract sealed class ASTNode (val location: Option[Position]) {
    protected var parent: ASTNode = null

    def getName: String = this.getClass.getSimpleName

    override def toString = print(0)

    /**
     * Prettyprint the node, given an indentation level and optionally a label
     * @param indentLevel the level to indent the node's name
     * @param label an optional label to attach to the node's
     * @return a String containing the prettyprint representation of the node
     */
    protected def print (indentLevel: Int, label: Option[String]=None): String = {
      val spaces = 3
      val result = new StringBuilder
      result += '\n'
      if (location.isDefined)
        result ++= ("%" + spaces + "d").format(location.get.line)
      else result ++= " "* spaces
      result ++= " " * (indentLevel*spaces) + label.getOrElse("") + getName
      result ++= printChildren(indentLevel)

      result.toString
    }
    protected def printChildren (indentLevel: Int): String
  }

  case class Identifier(loc: Option[Position], name: String) extends ASTNode(loc) {
    def this (name: String)  = this(None, name)
    def this (loc: Position, name:String) = this (Some(loc), name)

    override def getName = name
    def printChildren(indentLevel: Int): Unit = getName
  }

  case class Program(decls: List[Decl]) extends ASTNode(None) {
    decls.foreach{d => d.parent = this}

    def printChildren(indentLevel: Int): String = decls.reduceLeft[String]{
      (acc, decl) => acc + decl.print(indentLevel +1)
    } + "\n"
  }

  /*----------------------- Statements ----------------------------------------------------------------------------*/
  abstract case class Stmt(locat: Option[Position]) extends ASTNode(locat)

  case class StmtBlock(decls: List[Decl],
                       stmts: List[Stmt],
                        loc: Option[Position]) extends Stmt(loc) {

    def this(decls: List[Decl], stmts: List[Stmt]) = this(decls, stmts, None)

    def this(decls: List[Decl], stmts: List[Stmt], loc: Position) = this(decls, stmts,Some(loc))

    decls.foreach(d => d.parent = this)
    stmts.foreach(s => s.parent = this)

    protected def printChildren(indentLevel: Int): String = {
      decls.reduceLeft[String] {
        (acc, decl) => acc + decl.print(indentLevel + 1)
      } + stmts.reduceLeft[String] {
        (acc, stmt) => acc + stmt.print(indentLevel + 1)
      }
    }
  }

  /*----------------------- Expressions ----------------------------------------------------------------------------*/
  abstract class Expr(locat: Option[Position]) extends Stmt(locat)


  /*----------------------- Declarations ---------------------------------------------------------------------------*/
  abstract case class Decl(id: Identifier) extends ASTNode(id.loc) {
    id.parent = this
  }

  case class VarDecl(n: Identifier, t: Type) extends Decl(n) {
    t.parent = this
    def printChildren(indentLevel: Int) = {n.print(indentLevel +1) + id.print(indentLevel+1)}
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
      } else {""} + implements.reduceLeft[String]{
        (acc, nt) => acc + nt.print(indentLevel + 1, Some("(implements)"))
      } + members.reduceLeft[String] {
        (acc, decl) => acc + decl.print(indentLevel + 1)
      }
    }
  }

  case class InterfaceDecl(name: Identifier, members: List[Decl]) extends Decl(name) {
    members.foreach { d => d.parent = this}

    def printChildren(indentLevel: Int) = name.print(indentLevel + 1) + members.reduceLeft[String] {
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
      id.print(indentLevel + 1) +
      formals.reduceLeft[String] { (acc, decl) => acc + decl.print(indentLevel + 1, Some("(formals)"))} +
      (if (body != null) {
        body.print(indentLevel + 1, Some("(body)"))
      } else {
        ""
      })

    override def getName: String = "FnDecl"
  }

  /*----------------------- Types ---------------------------------------------------------------------------------*/
  abstract case class Type(typeName: String, loc: Option[Position]) extends ASTNode(None) {
    override def getName = "Type"
    protected def printChildren(indentLevel: Int): String = typeName
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
