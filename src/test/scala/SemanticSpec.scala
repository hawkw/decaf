import decaf.frontend.{DecafSemantic, DecafSyntactical, ConflictingDeclException}
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

}
