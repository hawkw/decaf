package decaf.frontend

import scala.collection.mutable
import scala.collection.mutable.Queue

import scala.util.parsing.input.Position

/**
 * Created by hawk on 10/26/14.
 */
case class SemanticException(message: String, pos: Position) extends Exception(message) {
  lazy val lineOfCode = pos.longString.replaceAll("\n\n", "\n")

  override def toString: String = s"$lineOfCode\n$message"

}
class ConflictingDeclException(name: String, where: Position)
  extends SemanticException(s"*** Declaration of '$name' here conflicts with declaration on line ${where.line}", where)

class UndeclaredTypeException(name: String, where: Position)
  extends SemanticException(s"*** No declaration for class '$name' found", where)

class TypeSignatureException(name: String, where: Position)
  extends SemanticException(s"** Method ’$name’ must match inherited type signature", where)

abstract class TypeAnnotation(where: Position) {
  def matches(that: TypeAnnotation): Boolean
}
case class MethodAnnotation(returnType: Type, formals: List[Type], pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case MethodAnnotation(rt, f, _) => rt == returnType && f == formals
    case _ => false
  }
  override def toString = s"Method: ${returnType.typeName} -> (" +
    formals.map(_.typeName).mkString(",") + ")"
}
case class ClassAnnotation(name: NamedType,
                           ext: Option[NamedType],
                           implements: List[NamedType],
                           classScope: DecafSemantic.ScopeTable,
                           pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    // matches if the that is equal to this
    // WARNING WARNING WARNING
    // Had to remove superclass checking from
    // this function for Type reasons.
    // Will have to implement the superclass
    // check somewhere else, based on ScopeNode
    // rather than by chaining ClassAnnotations.
    // WARNING WARNING WARNING
    case ClassAnnotation(_, e,i,m, _) => this == that
    case _ => false
  }
  override def toString = s"Class: ${name.name.name}"
}

case class InterfaceAnnotation(name: NamedType,
                               interfaceScope: DecafSemantic.ScopeTable,
                               pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case InterfaceAnnotation(_, _, _) => this == that
    case _ => false
  }

  override def toString = s"Interface: ${name.name.name}"
}

case class VariableAnnotation(t: Type, pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case VariableAnnotation(typ, _) => typ == t
    case _ => false
  }
  override def toString = s"Variable of ${t.typeName}"
}

case class ScopeNode(table: DecafSemantic.ScopeTable, boundName: String, parent: Option[ScopeNode] = None, statement: ASTNode) {
  var children = List[ScopeNode]()
  def child(boundName: String, stmt: ASTNode): ScopeNode = {
    val c = new ScopeNode(table.fork(), boundName, Some(this), stmt)
    children = children :+ c
    c
  }
  override def toString = stringify(0)
  def stringify(indentLevel: Int): String = {
    val s = new StringBuilder
    s ++= (" "*indentLevel) + boundName + ":"
    s ++= table.prettyprint(indentLevel + 2)
    if(children.length > 0) {
      s ++= s"\n${" " * indentLevel}\\\\"
      s ++= children.foldLeft[String]("") { (acc, child) => acc + "\n" + child.stringify(indentLevel + 2)}
      s ++= s"\n${" " * indentLevel}//"
    }
    s.toString()
  }

}

object DecafSemantic {
  type ScopeTable = ForkTable[String, TypeAnnotation]

  def decorateScope (tree: ASTNode, scope: ScopeNode): Unit = {
    tree.state = Some(scope)
    tree match {
      case Program(decls) => decls.foreach {
        match {
          case f: FnDecl => decorateScope(f, scope)
          case v: VarDecl => decorateScope(v, scope)
          case c: ClassDecl => decorateScope(c, scope.child(s"Class Declaration of ${c.name.name}", c))
          case i: InterfaceDecl => decorateScope(i, scope.child(s"Interface Declaration of ${i.name.name}", i))
        }
      }
      case c: ClassDecl =>
        c.members.foreach {
            case f: FnDecl => decorateScope(f, scope)
            case v: VarDecl => decorateScope(v, scope)
            case _ => //class/interface decls can't be embedded.
          }
      case i: InterfaceDecl => i.members.foreach {
        case f: FnDecl => decorateScope(f, scope)
        case _ => //We shouldn't have any other types of decl in an interface. If we do, then we have a problem.
      }
      case f: FnDecl =>
        var s = scope.child(s"FnDecl (formals) ${f.name.name}", f)
        f.formals.foreach {
          decorateScope(_, s)
        }
        if(f.body.isDefined) {
          val decls = f.body.get.decls
          val stmts = f.body.get.stmts
          val fs = s.child(s"FnDecl (body) ${f.name.name}", f.body.get)
          f.body.get.state = Some(fs)
          decls.foreach { decorateScope(_, fs) }
          stmts.foreach { s => if(s.isInstanceOf[StmtBlock]) decorateScope(s, fs.child("Subblock", s)) }
        }
      case StmtBlock(decls, stmts) =>
        decls.foreach {
          decorateScope(_, scope)
        }
        stmts.foreach { s =>
          if(s.isInstanceOf[StmtBlock]) decorateScope(s, scope.child("Subblock", s))
        }
      case n: ASTNode => n.state = Some(scope)
    }
  }

  def descent(node: ASTNode): List[ASTNode] = {
    node match {
      case Program(decls) => decls
      case ClassDecl(_, _, _, members) => members
      case FnDecl(_,_,formals,Some(body)) => formals.asInstanceOf[List[ASTNode]] ::: body.asInstanceOf[ASTNode] :: Nil
      case StmtBlock(decls, stmts) => decls.asInstanceOf[List[ASTNode]] ::: stmts.asInstanceOf[List[ASTNode]]
      case _ => List[ASTNode]()
    }
  }

  def annotateVariable(v: VarDecl, compilerProblems: mutable.Queue[Exception]): Unit = {
    val ident = v.n
    val typ = v.t

    if(v.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + v.toString)
    }

    val state = v.state.get


    if (state.table.contains(ident.name)) {
      compilerProblems += new ConflictingDeclException(ident.name, ident.pos)
      return
    } else {
      state.table.put(ident.name, new VariableAnnotation(typ, v.getPos))
    }
  }

  def annotateFunction(fn: FnDecl, compilerProblems: mutable.Queue[Exception]): Unit = {
    var ident = fn.name
    val rettype = fn.returnType
    var formals = fn.formals

    if(fn.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + fn.toString)
    }

    val state = fn.state.get

    if(state.table.contains(ident.name)) {
      compilerProblems += new ConflictingDeclException(ident.name, ident.pos)
      return
    } else {
      state.table.put(ident.name, new MethodAnnotation(rettype, formals.map(_.t), fn.pos))
    }
    for (formal <- formals) {
      if(formal.state.isEmpty) {
        throw new IllegalArgumentException("Tree didn't contain a scope for\n" + formal.toString + "\nin " + fn.toString)
      }
      annotateVariable(formal, compilerProblems)
    }
    if(fn.body.isDefined) {
      annotateStmtBlock(fn.body.get, compilerProblems)
    }
  }

  def annotateStmtBlock(b: StmtBlock, compilerProblems: mutable.Queue[Exception]): Unit = {
    if(b.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't conatin a scope for\n" + b.toString)
    } else {
      val state = b.state.get
      for (decl <- b.decls) {
        annotateVariable(decl, compilerProblems)
      }

      for(stmt <- b.stmts) {
        stmt match {
          case s if s.isInstanceOf[StmtBlock] => annotateStmtBlock(stmt.asInstanceOf[StmtBlock], compilerProblems)
          case _ =>
        }
      }
    }
  }

  def annotateClass(c: ClassDecl, compilerProblems: mutable.Queue[Exception]): Unit = {
    if (c.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + c.toString)
    }
    var cscope = c.state.get
    if(cscope.table.contains("this")) {
      throw new IllegalArgumentException("keyword \'this\' already (accidentally?) bound for class scope in " + c.toString)
    } else {
      cscope.table.put("this", new VariableAnnotation(NamedType(c.name),c.pos))
    }

    for(member <- c.members) {
      member match {
        case v: VarDecl => annotateVariable(v, compilerProblems)
        case f: FnDecl => annotateFunction(f, compilerProblems)
      }
    }

    var pscope = c.state.get.parent
    if(pscope.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't have a parent scope to enclose class type declaration in class " + c.toString)
    } else {
      var ptable = pscope.get.table
      if(ptable.contains(c.name.name)) {
        compilerProblems += new ConflictingDeclException(c.name.name, c.name.loc.get)
        return
      } else {
        ptable.put(c.name.name, new ClassAnnotation(new NamedType(c.name), c.extnds, c.implements, cscope.table, c.pos))
      }
    }
    //TODO: Finish me
  }

  def annotateInterface(i: InterfaceDecl, compilerProblems: mutable.Queue[Exception]): Unit = {
    if(i.state.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't contain a scope for " + i.toString)
    }

    val iscope = i.state.get

    for(member <- i.members) {
      member match {
        case f: FnDecl => annotateFunction(f, compilerProblems)
      }
    }

    val pscope = i.state.get.parent

    if(pscope.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't have a parent scope to enclose interface declaration for interface " + i.toString)
    } else {
      val ptable = pscope.get.table
      if(ptable.contains(i.name.name)) {
        compilerProblems += new ConflictingDeclException(i.name.name, i.name.pos)
        return
      } else {
        ptable.put(i.name.name, new InterfaceAnnotation(new NamedType(i.name), iscope.table, i.pos))
      }
    }
  }

  def pullDeclsToScope (tree: ASTNode, compilerProblems: mutable.Queue[Exception]): Unit = {
    if (tree.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope at " + tree.toString)
    }
    var state = tree.state.get
    tree match {
      case Program(decls) => for(decl <- decls) {
        decl match {
          case v: VarDecl => annotateVariable(v, compilerProblems)
          case f: FnDecl => annotateFunction(f, compilerProblems)
          case c: ClassDecl => annotateClass(c, compilerProblems)
          case i: InterfaceDecl => annotateInterface(i, compilerProblems)
        }
      }
    }
  }

  def checkTypeExists(node: ScopeNode, value: Type, compilerProblems: mutable.Queue[Exception]): Unit = {
    value match {
      case n: NamedType =>
        if(!node.table.chainContains(n.name.name)) compilerProblems += new UndeclaredTypeException(n.name.name, node.statement.pos)
      case ArrayType(_, t) => checkTypeExists(node, t, compilerProblems)
      case VoidType() | IntType() | DoubleType() | BoolType() | StringType() | NullType() | UndeclaredType(_,_) =>
      case _ => compilerProblems += new SemanticException(s"Unexpected type '${value.typeName}'!", node.statement.pos)
    }
  }

  def simpleCheckTypes(scopeTree: ScopeNode, compilerProblems: mutable.Queue[Exception]): Unit = {
    scopeTree.table.keys.foreach { key =>
      for(decl <- scopeTree.table.get(key)) {
        decl match {
          case m: MethodAnnotation =>
            checkTypeExists(scopeTree, m.returnType, compilerProblems)
            m.formals.foreach(checkTypeExists(scopeTree,_,compilerProblems))
          case c: ClassAnnotation =>
            if(c.ext.isDefined) checkTypeExists(scopeTree, c.ext.get, compilerProblems)
            c.implements.foreach(checkTypeExists(scopeTree,_,compilerProblems))
          case i: InterfaceAnnotation => //don't actually need to check any inherent types in this declaration.
          case v: VariableAnnotation => checkTypeExists(scopeTree, v.t, compilerProblems)
        }
      }
    }
    scopeTree.children.foreach(simpleCheckTypes(_, compilerProblems))
  }

  /**
   * Performs the semantic analysis
   *
   * ===Max's initial description of the algorithm===
   * I think I've come up with the complete coloring algorithm on my walk back home, and it can be simplified in the
   * "algorithm" sense fairly easily if we can have 4 colors: Black, Green, Yellow, and Red.
   *
   * Here's how it goes:
   *   + Start at the top, which is black.
   *     - If the top is a valid expression AND its leaves are all green, it gets green
   *     - If the top is NOT valid and its children are all green, it gets red.
   *     - If the top is valid and its children are all a mix of green and red, it gets yellow
   *     - If the top is valid and any of its children are yellow, it gets yellow.
   *     -  Otherwise, recurse to any black children
   *   + Yellow = redux
   *   + At the top level, we redo the walk for any non-green children IF we have colored anything green during
   *     the previous pass.
   *   + If the top level is ever colored red, then the program is Wrong.
   *   + If the top level is ever colored yellow and we cannot make a pass that colors something new as green, then the
   *     program is Wrong
   *   + If the top level is ever green, we can stop and the program is Right
   *   + When we redo the walk, all the reds and yellows are painted Black again before we try it over.
   * @param top
   * @return
   */
  def analyze(top: Program): ScopeNode = {
    var continue = true
    var tree: ScopeNode = new ScopeNode(new ScopeTable, "Global", None, top)
    decorateScope(top, tree)
    val problems = mutable.Queue[Exception]()
    pullDeclsToScope(top, problems)
    simpleCheckTypes(top.state.get, problems)
    problems.map(System.err.println(_))
    top.state.get
  }

  def compileToSemantic(progn: String): ScopeNode = {
    val r = new DecafSyntactical().parse(progn)
    //System.out.println(r)
    analyze(r)
  }

  def main(args: Array[String]): Unit = {
    println(compileToSemantic("class Cow implements Animal { int a; void talk(String how) { Farts q; } }\n interface Animal {void talk(String how);}\n interface Animal {} \nclass Cow {}\nvoid main(String[][] args) { cow a; {a = b; int b;{}} }").toString)
  }
}