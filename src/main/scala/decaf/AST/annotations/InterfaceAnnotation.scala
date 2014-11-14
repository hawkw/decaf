package decaf.AST.annotations

import decaf.AST.{ScopeTable, NamedType}

import scala.util.parsing.input.Position

/**
 * Created by hawk on 11/14/14.
 */
case class InterfaceAnnotation(name: NamedType,
                               interfaceScope: ScopeTable,
                               pos: Position) extends TypeAnnotation(pos) {
  override def matches(that: TypeAnnotation): Boolean = that match {
    case InterfaceAnnotation(_, _, _) => this == that
    case _ => false
  }

  override def toString = s"Interface: ${name.name.name}"
}
