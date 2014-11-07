import decaf.frontend.{UndeclaredTypeException, DecafSemantic, DecafSyntactical, ConflictingDeclException}
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
 * Created by hawk on 11/6/14.
 */
class SemanticCheckpointSpec extends FlatSpec with Matchers {

  val parser = new DecafSyntactical
  val target = DecafSemantic

  "checkTypes()" should "detect the conflicting declaration in bad1.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad1.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs should have length 1
    errs(0) shouldBe a [ConflictingDeclException]
    errs(0).getMessage should include ("Declaration of 'b' here conflicts")
  }

  it should "detect the conflicting declaration in bad2.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad2.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs should have length 1
    errs(0) shouldBe a [ConflictingDeclException]
    errs(0).getMessage should include ("Declaration of 'Rutabaga' here conflicts")
  }

  it should "detect the undeclared types in bad3.decaf" in {
    val source = Source fromFile "build/resources/test/lab3-samples/samples-checkpoint/bad3.decaf" mkString
    val (scopes, errs) = target analyze (parser parse source)
    errs should have length 3
    errs(0) shouldBe a [UndeclaredTypeException]
    errs(1) shouldBe a [UndeclaredTypeException]
    errs(2) shouldBe a [UndeclaredTypeException]
  }

}
