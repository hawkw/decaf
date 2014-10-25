package decaf.frontend


/**
 * Created by hawk on 10/25/14.
 */

trait DecafSemantic extends DecafAST {
  class SemanticException extends Exception
  type ScopeTable = ForkTable[ASTIdentifier, TypeAnnotation]
  type Pending = (State, SemanticException)
  case class TypeAnnotation(val node: ASTNode, val typ: Type)
  case class State(val location: List[ASTNode], val scopeTable: ScopeTable)
}