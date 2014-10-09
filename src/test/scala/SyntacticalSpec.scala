import decaf.frontend.DecafSyntactical
import org.scalatest.{Matchers, FlatSpec}

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

/**
 * Created by hawk on 10/1/14.
 */
class ParserSpec extends FlatSpec with Matchers {

  val target = new DecafSyntactical

  def testOneLiner(source: String) = target.parse(source)

  def ol(source:String) = oluw("void main() { " + source + " }")
  def oluw(source:String) = System.out.println(testOneLiner(source))

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
    System.out.println(ast)
  }

  it should "correctly parse a compound assignment" in {
    val ast = testOneLiner("void main() { int a; a = a + 1; }")
    System.out.println(ast)
  }

  it should "correctly parse a simple while loop" in {
    val ast = testOneLiner("void main() { while (a > 10) { } }")
    System.out.println(ast)
  }

  it should "correctly parse an empty while loop" in {
    val ast = testOneLiner("void main() { while (a > 10); }")
    System.out.println(ast)
  }

  it should "correctly parse an empty if block" in {
    val ast = testOneLiner("void main() { if (a > 10); }")
    System.out.println(ast)
  }

  it should "correctly parse an if statement" in {
      val ast = testOneLiner("void main() { if (1 == 1) break; }")
      System.out.println(ast)
  }

  it should "correctly parse an if statement with break" in {
    val ast = testOneLiner("void main() { if (3 == 5) break; }")
    System.out.println(ast)
  }

  it should "is equality broken?" in {
    val ast = testOneLiner("void main() { a == 5;}")
    System.out.println(ast)
  }

  it should "correctly parse an if statement with a modulo" in {
    val ast = testOneLiner("void main() { if (a % 2 == 0) a = 1; }")
    System.out.println(ast)
  }

  it should "correctly parse an if statement with a bare else" in {
    val ast = testOneLiner("void main() { if (a % 2 == 0) a = 2; else a = 500000000; }")
    System.out.println(ast)
  }

  it should "correctly parse an if statement with a double-bar or" in {
    val ast = testOneLiner("void main() { if (a || b) a = 2; else a = 500000000; }")
    System.out.println(ast)
  }

  it should "correctly parse an empty for loop" in {
    val ast = testOneLiner("void main() { for (; a < 0 ;); }")
    System.out.println(ast)
  }

  it should "correctly parse the for statement from control.decaf" in {
    val ast = testOneLiner("void main() { for (; a <= 10 || done; a = a + 1); }")
    System.out.println(ast)
  }

  it should "correctly parse an empty return statement" in {
    val ast = testOneLiner("void main() { return; }")
    System.out.println(ast)
  }

  it should "correctly parse the bottom part of control.decaf" in {
    val ast = testOneLiner("void main() {   for (; a <= 10 || done; a = a + 1) {\n     Print(a, \" \");\n     if (a == 10) a;\n  }\n  return; }")
    System.out.println(ast)
  }

  it should "correctly parse the top part of control.decaf" in {
    val ast = testOneLiner("void main() {\n  int a;\n  bool done;\n\n  a = 0;\n  while (a < 10) {\n\tPrint(a, \" \");\n\ta = a + 1;\n\tif (a == 5) break;\n  }\n}")
    System.out.println(ast)
  }

  it should "correctly parse the while loop of control.decaf" in {
    val ast = testOneLiner("void main() {  while (a < 10) {\n\tPrint(a, \" \");\n\ta = a + 1;\n\tif (a == 5) break;\n  }\n}")
    System.out.println(ast)
  }
  it should "correctly parse a handful of variable declarations" in {
    val ast = testOneLiner("void main() {\n  int a;\n  bool done;\n\n  a = 0;\n }")
    System.out.println(ast)
  }

  it should "correctly parse a call" in {
    val ast = testOneLiner("void main() { main(); }")
    System.out.println(ast)
  }

  it should "correctly parse a chain of calls" in {
    val ast = testOneLiner("void main() { (thingOne()).thingTwo(); }")
    System.out.println(ast)
  }

  it should "parse a god awful PoS" in {
    val ast = testOneLiner("void main() { (a + b + thing()).q(astNode[3 * q()], dutyCalls, anotherCall()); }")
    System.out.println(ast)
  }

  it should "handle array stuff" in {
    System.out.println(testOneLiner("void main() { a[3].thing.spammy.sammi()[4]; }"))
  }

  it should "handle a simple class definition" in {
    System.out.println(testOneLiner("class Thing { } void main() { a[3]; }"))
  }
  it should "handle a simple class definition and field access" in {
    System.out.println(testOneLiner("class Thing { int a; } void main() { Thing q; }"))
  }

  it should "handle a simple interface definition" in {
    System.out.println(testOneLiner("interface Colorable {\n   Color GetColor();\n   void SetColor(Color c);\n}"))
  }

  it should "handle a this statement" in {
    System.out.println(testOneLiner("void main() { this.red = red; }"))
  }

  it should "handle an assignment to a field access" in {
    System.out.println(testOneLiner("void main() { a.b = c; }"))
  }

  it should "handle an assignment to the result of a field access" in {
    System.out.println(testOneLiner("void main() { c = a.b; }"))
  }


  it should "handle a stupid unary minus" in {
    System.out.println(testOneLiner("void main() { a = a - -b; }"))
  }

  it should "handle an array access in an assignment" in {
    System.out.println(testOneLiner("void main() { a = m[i]; }"))
  }

  it should "handle an assignment to an array access" in {
    System.out.println(testOneLiner("void main() { m[i] = a; }"))
  }

  it should "Trivially use the stdlib" in {
    oluw("void main() { readInteger(); readLine(); }")
  }

  it should "Assign from the stdlib" in {
    System.out.println("void main() { ll = readInteger(); }")
  }

  it should "Assign to an array access from the stdlib" in {
    ol("ll[3] = readInteger();")
  }

  it should "handle this thing" in {
    ol("(b[d]);")
  }

  it should "handle adding some things together" in {
    ol("(b[d]) + d;")
  }

  it should "handle a complex return statement" in {
    ol("return (b[d] + d);")
  }

  it should "Handle the tester fragment from functions.decaf" in {
    oluw("int tester(int d) {\n  return (b[d] + d);\n}")
  }

  it should "recurse with peren-wrapped rexprs correctly" in {
    ol("(a) + (b)")
  }

  it should "recurse with one rexpr on left-hand-side" in {
    ol("(a) + b")
  }

  it should "recurse with one rexpr on right-hand-side" in {
    ol("a + (b)")
  }

  it should "recurse with peren-wrapped rexprs correctly in equality" in {
    ol("(a) == (b)")
  }

  it should "recurse with one rexpr on left-hand-side in equality" in {
    ol("(a) == b")
  }

  it should "recurse with one rexpr on right-hand-side in equality" in {
    ol("a == (b)")
  }
  "The parser" should "correctly parse a simple program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/simple.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/simple.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle control flow" in {
        val source = Source fromFile "build/resources/test/lab2-samples/control.decaf" mkString
        val expected = Source fromFile "build/resources/test/lab2-samples/control.out" mkString
        val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle classes" in {
      val source = Source fromFile "build/resources/test/lab2-samples/class.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/class.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle expressions" in {
      val source = Source fromFile "build/resources/test/lab2-samples/expressions.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/expressions.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle functions" in {
      val source = Source fromFile "build/resources/test/lab2-samples/functions.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/functions.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

    it should "handle inheritance" in {
      val source = Source fromFile "build/resources/test/lab2-samples/inheritance.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/inheritance.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }
/* // Commented out because it uses the  the in-place add one statement ("n++"), which isn't in the Decaf spec
    it should "handle the increment and decrement operators" in {
      val source = Source fromFile "build/resources/test/lab2-samples/incrdecr.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/incrdecr.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }
*/
/* // Commented out because it uses the "switch/case" statement and the in-place add one statement ("n++"),
   // neither of which are in the Decaf spec
    it should "correctly parse a complex program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/switch.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/switch.out" mkString
      val ast = target.parse(source).get

      ast.toString.replaceAll( """(?m)\s+$""", "") should include(expected.replaceAll( """(?m)\s+$""", ""))
    }
*/
    it should "correctly parse a the matrix math program" in {
      val source = Source fromFile "build/resources/test/lab2-samples/matrix.decaf" mkString
      val expected = Source fromFile "build/resources/test/lab2-samples/matrix.out" mkString
      val ast = target.parse(source)

      ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
    }

  it should "correctly parse a program with interfaces" in {
    val source = Source fromFile "build/resources/test/lab2-samples/interface.decaf" mkString
    val expected = Source fromFile "build/resources/test/lab2-samples/interface.out" mkString
    val ast = target.parse(source)

    ast.toString.replaceAll("""(?m)\s+$""", "") should include (expected.replaceAll("""(?m)\s+$""", ""))
  }

}
