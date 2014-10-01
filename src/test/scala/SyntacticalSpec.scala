import org.scalatest.{Matchers, FlatSpec}
import decaf.frontend.DecafParser

import scala.io.Source

/**
 * Created by hawk on 10/1/14.
 */
class ParserSpec extends FlatSpec with Matchers {

  "The parser" should "correctly parse a simple program" in {
    val source = Source fromFile "build/resources/test/lab2-samples/simple.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/simple.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "handle control flow" in {
      val source = Source fromFile "build/resources/test/lab2-samples/control.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/control.out" mkString
      val ast = DecafParser.parseAll(source).get

      ast.toString should include (expected)
  }

  it should "handle classes" in {
    val source = Source fromFile "build/resources/test/lab2-samples/class.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/class.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "handle expressions" in {
    val source = Source fromFile "build/resources/test/lab2-samples/expressions.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/expressions.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "handle functions" in {
    val source = Source fromFile "build/resources/test/lab2-samples/functions.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/functions.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "handle inheritance" in {
    val source = Source fromFile "build/resources/test/lab2-samples/inheritance.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/inheritance.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "handle the increment and decrement operators" in {
    val source = Source fromFile "build/resources/test/lab2-samples/incrdecr.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/incrdecr.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }


  it should "correctly parse a complex program" in {
    val source = Source fromFile "build/resources/test/lab2-samples/switch.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/switch.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

  it should "correctly parse a the matrix math program" in {
    val source = Source fromFile "build/resources/test/lab2-samples/matrix.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/matrix.out" mkString
    val ast = DecafParser.parseAll(source).get

    ast.toString should include (expected)
  }

}
