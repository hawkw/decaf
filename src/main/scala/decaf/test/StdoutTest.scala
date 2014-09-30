package decaf.test

import decaf.frontend.DecafLexical

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

/**
 * Quick command-line test for the lexer.
 *
 * @deprecated Eventually this should be replaced by a ScalaTest test suite
 *             Created by hawk on 9/28/14.
 */
object StdoutTest extends DecafLexical {
  def main(args: Array[String]) {
    val source = if (args.length > 0) {
      Source.fromFile(args(0)).toArray
    } else {
      Source.fromFile("build/resources/test/lab1-samples/program2.decaf").toArray
    }
    val tokens = program(new CharArrayReader(source, 0)).get
    tokens.filter(!_.isInstanceOf[Character]).foreach(t => println(t.toString))
  }

}