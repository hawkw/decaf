package decaf.AST.annotations

import decaf.AST.{ScopeTable, NamedType}
import decaf.frontend.ScopeNode

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
case class InterfaceAnnotation(name: NamedType,
                               interfaceScope: ScopeNode,
                               pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case InterfaceAnnotation(_, _, _) => this == that
    case _ => false
  }
  override def getName = name.name.name
  override def toString = s"Interface: ${name.name.name}"
}
