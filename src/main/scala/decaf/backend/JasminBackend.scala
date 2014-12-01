package decaf.backend

import decaf.AST._

/**
 * Created by hawk on 12/1/14.
 */
object JasminBackend {

  def emit(node: ASTNode): String = node match {
    case Program(decls, _) => decls.reduceLeft((acc, decl) => acc + emit(decl))
      // TODO: everthing in between
    case _ => println(s"ignored $node"); ""

  }

}
