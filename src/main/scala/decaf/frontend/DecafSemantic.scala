package decaf.frontend
import scala.util.parsing.input.{NoPosition, Positional, Position}

/**
 * Created by hawk on 10/26/14.
 */
case class SemanticException(message: String, pos: Position) extends Exception(message) {
  lazy val lineOfCode = "" // TODO : go get the actual line of code from the parser
  // def toString() // TODO: Issue
}
class DecafSemantic extends DecafAST {



}
