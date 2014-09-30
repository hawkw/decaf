package decaf.frontend

import scala.util.parsing.input.Position

/**
 * Decaf Abstract Syntax Tree, based on the C implementation provided by Professor Jumadinova.
 * Created by hawk on 9/30/14.
 */
trait DecafAST {

  abstract sealed class ASTNode (val location: Option[Position]) {
    protected var parent: ASTNode = null

    def getName: String

    override def toString = print(0)

    /**
     * Prettyprint the node, given an indentation level and optionally a label
     * @param indentLevel
     * @param label
     * @return
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

  case class Identifier(loc: Option[Position], val name: String) extends ASTNode(loc) {
    def this (name: String)  = this(None, name)
    def this (loc: Position, name:String) = this (Some(loc), name)
    def getName = name
    def printChildren(indentLevel: Int): Unit = getName
  }

  case class Program(decls: List[Decl]) extends ASTNode(None) {
    decls.foreach{d => d.parent = this}

    def getName: String = "Program"

    def printChildren(indentLevel: Int): String = decls.reduceLeft[String]{
      (acc, decl) => acc + decl.print(indentLevel +1)
    } + "\n"
  }

  abstract case class Stmt(locat: Option[Position]) extends ASTNode(locat)

  case class StmtBlock (val decls: List[Decl],
                        val stmts: List[Stmt],
                        loc: Option[Position]) extends Stmt(loc) {

    def this(decls: List[Decl], stmts: List[Stmt]) = this(decls, stmts, None)

    def this(decls: List[Decl], stmts: List[Stmt], loc: Position) = this(decls, stmts,Some(loc))

    decls.foreach(d => d.parent = this)
    stmts.foreach(s => s.parent = this)

    def getName: String = "StmtBlock"

    protected def printChildren(indentLevel: Int): String = {
      decls.reduceLeft[String] {
        (acc, decl) => acc + decl.print(indentLevel + 1)
      } + stmts.reduceLeft[String] {
        (acc, stmt) => acc + stmt.print(indentLevel + 1)
      }
    }
  }

  abstract case class Decl(id: Identifier) extends ASTNode(id.loc) {
    id.parent = this
  }

  case class VarDecl(n: Identifier, val t: Type) extends Decl(n) {
    t.parent = this
    def getName = "VarDecl"
    def printChildren(indentLevel: Int) = {n.print(indentLevel +1) + id.print(indentLevel+1)}
  }

  case class ClassDecl(name: Identifier,
                       val extnds: Option[NamedType]=None,
                       val implements: List[NamedType],
                       val members: List[Decl]) extends Decl(name) {
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

    def getName = "ClassDecl"
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

  abstract case class Type(val typeName: String, loc: Option[Position]) extends ASTNode(None) {
    def getName: String = "Type"
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
