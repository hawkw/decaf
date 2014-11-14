import decaf.frontend._
import org.scalatest.{FlatSpec, Matchers}

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

  it should "detect the undeclared types in bad3.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad3.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs should have length 4
    errs(0) shouldBe an [UndeclaredTypeException]
    errs(1) shouldBe an [UndeclaredTypeException]
    errs(2) shouldBe an [UnimplementedInterfaceException]
    errs(3) shouldBe an [UndeclaredTypeException]
  }

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
    errs(0).getMessage should include("[ ] can only be applied to arrays")
    errs(1) shouldBe a [TypeErrorException]
    errs(1).getMessage should include("Incompatible operands: double[] = Binky[]")
    errs(2) shouldBe a [TypeErrorException]
    errs(2).getMessage should include("Array subscript must be an integer")
    errs(3) shouldBe a [TypeErrorException]
    errs(3).getMessage should include("Size for NewArray must be an integer")
  }

  it should "detect the errors in bad4.decaf" in {
    val (scopes, errs) = analyze("bad4.decaf")
    errs should have length 2 // we are skipping the encapsulation one since we don't do that
    errs(0) shouldBe a [TypeErrorException]
    errs(0).getMessage should include("*** 'this' is only valid within class scope")
    errs(1) shouldBe a [TypeErrorException]
    errs(1).getMessage should include("*** int[] has no such field 'color'")
  }

}
