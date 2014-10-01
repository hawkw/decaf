/**
 * Created by hawk on 9/28/14.
 */

import decaf.frontend.DecafLexical
import org.scalatest._

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

class LexicalSamplesSpec extends FlatSpec with Matchers {

  val target = new DecafLexical

  "A Lexical Parser" should "tokenize program2.decaf" in {
    val source = Source fromFile "build/resources/test/lab1-samples/program2.decaf" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/program2.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "tokenize program3.decaf" in {
    val source = Source fromFile "build/resources/test/lab1-samples/program3.decaf" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/program3.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "handle comments correctly" in {
    val source = Source fromFile "build/resources/test/lab1-samples/comment.frag" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/comment.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "handle diverse identifiers" in {
    val source = Source fromFile "build/resources/test/lab1-samples/ident.frag" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/ident.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "handle many types of numbers" in {
    val source = Source fromFile "build/resources/test/lab1-samples/number.frag" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/number.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "tokenize reserved words and operators" in {
    val source = Source fromFile "build/resources/test/lab1-samples/reserve_op.frag" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/reserve_op.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

  it should "handle strings" in {
    val source = Source fromFile "build/resources/test/lab1-samples/string.frag" toArray
    val expected = Source fromFile "build/resources/test/lab1-samples/string.out" getLines
    val tokens = target.program(new CharArrayReader(source, 0)).get.toIterator

    for (line <- expected) {
      line should include(tokens.next.toString)
    }
  }

}