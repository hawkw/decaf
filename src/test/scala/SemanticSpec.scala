import decaf.frontend._
import org.scalatest.{FlatSpec, Matchers}

import language.postfixOps

import scala.io.Source

/**
 * Spec for semantic analysis based on the checkpoint samples.
 *
 * NOTE that this is not finished since we now actually generate
 * more errors than are included in the samples.
 *
 * Created by hawk on 11/6/14
 */
class SemanticCheckpointSpec extends FlatSpec with Matchers {

  val parser = new DecafSyntactical
  val target = DecafSemantic

  "checkTypes()" should "detect the conflicting declaration in bad1.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad1.decaf" mkString
    val ast = parser parse source
    val (scopes, errs) = target analyze ast
    errs should have length 1
    errs(0) shouldBe a [ConflictingDeclException]
    errs(0).getMessage should include ("Declaration of 'b' here conflicts")
  }

  it should "detect the conflicting declaration in bad2.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad2.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs(0) shouldBe a [ConflictingDeclException]
    errs(0).getMessage should include ("Declaration of 'Rutabaga' here conflicts")
  }
/*
  it should "detect the undeclared types in bad3.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad3.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs should have length 4
    errs(0) shouldBe an [UndeclaredTypeException]
    errs(1) shouldBe an [UndeclaredTypeException]
    errs(2) shouldBe an [UnimplementedInterfaceException]
    errs(3) shouldBe an [UndeclaredTypeException]
  }
*/
}

/**
 * Spec for semantic analysis based on the final deadline samples.
 *
 * Note that this does not expect the errors that we do not generate.
 */
class SemanticFinalSpec extends FlatSpec with Matchers {
  val parser = new DecafSyntactical
  val target = DecafSemantic

  /**
   * Helper method so I don't have to type the boilerplate every time.
   * @param name
   */
  private def analyze(name: String): (ScopeNode, List[Exception]) =
    target analyze (
      parser parse (
        Source fromFile s"build/resources/test/lab3-samples/samples/$name" mkString
      )
    )

  "The semantic analyzer" should "detect no errors in bad1.decaf" in {
    val (scopes, errs) = analyze("bad1.decaf")
    // ignore the error JJ wants to see here
    // we decided that type lifting int -> double is okay
    errs should have length 0
  }

  it should "detect the errors in bad2.decaf" in {
    val (scopes, errs) = analyze("bad2.decaf")
    errs should have length 2
    errs(0) shouldBe a [TypeErrorException]
    errs(0).getMessage should include("Incompatible operands: int = bool")
    errs(1) shouldBe a [TypeErrorException]
    // we generate a slightly different error here because type lifting.
    // the divide is okay (it becomes double) but assigning the result to
    // a variable of type int is Not Okay
    errs(1).getMessage should include("Incompatible operands: int = double")
  }

  it should "detect the errors in bad3.decaf" in {
    val (scopes, errs) = analyze("bad3.decaf")
    errs should have length 4
    errs(0) shouldBe a [TypeErrorException]
    errs(0).getMessage should include ("[ ] can only be applied to arrays")
    errs(1) shouldBe a [TypeErrorException]
    errs(1).getMessage should include ("Incompatible operands: double[] = Binky[]")
    errs(2) shouldBe a [TypeErrorException]
    errs(2).getMessage should include ("Array subscript must be an integer")
    errs(3) shouldBe a [TypeErrorException]
    errs(3).getMessage should include ("Size for NewArray must be an integer")
  }

  it should "detect the errors in bad4.decaf" in {
    val (scopes, errs) = analyze("bad4.decaf")
    errs should have length 2 // we are skipping the encapsulation one since we don't do that
    errs(0) shouldBe a [TypeErrorException]
    errs(0).getMessage should include ("*** 'this' is only valid within class scope")
    errs(1) shouldBe a [TypeErrorException]
    errs(1).getMessage should include ("*** int[] has no such field 'color'")
  }

  it should "detect the errors in bad5.decaf" in {
    val (scopes, errs) = analyze("bad5.decaf")
    errs should have length 7
    errs(0).getMessage should include ("No declaration found for function 'FindTime'")
    errs(1).getMessage should include ("No declaration found for variable 'nico'")
    errs(2).getMessage should include ("No declaration found for variable 'trish'")
    errs(3).getMessage should include ("No declaration found for variable 'katie'")
    errs(4).getMessage should include ("No declaration found for function 'cheryl'")
    errs(5).getMessage should include ("No declaration found for variable 'caroline'")
    errs(6).getMessage should include ("Club has no such field 'Meeting'")
  }

  it should "detect the errors in bad6.decaf" in {
    val (scopes, errs) = analyze("bad6.decaf")
    errs should have length 5
    errs(0).getMessage should include ("Incompatible operand: bool && int")
    errs(1).getMessage should include ("break is only allowed inside a loop")
    errs(2).getMessage should include ("Incompatible operand: ! int")
    errs(3).getMessage should include ("Test expression must have boolean type")
    errs(4).getMessage should include ("Incompatible return : int given, void expected")
  }

  it should "detect the errors in bad7.decaf" in {
    val (scopes, errs) = analyze("bad7.decaf")
    errs should have length 3 // we are skipping the last error due to type lifting
    errs(0).getMessage should include ("Incompatible argument 3: int given, bool expected")
    errs(1).getMessage should include ("Function 'Binky' expects 3 arguments but 1 given")
    errs(2).getMessage should include ("Incompatible operands: double = int")
  }
  it should "detect the errors in bad8.decaf" in {
    val (scopes, errs) = analyze("bad8.decaf")
    errs should have length 4 // we are skipping the first error due to encapsulation
    errs(0).getMessage should include ("Honda has no such field 'horn'")
    errs(1).getMessage should include ("Honda has no such field 'Honk'")
    errs(2).getMessage should include ("Incompatible operands: Boat < Honda")
    errs(3).getMessage should include ("Incompatible operands: Honda = bool")
  }
  it should "detect the errors in bad9.decaf" in {
    val (scopes, errs) = analyze("bad9.decaf")
    errs should have length 5
    errs(0).getMessage should include ("Incompatible operands: int = string")
    errs(1).getMessage should include ("Incompatible argument 1: double given, int/bool/string expected")
    errs(2).getMessage should include ("Incompatible argument 3: double given, int/bool/string expected")
    errs(3).getMessage should include ("No declaration found for variable 'main'")
    errs(4).getMessage should include ("int has no such field 'length'")
  }
  it should "detect the errors in bad10.decaf" in {
    val (scopes, errs) = analyze("bad10.decaf")
    errs should have length 4
    errs(0).getMessage should include("No declaration found for type 'Unknown'")
    errs(1).getMessage should include("No declaration found for variable 'var'")
    errs(2).getMessage should include("No declaration found for class 'Missing'")
    // seems to me that it's correct to make this error here
    errs(3).getMessage should include("Unknown has no such field 'GetColor'")
  }
  it should "detect the errors in bad11.decaf" in {
    val (scopes, errs) = analyze("bad11.decaf")
    errs should have length 3
    errs(0).getMessage should include("Method 'ToString' must match inherited type signature")
    errs(1).getMessage should include("Class 'Number' does not implement entire interface 'Printable'")
    errs(2).getMessage should include("Incompatible operands: Number = Printable")
  }
  it should "detect the errors in bad12.decaf" in {
    val (scopes, errs) = analyze("bad12.decaf")
    errs should have length 8
    errs(0).getMessage should include ("No declaration found for class 'Animal'")
    errs(1).getMessage should include ("No declaration found for interface 'Trick'")
    errs(2).getMessage should include ("No declaration found for interface 'Halloween'")
    errs(3).getMessage should include ("No declaration found for type 'B'")
    errs(4).getMessage should include ("No declaration found for type 'Trick'")
    errs(5).getMessage should include ("No declaration found for class 'A'")
    errs(6).getMessage should include ("No declaration found for class 'Treat'")
    errs(7).getMessage should include ("No declaration found for type 'Trick'")
  }
  it should "detect the error in bad13.decaf" in {
    val (scopes, errs) = analyze("bad13.decaf")
    errs should have length 1
    errs(0).getMessage should include ("Incompatible operands: void == void")

  }
}