/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */

package decaf
import decaf.frontend.{DecafSyntactical, DecafSemantic}
import decaf.backend.JasminBackend
import scala.io.Source

/**
 * Quick driver class for command-line invocation of the compiler.
 *
 * @author Hawk Weisman <hawk@meteorcodelabs.com>
 * @author Max Clive <mattrulz127@gmail.com>
 *
 * Created by hawk on 10/10/14.
 */
object Compiler extends App {
  val parser = new DecafSyntactical
  val source: String = args match {
    case Array() =>
      Source.fromInputStream(System.in).mkString // should detect EOF automagically?
    case Array(path: String, _*) =>
      Source.fromFile(path).mkString
  }
  lazy val ast = parser.parse(source)
  lazy val (scopes, errors) = DecafSemantic.analyze(ast)
  errors match {
    case Nil => println(JasminBackend.compile(ast))
    case errors: List[Exception] => errors.foreach(System.err.println(_))
  }


}