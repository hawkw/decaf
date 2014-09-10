package decaf.lexer

trait Token
case class Identifier (val id: String) extends Token
case class Integer (val value: Integer) extends Token
case class Double (val value: Double) extends Token
case class EOF extends Token
case class Lparen extends Token
cae class Semicolon extends Token

class Lexer(input: Source) {
    var next_char = ''

    def advance(): Unit = next_char = input.next

    def getToken(): Token = next_char match {
        case null => input.close(); new EOF
        case '(' => next_char; new Lparen
        case ')' => next_char; new Rparen
        case ';' => next_char; new Semicolon
        case
    }

}