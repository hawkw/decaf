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
      case Program(decls) => decls.foreach { d =>
        decorateScope(d, scope.child(d))
      }
      case ClassDecl(_, _, _, members) => members.foreach { m =>
        decorateScope(m, scope.child(m))
      }
      case InterfaceDecl(_, members) => members.foreach { i =>
        decorateScope(i, scope.child(i))
      }
      case FnDecl(_, _, formals, Some(body)) => {
        var s = scope.child(tree)
        formals.foreach {
          decorateScope(_, s)
        }
        decorateScope(body, s.child(tree))
      }
      case FnDecl(_, _, formals, None) => {
        var s = scope.child(tree)
        formals.foreach {
          decorateScope(_, s)
        }
      }
      case StmtBlock(decls, stmts) => {
        var s = scope.child(tree)
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
    var cscope = c.state.get.table
    if(cscope.contains("this")) {
      throw new IllegalArgumentException("keyword \'this\' already (accidentally?) bound for class scope in " + c.toString)
    } else {
      cscope.put("this", new VariableAnnotation(NamedType(c.name)))
    }
    //TODO: Finish me
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
    var tree: ScopeNode = new ScopeNode(new ScopeTable, None, top)
    decorateScope(top, tree)
    pullDeclsToScope(top)
    top.state.get
  }

  def compileToSemantic(progn: String): ScopeNode = {
    analyze(new DecafSyntactical().parse(progn).asInstanceOf[Program])
  }

  def main(args: Array[String]): Unit = {
    System.out.println(compileToSemantic("class cow {} \nvoid main() { cow a; a = b; int b; }").toString);
  }
}