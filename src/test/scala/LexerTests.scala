/**
 * Created by hawk on 9/28/14.
 */

import decaf.frontend.DecafLexical
import org.scalatest._

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

class LexerSamplesSpec extends FlatSpec with Matchers {

  val target = new DecafLexical

  "A Lexer" should "parse program2.decaf" in {
    val program2 = Source fromFile "build/resources/test/lab1-samples/program2.decaf" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/program2.out" getLines
    val tokens = target.program(new CharArrayReader(program2, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "parse program3.decaf" in {
    val program2 = Source fromFile "build/resources/test/lab1-samples/program3.decaf" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/program3.out" getLines
    val tokens = target.program(new CharArrayReader(program2, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }
}