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

  }

  def analyze(top: Program) = {
    var continue = true
    var tree: ScopeNode = new ScopeNode(new ScopeTable)
    decorateScope(top, tree)
    do {
      var currentNode = top
      var thingy = false
      do {

      } while(thingy)

    } while (continue)
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

}
