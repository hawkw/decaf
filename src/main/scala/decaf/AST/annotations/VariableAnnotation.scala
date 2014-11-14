package decaf.AST.annotations

import decaf.AST.Type

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
case class VariableAnnotation(t: Type, pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case VariableAnnotation(typ, _) => typ == t
    case _ => false
  }
  override def toString = s"Variable of ${t.typeName}"
}
