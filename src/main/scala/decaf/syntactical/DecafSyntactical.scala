package decaf.syntactical

import scala.util.parsing.combinator.syntactical.TokenParsers
import decaf.lexical.{DecafLexical, DecafTokens}

/**
 * Syntactical analyzer for the Decaf programming language
 *
 * @author Hawk Weisman
 *         Created by hawk on 9/28/14.
 */
class DecafSyntactical extends TokenParsers {
  type Tokens = DecafTokens
  val lexical = new DecafLexical

}
