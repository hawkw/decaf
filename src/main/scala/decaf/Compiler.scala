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
  args.length match {
    case 0 => println("Please enter a Decaf source code file to compile.")
    case 1 =>
      val source = Source.fromFile(args(0)).mkString
      val ast = parser.parse(source)
      val (scopes, errors) = DecafSemantic.analyze(ast)
      errors.foreach(System.err.println(_))
    case _ => println("Too many arguments!")
  }

}