package decaf.frontend

import scala.collection.mutable

import scala.util.parsing.input.Position


/**
 * Contains the Semantic Analysis stuff
 */
case class SemanticException(message: String, pos: Position) extends Exception(message) {
  lazy val lineOfCode = pos.longString.replaceAll("\n\n", "\n")

  override def toString: String = s"$lineOfCode\n$message\n"

}
class IllegalClassInheritanceCycle(className: String, where: Position)
  extends SemanticException(s"*** Illegal cyclic class inheritance involving $className on line ${where.line}", where)

class ConflictingDeclException(name: String, where: Position)
  extends SemanticException(s"*** Declaration of '$name' here conflicts with declaration on line ${where.line}", where)

class UndeclaredTypeException(name: String, where: Position)
  extends SemanticException(s"*** No declaration for class '$name' found", where)

class TypeSignatureException(name: String, where: Position)
  extends SemanticException(s"** Method ’$name’ must match inherited type signature", where)

class InvalidTestException(where: Position)
  extends SemanticException("*** Test expression must have boolean type", where)

class TypeErrorException(what: String, where: Position) extends SemanticException(what, where)

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
  implicit def errorType2TypeError(e: ErrorType): Exception = new TypeErrorException(e.message, e.pos)

  def decorateScope (tree: ASTNode, scope: ScopeNode): Unit = {
    tree.state = Some(scope)
    tree match {
      case Program(decls, _) => decls.foreach {
        case f: FnDecl => decorateScope(f, scope)
        case v: VarDecl => decorateScope(v, scope)
        case c: ClassDecl => decorateScope(c, scope.child(s"Class Declaration of ${c.name.name}", c))
        case i: InterfaceDecl => decorateScope(i, scope.child(s"Interface Declaration of ${i.name.name}", i))
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
        val s = scope.child(s"FnDecl (formals) ${f.name.name}", f)
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
      case StmtBlock(decls, stmts, _) =>
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
      case Program(decls, _) => decls
      case ClassDecl(_, _, _, members) => members
      case FnDecl(_,_,formals,Some(body)) => formals.asInstanceOf[List[ASTNode]] ::: body.asInstanceOf[ASTNode] :: Nil
      case StmtBlock(decls, stmts, _) => decls.asInstanceOf[List[ASTNode]] ::: stmts.asInstanceOf[List[ASTNode]]
      case _ => List[ASTNode]()
    }
  }

  def annotateVariable(v: VarDecl): List[Exception] = {
    val ident = v.n
    val typ = v.t

    if(v.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + v.toString)
    }

    val state = v.state.get


    if (state.table.contains(ident.name)) {
      new ConflictingDeclException(ident.name, ident.pos) :: List()
    } else {
      state.table.put(ident.name, new VariableAnnotation(typ, ident.pos))
      List()
    }
  }

  def annotateFunction(fn: FnDecl): List[Exception] = {
    var ident = fn.name
    var compilerProblems = List[Exception]()
    val rettype = fn.returnType
    val formals = fn.formals

    if(fn.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + fn.toString)
    }

    val state = fn.state.get

    if(state.table.contains(ident.name)) {
      compilerProblems = new ConflictingDeclException(ident.name, ident.pos) :: compilerProblems
      return compilerProblems
    } else {
      state.table.put(ident.name, new MethodAnnotation(rettype, formals.map(_.t), fn.pos))
    }
    for (formal <- formals) {
      if(formal.state.isEmpty) {
        throw new IllegalArgumentException("Tree didn't contain a scope for\n" + formal.toString + "\nin " + fn.toString)
      }
      compilerProblems = annotateVariable(formal) ::: compilerProblems
    }
    fn.body.foreach(body => compilerProblems = annotateStmtBlock(body) ::: compilerProblems)
    compilerProblems
  }

  def annotateStmtBlock(b: StmtBlock): List[Exception] = {
    var compilerProblems = List[Exception]()
    if(b.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't conatin a scope for\n" + b.toString)
    } else {
      b.decls.foreach {
        case decl => annotateVariable(decl) ::: compilerProblems
      }

      compilerProblems = compilerProblems ::: b.stmts.flatMap(
        _ match {
          case s: StmtBlock => annotateStmtBlock(s)
          case _ => Nil
        }
      )
    }
  compilerProblems
  }

  def annotateClass(c: ClassDecl): List[Exception] = {
    var compilerProblems = List[Exception]()
    if (c.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + c.toString)
    }
    val cscope = c.state.get
    if(cscope.table.contains("this")) {
      throw new IllegalArgumentException("keyword \'this\' already (accidentally?) bound for class scope in " + c.toString)
    } else {
      cscope.table.put("this", new VariableAnnotation(NamedType(c.name),c.pos))
    }

    compilerProblems = compilerProblems ::: c.members.map{
      _ match {
        case v: VarDecl => annotateVariable(v)
        case f: FnDecl => annotateFunction(f)
      }
    }.flatten

    val pscope = c.state.get.parent
    if(pscope.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't have a parent scope to enclose class type declaration in class " + c.toString)
    } else {
      val ptable = pscope.get.table
      if(ptable.contains(c.name.name)) {
        compilerProblems = new ConflictingDeclException(c.name.name, c.name.pos) :: compilerProblems
      } else {
        ptable.put(c.name.name, new ClassAnnotation(new NamedType(c.name), c.extnds, c.implements, cscope.table, c.pos))
      }
    }
    //TODO: Finish me
    compilerProblems
  }

  def annotateInterface(i: InterfaceDecl): List[Exception] = {
    var compilerProblems = List[Exception]()
    if(i.state.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't contain a scope for " + i.toString)
    }

    val iscope = i.state.get

    compilerProblems ::: i.members.map({case m: FnDecl =>
      annotateFunction(m)}
    ).flatten

    val pscope = i.state.get.parent

    if(pscope.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't have a parent scope to enclose interface declaration for interface " + i.toString)
    } else {
      val ptable = pscope.get.table
      if(ptable.contains(i.name.name)) {
        compilerProblems = new ConflictingDeclException(i.name.name, i.name.pos) :: compilerProblems
      } else {
        ptable.put(i.name.name, new InterfaceAnnotation(new NamedType(i.name), iscope.table, i.pos))
      }
    }
    compilerProblems
  }

  def pullDeclsToScope (tree: ASTNode): List[Exception] = {
    if (tree.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope at " + tree.toString)
    }
    var state = tree.state.get
    tree match {
      case Program(decls, _) => decls.map(
        _ match {
          case v: VarDecl => annotateVariable(v)
          case f: FnDecl => annotateFunction(f)
          case c: ClassDecl => annotateClass(c)
          case i: InterfaceDecl => annotateInterface(i)
        }
      ).flatten
    }
  }

  def checkTypeExists(node: ScopeNode,pos: Position, value: Type): List[Exception] = {
    value match {
      case n: NamedType =>
        if(!node.table.chainContains(n.name.name)) { new UndeclaredTypeException(n.name.name, pos) :: Nil } else Nil
      case ArrayType(_, t) => checkTypeExists(node, pos, t)
      case VoidType(_) | IntType(_) | DoubleType(_) | BoolType(_) | StringType(_) | NullType(_) => Nil
      case UndeclaredType(_,_) | _ => new SemanticException(s"Unexpected type '${value.typeName}'!", pos) :: Nil
    }
  }

  def verifyClassChain(scope: ScopeNode, seen: List[String], c: NamedType, p: Position): List[Exception] = {
    if(seen.contains(c.name.name)) {
      new IllegalClassInheritanceCycle(seen.head, p) :: Nil
    } else if(!scope.table.chainContains(c.name.name)) {
      //Exception is silent in this case; since we will report it during general typechecking elsewhere.
      //Unless we actually want to typecheck on this a billion times?
      Nil
    } else {
      val t = scope.table.get(c.name.name).get
      t match {
        case otherc: ClassAnnotation =>
          if(otherc.ext.isDefined) {
            verifyClassChain(scope, seen ::: c.name.name :: Nil, otherc.name, p)
          } else {
            //we've found a class which goes to ground, do we want to do anything here?
            //I think no.
            Nil
          }
        case _ => Nil //what should we even cause here? I.e. the type we got was NOT a class.
      }
    }
  }

  def checkTypes(ast: ASTNode): List[Exception] = {
    if(!ast.isInstanceOf[Program] && ast.state.isEmpty)
      throw new IllegalArgumentException("Tree does not contain scope for " + ast)
    val scope = ast.state.get
    ast match {
      case Program(d, _) => d.flatMap(checkTypes(_))

      case VarDecl(_, typ) =>
        checkTypeExists(ast.state.get, typ.pos, typ)

      case ClassDecl(_, ext, impl, mems) =>
        ext.map(verifyClassChain(scope, List[String](), _, ast.pos)).getOrElse(Nil) :::
          impl.flatMap(checkTypeExists(scope, ast.pos, _)) ::: mems.flatMap(checkTypes(_)) ::: Nil

      case InterfaceDecl(_, members) => members.flatMap(checkTypes(_))

      case FnDecl(_, rt, formals, body) =>
        checkTypeExists(scope, ast.pos, rt) :::
          formals.flatMap(checkTypes(_)) ::: body.map(checkTypes(_)).getOrElse(Nil)

      case StmtBlock(decls, stmts, _) => decls.flatMap(checkTypes(_)) ::: stmts.flatMap(checkTypes(_))

      case IfStmt(test, ifbody, elsebody) =>
        val t: List[Exception] = test.typeof(scope) match {
          case BoolType(_) => Nil
          case e: ErrorType =>  e :: new InvalidTestException(ast.pos) :: Nil
          case _ => new InvalidTestException(ast.pos) :: Nil
        }
        t ::: checkTypes(ifbody) ::: elsebody.map(checkTypes(_)).getOrElse(Nil)

      case ForStmt(init, test, step, body) =>
        val t: List[Exception] = test.typeof(scope) match {
          case BoolType(_) => Nil
          case e: ErrorType => e :: new InvalidTestException(ast.pos) :: Nil
          case _ => new InvalidTestException(ast.pos) :: Nil
        }
        t ::: init.map(checkTypes(_)).getOrElse(Nil) ::: step.map(checkTypes(_)).getOrElse(Nil) ::: checkTypes(body)

      case WhileStmt(test, body) =>
        val t: List[Exception] = test.typeof(scope) match {
          case BoolType(_) => Nil
          case e: ErrorType => e :: new InvalidTestException(ast.pos) :: Nil
          case _ => new InvalidTestException(ast.pos) :: Nil
        }
        t ::: checkTypes(body) ::: checkTypes(test)

      case SwitchStmt(what, cases, default, _) =>
        what.map(checkTypes(_)).getOrElse(Nil) ::: cases.flatMap(checkTypes(_)) ::: default.map(checkTypes(_)).getOrElse(Nil)

      case CaseStmt(value,body,_) => body.flatMap(checkTypes(_))
    }
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
   * @param top The program to analyze
   * @return The ScopeTree generated by the analysis
   */
  def analyze(top: Program): (ScopeNode, List[Exception]) = {
    val tree: ScopeNode = new ScopeNode(new ScopeTable, "Global", None, top)
    decorateScope(top, tree)
    val problems = pullDeclsToScope(top) ::: checkTypes(top)
    (top.state.get, problems)
  }

  def compileToSemantic(progn: String): ScopeNode = {
    val r = new DecafSyntactical().parse(progn)
    //System.out.println(r)
    val result = analyze(r)
    result._2.map(System.err.println(_))
    result._1
  }

  def main(args: Array[String]): Unit = {
    println(compileToSemantic("int A; moo A;").toString)
  }
}