package decaf.AST.annotations

import decaf.AST.{ScopeTable, NamedType}
import decaf.frontend.ScopeNode

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
case class ClassAnnotation(name: NamedType,
                           ext: Option[NamedType],
                           implements: List[NamedType],
                           classScope: ScopeNode,
                           pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    // matches if the that is equal to this
    // WARNING WARNING WARNING
    // Had to remove superclass checking from
    // this function for Type reasons.
    // Will have to implement the superclass
    // check somewhere else, based on ScopeNode
    // rather than by chaining ClassAnnotations.
    // WARNING WARNING WARNING
    case ClassAnnotation(_, e,i,m, _) => this == that
    case _ => false
  }
  override def getName = name.name.name
  override def toString = s"Class: ${name.name.name}"
}
