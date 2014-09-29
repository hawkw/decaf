import decaf.frontend.{DecafTokens, DecafLexical}

import scala.io.Source
import scala.util.parsing.input.CharArrayReader
import scala.util.parsing.input.CharArrayReader.EofCh

/**
 * Created by hawk on 9/28/14.
 */
object StdoutTest extends DecafLexical {

  def program: Parser[List[Any]] = rep(token | whitespaceChar)

  def main(args: Array[String]) {
    val program2 = Source.fromFile("build/resources/test/lab1-samples/program2.decaf").toArray
    val tokens = program(new CharArrayReader(program2, 0)).get
    tokens.filter(!_.isInstanceOf[Character]).foreach(t => println(t.toString))
  }

}
