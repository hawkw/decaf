/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */

package decaf.frontend

import scala.collection.immutable.HashSet
import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.token._
import scala.util.parsing.input.CharArrayReader.EofCh

/**
 * Tokens for the Decaf programming language.
 *
 * TODO: Consider separating Keyword/Operator/Delimiter into separate types for each (a la JJ's C implementation)
 * TODO: Add line number tracking to tokens
 *
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

trait DecafTokens extends Tokens {

  val MaxIdentLen = 31

  case class IntConst(chars: String) extends Token {
    val value: Integer = if (chars.contains("0x") || chars.contains("0X"))
                            Integer.parseInt(chars.drop(2), 16)
                         else chars.toInt
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
    override def toString = "Keyword: " + chars
  }

  case class Operator(chars: String) extends Token {
    override def toString = "Operator: " + chars
  }

  case class Delimiter(chars: String) extends Token {
    override def toString = "Delimiter: " + chars
  }

}

/**
 * Lexical analyzer for Decaf tokens.
 *
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

class DecafLexical extends Lexical with DecafTokens {

  val keywords = HashSet("void", "int", "double", "bool", "string", "null", "class", "extends", "this", "interface",
  "implements", "while", "for", "if", "else", "return", "break", "new", "NewArray", "Print", "ReadInteger", "ReadLine")

  val boolLit = HashSet("true", "false")

  def chrIn(cs: Char*) = elem("", ch => cs contains ch)

  protected def exponent = chrIn('e','E') ~ chrIn('+', '-').? ~ digit.+
  protected def hexLetter = chrIn('a','b','c','d','e','f','A','B','C','D','E','F')

  def token: Parser[Token] = (
    /*------------------- Identifiers, Keywords, Boolean Literals --------------------------------------------------*/
    letter ~ rep(letter | digit | elem('_')) ^^ { case first ~ rest => processIdent(first :: rest mkString "")}
      /*------------------- Integer literals -------------------------------------------------------------------------*/
    | '0' ~ chrIn('x', 'X') ~ rep(digit | hexLetter)    ^^ { case first ~ rest => IntConst(first :: rest mkString "") }
    | digit ~ rep(digit)                                ^^ { case first ~ rest => IntConst(first :: rest mkString "") }
    | digit.+ ~ '.' ~ digit.* ~ exponent.?              ^^ { case first ~ rest ~ exponent => DoubleConst((first :: rest :: exponent.getOrElse("") :: Nil) mkString "") }
      /*------------------- String literals --------------------------------------------------------------------------*/
    | '\'' ~ rep( chrExcept('\'', '\"', '\n') ) ~ '\'' ^^ { case '\'' ~ chars ~ '\'' => StringConst(chars mkString "")}
    | '\"' ~ rep( chrExcept('\'', '\"', '\n') ) ~ '\"' ^^ { case '\"' ~ chars ~ '\"' => StringConst(chars mkString "")}
      | '\'' ~> failure("Unterminated string constant: ") //TODO: Line number of failure
      | '\"' ~> failure("Unterminated string constant: ") //TODO: Line number of failure
      /*------------------ Operators ---------------------------------------------------------------------------------*/
      // Note: we could probably actually be doing a higher level of semantic analysis here - we could have separate
      // operator types for logical, mathematical, bitwise, and equality operators (we're already separating them here)
      | chrIn('>', '<', '!', '=') ~ '=' ^^ { case first ~ last => Operator(first :: last :: Nil mkString "")}
      | (repN(2, '|') | repN(2, '&')) ^^ { case chars => Operator(chars mkString "")}
      | chrIn('+', '-', '!', '/', '=', '*', '>', '<', '&') ^^ { case char => Operator(char.toString)}
      /*------------------ Delimiters --------------------------------------------------------------------------------*/
      | chrIn(',', '.', ';', '{', '}', '(', ')') ^^ { case char => Delimiter(char.toString)}
      /*------------------ Misc --------------------------------------------------------------------------------------*/
    | failure("Error: Unrecognized character")
   )


  protected def processIdent(chars: String) = if (keywords contains chars)
                                              Keyword(chars)
                                            else if (boolLit contains chars)
                                              BoolConst(chars)
                                            else Identifier(chars)

  def whitespace: Parser[Any] = rep(
    whitespaceChar
      | '/' ~ '*' ~ comment
      | '/' ~ '/' ~ chrExcept('\n', EofCh).+
      | '/' ~ '*' ~ failure("Unterminated comment") //TODO: Line number of failure
  )

  def comment: Parser[Any] = (
    '*' ~ '/' ^^ { case _ => ' '}
      | chrExcept(EofCh) ~ comment
    )
}
