package decaf.lexer

import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.token._

/**
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

trait DecafTokens extends Tokens {

  case class IntConst(chars: String) extends Token {
    val value: Integer = chars.toInt
    override def toString = "IntConstant: " + value
  }

  case class BoolConst(chars: String) extends Token {
    val value: Boolean = chars.toBoolean
    override def toString = "BoolConstant: " + value
  }

  
}

class Lexer extends Lexical with DecafTokens {

}
