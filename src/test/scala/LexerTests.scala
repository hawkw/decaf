/**
 * Created by hawk on 9/28/14.
 */

import decaf.frontend._
import org.scalatest._

import scala.io.Source
import scala.util.parsing.input.CharArrayReader

/**
 * This is just a test [[decaf.frontend.DecafLexical D e c a f L e x i c a l]] that parses a whole program into a list of
 * [[DecafTokens]]. Whitespace is ignored.
 * @author Hawk Weisman
 */
object TestLexer extends DecafLexical {

  def program: Parser[List[Any]] = rep(token | whitespaceChar) ^^ {
    case tokens => tokens.filter(!_.isInstanceOf[Character])
  }

}

class LexerSamplesSpec extends FlatSpec with Matchers with DecafTokens {

  val program2 = Source.fromFile("build/resources/test/lab1-samples/program2.decaf").toArray

  "The Lexer" should "parse program2.decaf" in {
    val tokens = TestLexer.program(new CharArrayReader(program2, 0)).get
    tokens(0) shouldBe a[Keyword]
    tokens(0).toString should include(": int")

    tokens(1) shouldBe a[Identifier]
    tokens(1).toString should include(": a")

    tokens(2) shouldBe a[Delimiter]
    tokens(2).toString should include(": ;")

    tokens(3) shouldBe a[Keyword]
    tokens(3).toString should include(": void")

    tokens(4) shouldBe a[Identifier]
    tokens(4).toString should include(": main")

    tokens(5) shouldBe a[Delimiter]
    tokens(5).toString should include(": (")

    tokens(6) shouldBe a[Delimiter]
    tokens(6).toString should include(": )")

    tokens(7) shouldBe a[Delimiter]
    tokens(7).toString should include(": {")

    tokens(8) shouldBe a[Keyword]
    tokens(8).toString should include(": int")

    tokens(9) shouldBe a[Identifier]
    tokens(9).toString should include(": b")

    tokens(10) shouldBe a[Delimiter]
    tokens(10).toString should include(": ;")

    tokens(11) shouldBe a[Keyword]
    tokens(11).toString should include(": int")

    tokens(12) shouldBe a[Identifier]
    tokens(12).toString should include(": a")

    tokens(13) shouldBe a[Delimiter]
    tokens(13).toString should include(": ;")
  }
}