package decaf.lexer

import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.token._

/**
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

trait DecafTokens extends Tokens {

  val MaxIdentLen = 31

  case class IntConst(chars: String) extends Token {
    val value: Integer = chars.toInt
    override def toString = "IntConstant: " + value
  }

  case class BoolConst(chars: String) extends Token {
    val value: Boolean = chars.toBoolean
    override def toString = "BoolConstant: " + value
  }

  case class StringConst(chars: String) extends Token {
    val value = chars
    override def toString = "StringConstant: \"" + value + "\""
  }

  case class DoubleConst(chars: String) extends Token {
    val value = chars.toDouble
    override def toString = "DoubleConstant: " + value
  }

  case class Identifier(chars: String) extends Token {
    val value = chars.take(MaxIdentLen)
    override def toString = "Identifier: " + value
  }

  case class Keyword(chars: String) extends Token {
    override def toString = chars
  }

  case class Operator(chars: String) extends Token {
    override def toString = "Operator: " + chars
  }

  
}

/**
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

class Lexer extends Lexical with DecafTokens {

}
