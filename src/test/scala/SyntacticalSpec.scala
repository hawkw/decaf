import decaf.frontend.DecafSyntactical
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

/**
 * Created by hawk on 10/1/14.
 */
class ParserSpec extends FlatSpec with Matchers {

  val target = new DecafSyntactical

  "The parser" should "correctly parse a single statement" in {
    val source= "sigv[][] a;"
    val ast = target.parse(source).get
    //System.out.println(ast);
  }

  it should "correctly parse a single function def" in {
    val source = "void main() { }"
    val ast = target.parse(source).get
    //System.out.println(ast);
  }

  it should "correctly parse a complex statement" in {
    val source = "void main() { a()[b]; }"
    val ast = target.parse(source).get
    System.out.println(ast);
  }

    "The parser" should "correctly parse a simple program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/simple.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/simple.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle control flow" in {
        val source = Source fromFile "build/resources/test/lab2-samples/control.decaf" mkString
        val expected = Source fromFile "build/resources/test/lab2-samples/control.out" mkString
        val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle classes" in {
      val source = Source fromFile "build/resources/test/lab2-samples/class.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/class.out" mkString
      val ast = target.parse(source).get

      ast.toString should include (expected)
    }

    it should "handle expressions" in {
      val source = Source fromFile "build/resources/test/lab2-samples/expressions.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/expressions.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle functions" in {
      val source = Source fromFile "build/resources/test/lab2-samples/functions.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/functions.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle inheritance" in {
      val source = Source fromFile "build/resources/test/lab2-samples/inheritance.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/inheritance.out" mkString
      val ast = target.parse(source).get

      ast.toString should include (expected)
    }

    it should "handle the increment and decrement operators" in {
      val source = Source fromFile "build/resources/test/lab2-samples/incrdecr.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/incrdecr.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }


    it should "correctly parse a complex program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/switch.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/switch.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll( """(?m)\s+$""", "") should include(expected.replaceAll( """(?m)\s+$""", ""))
    }

    it should "correctly parse a the matrix math program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/matrix.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/matrix.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

}
