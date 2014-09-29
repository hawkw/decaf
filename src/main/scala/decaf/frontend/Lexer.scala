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
import scala.util.parsing.input.Positional

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

  abstract class DecafToken(val chars: String) extends Token with Positional {
    def value: Any = None

    def name = "T_" + this.getClass.getSimpleName

    def spaces = " " * (13 - chars.length)

    def line = pos.line

    def first_col = pos.column

    def last_col = pos.column + (chars.length - 1)

    override def toString = value match {
      case None => s"$chars $spaces line $line cols $first_col-$last_col is $name"
      case _ => s"$chars $spaces line $line cols $first_col-$last_col is $name (value = $value)"
    }
  }

  case class IntConstant(ch: String) extends DecafToken(ch) {
    override def value: Integer = if (chars.contains("0x") || chars.contains("0X"))
      Integer.parseInt(chars.drop(2), 16)
    else chars.toInt
  }

  case class BoolConstant(ch: String) extends DecafToken(ch) {
    override def value: Boolean = chars.toBoolean
  }

  case class StringConstant(ch: String) extends DecafToken(ch) {
    override def value: String = chars
  }

  case class DoubleConstant(ch: String) extends DecafToken(ch) {
    override def value: Double = chars.toDouble
  }

  case class Identifier(ch: String) extends DecafToken(ch) {
    override def value: String = chars.take(MaxIdentLen)
  }

  case class Keyword(ch: String) extends DecafToken(ch) {
    override def name = "T_" + chars.capitalize
  }

  case class Operator(ch: String) extends DecafToken(ch) {
    override def name = s"\'$chars\'"
  }

  case class Delimiter(ch: String) extends DecafToken(ch) {
    override def name = s"\'$chars\'"
  }

}

/**
 * Lexical analyzer for Decaf tokens.
 *
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

class DecafLexical(val trackPos: Boolean = true) extends Lexical with DecafTokens {

  type Token = DecafToken

  val keywords = HashSet("void", "int", "double", "bool", "string", "null", "class", "extends", "this", "interface",
  "implements", "while", "for", "if", "else", "return", "break", "new", "NewArray", "Print", "ReadInteger", "ReadLine")

  val boolLit = HashSet("true", "false")

  def chrIn(cs: Char*) = elem("", ch => cs contains ch)

  protected def exponent = chrIn('e','E') ~ chrIn('+', '-').? ~ digit.+
  protected def hexLetter = chrIn('a','b','c','d','e','f','A','B','C','D','E','F')

  def token: Parser[Token] = if (trackPos) positioned(_token) else _token

  private def _token: Parser[Token] = (
    /*------------------- Identifiers, Keywords, Boolean Literals --------------------------------------------------*/
    letter ~ rep(letter | digit | elem('_')) ^^ { case first ~ rest => processIdent(first :: rest mkString "")}
      /*------------------- Integer literals -------------------------------------------------------------------------*/
      | '0' ~ chrIn('x', 'X') ~ rep(digit | hexLetter) ^^ { case first ~ rest => IntConstant(first :: rest mkString "")}
      | digit ~ rep(digit) ^^ { case first ~ rest => IntConstant(first :: rest mkString "")}
      | digit.+ ~ '.' ~ digit.* ~ exponent.? ^^ { case first ~ rest ~ exponent => DoubleConstant((first :: rest :: exponent.getOrElse("") :: Nil) mkString "")}
      /*------------------- String literals --------------------------------------------------------------------------*/
      | '\'' ~ rep(chrExcept('\'', '\"', '\n')) ~ '\'' ^^ { case '\'' ~ chars ~ '\'' => StringConstant(chars mkString "")}
      | '\"' ~ rep(chrExcept('\'', '\"', '\n')) ~ '\"' ^^ { case '\"' ~ chars ~ '\"' => StringConstant(chars mkString "")}
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
    BoolConstant(chars)
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
