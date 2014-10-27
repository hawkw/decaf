package decaf.frontend
import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Created by hawk on 10/26/14.
 */
case class SemanticException(message: String, pos: Position) extends Exception(message) {
  lazy val lineOfCode = "" // TODO : go get the actual line of code from the parser
  // def toString() // TODO: Issue
}
class ConflictingDeclException(name: String, where: Position)
  extends SemanticException(s"*** Declaration of ‘$name’ here conflicts with declaration on line ${where.line}}", where)
class TypeSignatureException(name: String, where: Position)
  extends SemanticException(s"** Method ’$name’ must match inherited type signature", where)
object DecafSemantic extends DecafAST {

  def decorateScope (tree: ASTNode, scope: ScopeNode): Unit = {
    tree.state = Some(scope);
    tree match {
      case Program(decls) => decls.foreach {
        _ match {
          case f: FnDecl => decorateScope(f, scope)
          case v: VarDecl => decorateScope(v, scope)
          case c: ClassDecl => decorateScope(c, scope.child(s"Class Declaration of ${c.name.name}", c))
          case i: InterfaceDecl => decorateScope(i, scope.child(s"Interface Declaration of ${i.name.name}", i))
        }
      }
      case c: ClassDecl => {
        c.members.foreach {
          _ match {
            case f: FnDecl => decorateScope(f, scope)
            case _ => //I don't think we fork scope for variable decls in a class? And class decls can't be embedded.
          }
        }
      }
      case i: InterfaceDecl => i.members.foreach {
        _ match {
          case f: FnDecl => decorateScope(f, scope)
          case _ => //We shouldn't have any other types of decl in an interface. If we do, then we have a problem.
        }
      }
      case f: FnDecl => {
        var s = scope.child(s"FnDecl (formals) ${f.name.name}", f)
        f.formals.foreach {
          decorateScope(_, s)
        }
        if(f.body.isDefined) {
          decorateScope(f.body.get, s.child(s"FnDecl (body) ${f.name.name}", f))
        }
      }
      case StmtBlock(decls, stmts) => {
        var s = scope.child("StmtBlock",tree)
        decls.foreach {
          decorateScope(_, s)
        }
        stmts.foreach {
          decorateScope(_, s)
        }
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

  def annotateVariable(state: ScopeNode, v: VarDecl) = {
    var ident = v.n
    var typ = v.t
    if (state.table.contains(ident.name)) {
      throw new ConflictingDeclException(ident.name, ident.pos)
    } else {
      state.table.put(ident.name, new VariableAnnotation(typ))
    }
  }

  def annotateFunction(state: ScopeNode, fn: FnDecl) = {
    var ident = fn.name
    var rettype = fn.returnType
    var formals = fn.formals
    if(state.table.contains(ident.name)) {
      throw new ConflictingDeclException(ident.name, ident.pos)
    } else {
      state.table.put(ident.name, new MethodAnnotation(rettype, formals.map(_.t)))
    }
    for (formal <- formals) {
      if(formal.state.isEmpty) {
        throw new IllegalArgumentException("Tree didn't contain a scope for\n" + formal.toString + "\nin " + fn.toString)
      }
      annotateVariable(formal.state.get, formal)
    }
    if(fn.body.isDefined) {
      var body = fn.body.get
      if (body.state.isEmpty) {
        throw new IllegalArgumentException("Tree didn't contain a scope for\n" + body.toString + "\nin " + fn.toString)
      } else {
        var bstate = body.state.get
        for (decl <- body.decls) {
          annotateVariable(bstate, decl)
        }
      }
    }
  }

  def annotateClass(state: ScopeNode, c: ClassDecl) = {
    if (c.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope for\n" + c.toString)
    }
    var cscope = c.state.get
    if(cscope.table.contains("this")) {
      throw new IllegalArgumentException("keyword \'this\' already (accidentally?) bound for class scope in " + c.toString)
    } else {
      cscope.table.put("this", new VariableAnnotation(NamedType(c.name)))
    }

    for(member <- c.members) {
      member match {
        case v: VarDecl => annotateVariable(cscope, v)
        case f: FnDecl => annotateFunction(cscope, f)
      }
    }

    var pscope = c.state.get.parent
    if(pscope.isEmpty) {
      throw new IllegalArgumentException("Tree doesn't have a parent scope to enclose class type declaration in class " + c.toString)
    } else {
      var ptable = pscope.get.table
      if(ptable.contains(c.name.name)) {
        throw new ConflictingDeclException(c.name.name, c.name.loc.get)
      } else {
        ptable.put(c.name.name, new ClassAnnotation(new NamedType(c.name), c.extnds, c.implements, cscope.table))
      }
    }
    //TODO: Finish me
  }

  def annotateInterface(node: DecafSemantic.ScopeNode, decl: DecafSemantic.InterfaceDecl) = {
  }

  def pullDeclsToScope (tree: ASTNode): Unit = {
    if (tree.state.isEmpty) {
      throw new IllegalArgumentException("Tree didn't contain a scope at " + tree.toString)
    }
    var state = tree.state.get
    tree match {
      case Program(decls) => for(decl <- decls) {
        decl match {
          case v: VarDecl => annotateVariable(state, v)
          case f: FnDecl => annotateFunction(state, f)
          case c: ClassDecl => annotateClass(state, c)
          case i: InterfaceDecl => annotateInterface(state, i)
        }
      }
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
   * @param top
   * @return
   */
  def analyze(top: Program): ScopeNode = {
    var continue = true
    var tree: ScopeNode = new ScopeNode(new ScopeTable, "Global", None, top)
    decorateScope(top, tree)
    pullDeclsToScope(top)
    top.state.get
  }

  def compileToSemantic(progn: String): ScopeNode = {
    analyze(new DecafSyntactical().parse(progn).asInstanceOf[Program])
  }

  def main(args: Array[String]): Unit = {
    System.out.println(compileToSemantic("class cow implements farts { int a; void moo(String how) { } } interface farts {} \nvoid main(String[] args) { cow a; a = b; int b; }").toString);
  }
}