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

import java.nio.file.{Files, Paths}

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
/*
object Compiler extends App {
  val parser = new DecafSyntactical
  val (source: String, fileName: Option[String]) = args match {
    case Array() =>
      (Source.fromInputStream(System.in).mkString, None)  // should detect EOF automagically?
    case Array(path: String, _*) =>
      (Source
        .fromFile(path)
        .mkString,
        Some(path
          .split('/')
          .last
          .split('.')
          .head)
        )
  }
  lazy val ast = parser.parse(source)
  lazy val (scopes, errors) = DecafSemantic.analyze(ast)
  errors match {
    case Nil => println(JasminBackend.compile(ast, fileName))
    case errors: List[Exception] => errors.foreach(System.err.println(_))
  }
*/
object Compiler {
  protected val parser = new DecafSyntactical
  protected val optParser = new scopt.OptionParser[Config]("dcc") {
    head("dcc", "0.1")
    arg[String]("<source>") required() action { (x, c) =>
      c.copy(path = x)
    } validate { x =>
      val path = Paths get x
      if (Files.exists(path) && Files.isReadable(path)) success
      else failure("Please enter a Decaf source code file to compile.")
    } text "Decaf source code file to compile"
    opt[Unit]('a', "ast") action { (_, c) =>
      c.copy(astMode = true)
    } text "Print the AST rather than compiling"
    help("help") text "prints this usage text"
  }

  def main(args: Array[String]): Unit = optParser parse(args, Config()) match {
    case Some(conf: Config) =>
      val (source: String, fileName: Option[String]) = (
        Source
          .fromFile(conf.path)
          .mkString,
        Some(
          conf
            .path
            .split('/')
            .last
            .split('.')
            .head
          )
        )
      lazy val ast = parser.parse(source)
      conf match {
        case Config(_,true) =>
          println(ast)
        case Config(_,false) =>
          lazy val (scopes, errors) = DecafSemantic.analyze(ast)
          errors match {
            case Nil => println(JasminBackend.compile(ast, fileName))
            case errors: List[Exception] => errors.foreach(System.err.println(_))
          }
      }
    case None => System.exit(1)
  }
}
case class Config(
                   path: String = ".",
                   astMode: Boolean = false
                   )