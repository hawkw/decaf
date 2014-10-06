import decaf.frontend.DecafSyntactical
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

/**
 * Created by hawk on 10/1/14.
 */
class ParserSpec extends FlatSpec with Matchers {

  val target = new DecafSyntactical

  def testOneLiner(source: String) = target.parse(source).get

  "The parser" should "correctly parse a single statement" in {
    val ast = testOneLiner("sigv[][] a;")
    //System.out.println(ast);
  }

  it should "correctly parse a single function def" in {
    val ast = testOneLiner("void main() { }")
    //System.out.println(ast);
  }

  it should "correctly parse a complex statement" in {
    val ast = testOneLiner("void main() { lval = 'q'; }")
    System.out.println(ast);
  }

  it should "correctly parse a compound assignment" in {
    val ast = testOneLiner("void main() { int a; a = a + 1; }")
    System.out.println(ast);
  }

  it should "correctly parse a simple while loop" in {
    val ast = testOneLiner("void main() { while (a > 10) { } }")
    System.out.println(ast);
  }

  it should "correctly parse an empty while loop" in {
    val ast = testOneLiner("void main() { while (a > 10); }")
    System.out.println(ast);
  }

  it should "correctly parse an empty if block" in {
    val ast = testOneLiner("void main() { if (a > 10); }")
    System.out.println(ast);
  }

    it should "correctly parse an if statement" in {
      val ast = testOneLiner("void main() { if (1 == 1) { } }")
      System.out.println(ast);
  }

  it should "correctly parse an if statement with break" in {
    val ast = testOneLiner("void main() { if (a == 5) break; }")
    System.out.println(ast);
  }

  it should "correctly parse an if statement with a modulo" in {
    val ast = testOneLiner("void main() { if (a % 2 == 0) a = 1; }")
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
