package decaf.AST.annotations

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
abstract class TypeAnnotation(where: Position) {
  def matches(that: TypeAnnotation): Boolean
}
