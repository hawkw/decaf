package decaf
import decaf.frontend.{DecafSyntactical, DecafSemantic}
import scala.io.Source

/**
 * Quick driver class for command-line invocation of the compiler.
 *
 * @author Hawk Weisman
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

  errors.foreach(System.err.println(_))

}