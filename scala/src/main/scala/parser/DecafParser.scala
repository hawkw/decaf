package decaf.parser
import scala.util.parsing.combinators._

object DecafParser extends RegexParsers {
    def float: Parser[Float] = """[0-9]+\.[0-9]+?""".r ^^ {_.toFloat}
    def int: Parser[Int] = """([0-9]+)|(0[xX][0-9a-fA-F]+)""".r ^^ {_.toInt}
    def string: Parser[String] = """\"[^\n\"]\"""".r
}