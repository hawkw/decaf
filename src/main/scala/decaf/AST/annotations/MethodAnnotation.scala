package decaf.AST.annotations

import decaf.AST.Type

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
case class MethodAnnotation(override val getName: String,
                            returnType: Type,
                            formals: List[Type],
                            pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case MethodAnnotation(_, rt, f, _) => rt == returnType && f == formals
    case _ => false
  }
  override def toString = s"Method: ${returnType.typeName} -> (" +
    formals.map(_.typeName).mkString(",") + ")"
}
