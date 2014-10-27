package decaf.frontend
import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Created by hawk on 10/26/14.
 */
case class SemanticException(message: String, pos: Position) extends Exception(message) {
  lazy val lineOfCode = "" // TODO : go get the actual line of code from the parser
  // def toString() // TODO: Issue
}
object DecafSemantic extends DecafAST {

  def decorateScope (tree: ASTNode, scope: ScopeNode): Unit = {
    tree.state = Some(scope);
    tree match {
      case Program(decls) => decls.foreach { decorateScope(_, scope.child) }
      case ClassDecl(_, _, _, members) => members.foreach {decorateScope(_, scope.child)}
      case InterfaceDecl(_, members) => members.foreach {decorateScope(_, scope.child)}
      case FnDecl(_,_,formals,Some(body)) => {
        var s = scope.child
        formals.foreach {decorateScope(_, s)}
        decorateScope(body, s)
      }
      case FnDecl(_,_,formals,None) => {
        var s = scope.child
        formals.foreach {
          decorateScope(_, s)
        }
      }
      case StmtBlock(decls, stmts) => {
        var s = scope.child
        decls.foreach { decorateScope(_, s) }
        stmts.foreach { decorateScope(_, s) }
      }
      case n: ASTNode => n.state = Some(scope)
    }

    def descent(node: ASTNode): List[ASTNode] = {
      node match {
        case Program(decls) => decls
        case ClassDecl(_, _, _, members) => members
        case FnDecl(_,_,formals,Some(body)) => formals.asInstanceOf[List[ASTNode]] ::: body.asInstanceOf[ASTNode] :: Nil
        case StmtBlock(decls, stmts) => {
          decls.asInstanceOf[List[ASTNode]] ::: stmts.asInstanceOf[List[ASTNode]]
        }
        case _ => List[ASTNode]()
      }
    }

  def pullDeclsToScope (tree: ASTNode): Unit = {
    tree match {
      case Program(decls) => for(var decl <- decls) {

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
  def analyze(top: Program) = {
   var continue = true
   var tree: ScopeNode = new ScopeNode(new ScopeTable)
   decorateScope(top, tree)
   do {
     var currentNode = top
     var thingy = false
     do {
       // todo: on entering a class scope, bind "this" in it's own symbol table to itself
       // todo: on enterign a class scope that extends another class, bind all fields of parent into child's scopetable
       // todo: also do this for method signatures in implements and extends
       // todo: when we get a variable declaration, make sure that the type exists
     } while (thingy)

   } while (continue)
  }

}
