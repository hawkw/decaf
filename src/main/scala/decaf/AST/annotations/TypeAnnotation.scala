package decaf.AST.annotations

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
abstract class TypeAnnotation(val where: Position) {
  def getName: String = ???
  def matches(that: TypeAnnotation): Boolean
}
