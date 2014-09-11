package decaf.parser
import scala.util.parsing.combinators._

object DecafParser extends RegexParsers {

    val ident= """[a-zA-Z][a-zA-Z_]+""".r

    def program = rep1(decl)
    def decl = variableDecl
             | functionDecl
             | classDecl
             | interfaceDecl
    def variableDecl = variable ~ ";"
    def variable = type ~ ident
    def type = "int"
             | "double"
             | "bool"
             | "string"
             | ident
             | type ~ "[]"
    def functionDecl = Type ~ ident ~ "(" ~ formals ~ ")" ~ stmtBlock
                     | "void" ~ ident ~ "(" ~ formals ~ ")" ~ stmtBlock
    def formals = variable* | ""
    def classDecl = "class" ~ ident ~ opt("extends" ~ ident) ~ opt("implements" ~ repsep(ident, ",")) "{" ~ field* ~ "}"
    def field = variableDecl | functionDecl
    def interfaceDecl = "interface" ~ ident ~ "{" ~ prototype* ~ "}"
    def prototype = type ~ ident ~ "(" ~ formals ~ ")" ~ ";"
                  | "void" ~ ident ~ "(" ~ formals ~ ")" ~ ";"
    def stmtBlock = ~ "{" ~ variableDecl* ~ stmt* ~ "}"
    def stmt = opt(expr) ~ ";"
             | ifStmt
             | whileStmt
             | forStmt
             | breakStmt
             | returnStmt
             | printStmt
             | stmtBlock
    def ifStmt = "if" ~ "(" ~ expr ~ ")" ~ stmt ~ opt("else" ~ stmt)

}