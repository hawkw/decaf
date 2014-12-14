/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */

package decaf
package frontend

import scala.collection.immutable.HashSet
import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.token._
import scala.util.parsing.input.CharArrayReader.EofCh
import scala.util.parsing.input.{CharArrayReader, Reader, Positional}

/**
 * Tokens for the Decaf programming language.
 *
 * TODO: Consider separating Keyword/Operator/Delimiter into separate types for each (a la JJ's C implementation)
 *
 * @author Hawk Weisman
 * Created by hawk on 9/27/14.
 */

trait DecafTokens extends Tokens {

  val MaxIdentLen = 31

  sealed abstract class DecafToken(val chars: String) extends Token with Positional {
    def value: Any = None

    def getPos = this.pos

    override def toString = s"$chars$spaces line $line cols $first_col-$last_col is $name"

    def spaces = " " * (12 - chars.length)

    def line = pos.line

    def first_col = pos.column

    def last_col = pos.column + (chars.length - 1)

    override def equals(obj: Any) = obj match {
      case that: DecafToken => this.name == that.name
      case _ => false
    }

    def name = "T_" + this.getClass.getSimpleName
  }

  case class IntConstant(ch: String) extends DecafToken(ch) {
    override def toString = s"$chars$spaces line $line cols $first_col-$last_col is $name (value = $value)"

    override def value: Integer = if (chars.contains("0x") || chars.contains("0X"))
      Integer.parseInt(chars.drop(2), 16)
    else chars.toInt
  }

  case class BoolConstant(ch: String) extends DecafToken(ch) {
    override def toString = s"$chars$spaces line $line cols $first_col-$last_col is $name (value = $value)"

    override def value: Boolean = chars.toBoolean
  }

  case class StringConstant(ch: String) extends DecafToken(ch) {
    override def toString = s"$chars$spaces line $line cols $first_col-$last_col is $name (value = $value)"

    override def value: String = chars
  }

  case class DoubleConstant(ch: String) extends DecafToken(ch) {
    override def toString = s"$chars$spaces line $line cols $first_col-$last_col is $name (value = $value)"

    override def value: Double = chars.toDouble
  }

  case class Identifier(ch: String) extends DecafToken(ch) {
    override def value: String = chars.take(MaxIdentLen)
  }

  case class Keyword(ch: String) extends DecafToken(ch) {
    override def name = "T_" + chars.capitalize
  }

  case class Operator(ch: String) extends DecafToken(ch) {
    override def name = chars match{
      case "||" =>"T_Or"
      case "<=" => "T_LessEqual"
      case ">=" => "T_GreaterEqual"
      case "==" => "T_Equal"
      case _ => s"\'$chars\'"
    }
  }

  case class Delimiter(ch: String) extends DecafToken(ch) {
    override def name = chars match{
      case "[]" => "T_Dims"
      case _ => s"\'$chars\'"
    }
  }

  case class Ignore() extends DecafToken("")

}

/**
 * Lexical analyzer for Decaf tokens.
 *
 * @author Hawk Weisman <hawk@meteorcodelabs.com>
 * @author Max Clive <mattrulz127@gmail.com>
 *
 * Created by hawk on 9/27/14.
 */

class DecafLexical extends Lexical with DecafTokens with Scanners {

  type Token = DecafToken

  val keywords = Set("void", "int", "double", "bool", "string", "null", "class", "extends", "this", "interface",
  "implements", "while", "for", "if", "switch", "case", "default", "else", "return", "break", "new", "NewArray",
  "Print", "ReadInteger", "ReadLine")

  val boolLit = Set("true", "false")

  def chrIn(cs: Char*) = elem("", ch => cs contains ch)

  def program: Parser[List[Any]] = rep(token) ^^ { case tokens => tokens.filter(!_.isInstanceOf[Ignore])}

  def token: Parser[Token] = positioned(
    /*------------------- Identifiers, Keywords, Boolean Literals --------------------------------------------------*/
    letter ~ rep(letter | digit | elem('_')) ^^ { case first ~ rest => processIdent(first :: rest mkString "")}
      /*------------------- Integer literals -------------------------------------------------------------------------*/
      | '0' ~ chrIn('x', 'X') ~ rep(digit | hexLetter) ^^ { case '0' ~ xes ~ digits => IntConstant("0" + xes + digits.mkString(""))}
      | digit.+ ~ '.' ~ digit.* ~ exponent.? ^^ { case first ~ '.' ~ rest ~ exponent => DoubleConstant(first.mkString("") + '.' + rest.mkString("") + exponent.getOrElse(""))}
      | digit ~ rep(digit) ^^ { case first ~ rest => IntConstant(first :: rest mkString "")}
      /*------------------- String literals --------------------------------------------------------------------------*/
      | '\'' ~ rep(chrExcept('\'', '\"', '\n')) ~ '\'' ^^ { case '\'' ~ chars ~ '\'' => StringConstant("\'" + chars.mkString("") + "\'")}
      | '\"' ~ rep(chrExcept('\'', '\"', '\n')) ~ '\"' ^^ { case '\"' ~ chars ~ '\"' => StringConstant("\"" + chars.mkString("") + "\"")}
      | '\'' ~> failure("Unterminated string constant: ")
      | '\"' ~> failure("Unterminated string constant: ")
      /*------------------ Whitespace ---------------------------------------------------------------------------------*/
      | chrIn(' ', '\n', '\t') ^^^ Ignore()
      | '/' ~ '*' ~ comment ^^^ Ignore()
      | '/' ~ '/' ~ rep(chrExcept(EofCh, '\n')) ^^^ Ignore()
      | '/' ~ '*' ~> failure("unclosed comment")
      /*------------------ Operators ---------------------------------------------------------------------------------*/
      // Note: we could probably actually be doing a higher level of semantic analysis here - we could have separate
      // operator types for logical, mathematical, bitwise, and equality operators (we're already separating them here)
      | chrIn('>', '<', '!', '=') ~ '=' ^^ { case first ~ last => Operator(first :: last :: Nil mkString "")}
      | (repN(2, '|') | repN(2, '&') | repN(2, '+') | repN(2, '-')) ^^ { case chars => Operator(chars mkString "")}
      | chrIn('+', '-', '!', '/', '%', '=', '*', '>', '<', '&') ^^ { case char => Operator(char.toString)}
      /*------------------ Delimiters --------------------------------------------------------------------------------*/
      | '[' ~ ']' ^^ { case _ => Delimiter("[]") }
      | chrIn(',', '.', ':', ';', '{', '}', '(', ')', '[', ']') ^^ { case char => Delimiter(char.toString)}
      /*------------------ Misc --------------------------------------------------------------------------------------*/
    | failure("Error: Unrecognized character")
   )

  def whitespace: Parser[Any] = rep[Any](
    whitespaceChar
      | '/' ~ '*' ~ comment
      | '/' ~ '/' ~ rep(chrExcept(EofCh, '\n'))
      | '/' ~ '*' ~ failure("unclosed comment")
  )

  protected def exponent = chrIn('e', 'E') ~ chrIn('+', '-').? ~ digit.+ ^^ { case first ~ sign ~ rest => first :: sign.getOrElse("") :: rest mkString ""}

  protected def hexLetter = chrIn('a','b','c','d','e','f','A','B','C','D','E','F')

  protected def processIdent(chars: String) = if (keywords(chars))
                                              Keyword(chars)
                                            else if (boolLit(chars))
                                              BoolConstant(chars)
                                            else Identifier(chars)

  protected def comment: Parser[Any] = (
    '*' ~ '/' ^^ { case _ => ' '}
      | chrExcept(EofCh) ~ comment
    )

  class DecafScanner(in: Reader[Char]) extends Reader[DecafToken] {
    private val (tok, rest1, rest2) = whitespace(in) match {
      case Success(_, in1) =>
        token(in1) match {
          case Success(token, in2) => (token, in1, in2)
          case ns: NoSuccess => (errorToken(ns.msg), ns.next, skip(ns.next))
        }
      case ns: NoSuccess => (errorToken(ns.msg), ns.next, skip(ns.next))
    }

    /** Convenience constructor (makes a character reader out of the given string) */
    def this(in: String) = this(new CharArrayReader(in.toCharArray))

    override def source: java.lang.CharSequence = in.source

    override def offset: Int = in.offset

    def first = tok.asInstanceOf[Token]

    def rest = new Scanner(rest2)

    def pos = rest1.pos

    def atEnd = in.atEnd || (whitespace(in) match { case Success(_, in1) => in1.atEnd case _ => false })

    private def skip(in: Reader[Char]) = if (in.atEnd) in else in.rest
  }
}
