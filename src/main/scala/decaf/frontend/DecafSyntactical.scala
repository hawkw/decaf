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

import scala.util.parsing.combinator.{PackratParsers, Parsers}
import scala.util.parsing.input.Reader

/**
 * Syntactical analyzer for the Decaf programming language
 *
 * @author Hawk Weisman
 *         Created by hawk on 9/28/14.
 */
class DecafSyntactical extends Parsers with DecafAST with DecafTokens with PackratParsers {
  type Tokens <: DecafLexical
  override type Elem = DecafToken
  val lexical = new DecafLexical
  type P[+T] = PackratParser[T]

  implicit def dkeyword(k: Keyword): Parser[Elem] = acceptIf(_.name == k.name)("`" + k + "' expected but " + _ + " found")
  implicit def ddelimiter(d: Delimiter): Parser[Elem] = acceptIf(_.name == d.name)("`" + d + "' expected but " + _ + " found")
  implicit def doperator(o: Operator): Parser[Elem] = acceptIf(_.name == o.name)("`" + o + "' expected but " + _ + " found")

  def parse(source: String): Option[Program] = {
    val scan = new lexical.DecafScanner(source).asInstanceOf[Reader[DecafToken]]

    /**
     * Some debuggery using Print.
     */
    var dscan = scan;
    while(!dscan.atEnd) {
      System.out.println(dscan.first)
      dscan = dscan.rest
    }


    phrase(program)(scan) match {
      case Success(result, _) => Some(result)
      case _ => None
    }
  }

  lazy val program: PackratParser[Program] = positioned(
    decl.+ ^^ {
      case decls => new Program(decls)
    })

  lazy val decl: PackratParser[Decl] = (
    variableDecl ~ Delimiter(";") ^^ {case v ~ _ => v}
    | functionDecl
    //| classDecl
    //| interfaceDecl
    )

  lazy val stmtBlock = Delimiter("{") ~ ((variableDecl <~ Delimiter(";"))| stmt).* ~ Delimiter("}") ^^ {
    case Delimiter("{") ~ stuff ~ Delimiter("}") => StmtBlock(
      stuff.filter(_.isInstanceOf[VarDecl]).asInstanceOf[List[VarDecl]],
      stuff.filter(_.isInstanceOf[Stmt]).asInstanceOf[List[Stmt]])
  }

  lazy val returnType: PackratParser[Type] = (
    Keyword("void") ^^^ VoidType()
    | typ ^^{case t => t}

    )

  lazy val formals: PackratParser[List[VarDecl]] = (
    Delimiter("(") ~ Delimiter(")") ^^^ List()
    | Delimiter("(") ~ repsep(variableDecl, Delimiter(",")) ~ Delimiter(")") ^^ {case _ ~ stuff ~ _ => stuff}
    )

  lazy val fnargs: PackratParser[List[Expr]] = Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
    case _ ~ stuff ~ _ => stuff
  }

  lazy val functionDecl: PackratParser[Decl] = returnType ~ ident ~ formals ~ stmtBlock ^^{
    case rt ~ name ~ fs ~ body => FnDecl( name, rt, fs, body)
  }

  lazy val stmt: PackratParser[Stmt] =(
    expr.? ~ Delimiter(";") ^^{
      case e ~ d => if (e.isDefined) {e.get.asInstanceOf[Stmt]} else {EmptyExpr(d.getPos)}
    }
      | Keyword("Print") ~ Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ~ Delimiter(";") ^^ { case _ ~ _ ~ e ~ _ ~ _ => PrintStmt(e)}
      | ifStmt
      | whileStmt
      | forStmt
      | breakStmt
      | stmtBlock
      | Keyword("return") ~ expr.? <~ Delimiter(";") ^^{
      case k ~ None => ReturnStmt(k.getPos, Some(EmptyExpr(k.getPos)))
      case k ~ Some(thing) => ReturnStmt(k.getPos, Some(thing))

    }
   )
  lazy val breakStmt: PackratParser[Stmt] = Keyword("break") ~ Delimiter(";") ^^{case k ~ Delimiter(";") => BreakStmt(k.getPos)}
  lazy val ifStmt: PackratParser[Stmt] =
    Keyword("if") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ~ opt(Keyword("else") ~> stmt) ^^{
      case Keyword("if") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ testbody ~ elsebody => IfStmt(test,testbody,elsebody)
    }
  lazy val whileStmt: PackratParser[Stmt] = Keyword("while") ~ Delimiter("(") ~ expr ~ Delimiter(")") ~ stmt ^^{
    case Keyword("while") ~ Delimiter("(") ~ test ~ Delimiter(")") ~ loopbody => WhileStmt(test,loopbody)
  }
  lazy val forStmt: PackratParser[Stmt] =
    Keyword("for") ~ Delimiter("(") ~ opt(expr) ~ Delimiter(";") ~ expr ~ Delimiter(";") ~ opt(expr) ~ Delimiter(")") ~ stmt ^^{
      case k ~ _ ~ None ~ _ ~ t ~ _ ~ Some(step) ~ _ ~ b => ForStmt(Some(EmptyExpr(k.getPos)),t,Some(step),b)
      case k ~ _ ~ Some(init) ~ _ ~ t ~ _ ~ Some(step) ~ _ ~ b =>ForStmt(Some(init),t,Some(step),b)
      case k ~ _ ~ None ~ _ ~ t ~ _ ~ None ~ _ ~ b => ForStmt(Some(EmptyExpr(k.getPos)),t,Some(EmptyExpr(k.getPos)),b)
      case k ~ _ ~ Some(init) ~ _ ~ t ~ _ ~ None ~ _ ~ b =>ForStmt(Some(init),t,Some(EmptyExpr(k.getPos)),b)
    }
  /*
    lazy val assign: PackratParser[Expr] = (
        lValue ~ Operator("=") ~ expr ^^ {case left ~ Operator("=") ~ right => AssignExpr(left.getPos, left, right)}
      )

    lazy val expr: PackratParser[Expr] = (
      expr ~ Operator("==") ~ expr ^^{
        case left ~ Operator("==") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "=="), right)
      }
        | expr ~ Operator("<") ~ expr ^^{
        case left ~ Operator("<") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, "<"), right)
      }
        | expr ~ Operator("<=") ~ expr ^^{
        case left ~ Operator("<=") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, "<="), right)
      }
        | expr ~ Operator(">") ~ expr ^^{
        case left ~ Operator(">") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, ">"), right)
      }
        | expr ~ Operator(">=") ~ expr ^^{
        case left ~ Operator(">=") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, ">="), right)
      }
        | expr ~ Operator("*") ~ expr ^^{
        case left ~ Operator("*") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "*"), right)
      }
        | expr ~ Operator("/") ~ expr ^^{
        case left ~ Operator("/") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "/"), right)
      }
        | expr ~ Operator("%") ~ expr ^^{
        case left ~ Operator("%") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "%"), right)
      }
        | expr ~ Operator("+") ~ expr ^^{
        case left ~ Operator("+") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "+"), right)
      }

      |  Delimiter("(") ~ expr ~ Delimiter(")") ^^{ case Delimiter("(") ~ e ~ Delimiter(")") => e }
        | const
        | assign
        | lValue
        | call
      | Keyword("this") ^^{ case k => This(k.getPos) }

      | expr ~ Operator("-") ~ expr ^^{
       case left ~ Operator("-") ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(left.getPos, "-"), right)
      }
        | expr ~ Operator("!=") ~ expr ^^{
        case left ~ Operator("!=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "!="), right)
      }
        | expr ~ Operator("&&") ~ expr ^^{
        case left ~ Operator("&&") ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "&&"), right)
      }
        | Operator("!") ~ expr ^^{
        case Operator("!") ~ right => new LogicalExpr(right.getPos, ASTOperator(right.getPos, "!"), right)
      }
        | Keyword("ReadInteger") ~ Delimiter("(") ~ Delimiter(")") ^^{
        case k ~ Delimiter("(") ~ Delimiter(")") => ReadIntegerExpr(k.getPos)
      }
        | Keyword("ReadLine") ~ Delimiter("(") ~ Delimiter(")") ^^{
        case k ~ Delimiter("(") ~ Delimiter(")") => ReadLineExpr(k.getPos)
      }
        | Keyword("new") ~ ident ^^{
        case Keyword("new") ~ i => NewExpr(i.getPos, NamedType(i))
      }
        | Keyword("NewArray") ~ Delimiter("(") ~ expr ~ Delimiter(",") ~ typ ~ Delimiter(")") ^^{
        case Keyword("NewArray") ~ Delimiter("(") ~ e ~ Delimiter(",") ~ t ~ Delimiter(")") => NewArrayExpr(e.getPos,e,t)
      }
      )
  */
  lazy val lValue: P[LValue] = (
    ident ^^{ case i => FieldAccess(i.getPos, None, i)}
    | expr ~ Delimiter("[") ~ expr ~ Delimiter("]") ^^{
      case first ~ Delimiter("[") ~ last ~ Delimiter("]") =>
        ArrayAccess(first.getPos, first, last)
    }
    | expr ~ Delimiter(".") ~ ident ^^{case e ~ Delimiter(".") ~ i => FieldAccess(i.getPos, Some(e), i)}
    )

  lazy val call: P[Call] = (
    expr ~ Delimiter(".") ~ ident ~ fnargs ^^{
      case base ~ _ ~ field ~ args  =>
        new Call(base.getPos, base, field, args)
    }
    | ident ~ fnargs ^^{
      case field ~ args  => new Call(field.getPos, field, args)
    }
    )
// Huge terrible expr thing
  lazy val expr: P[Expr] = (
    assign
  | arithmatic
  | logical
  | relational
  | unary
  | func
  | storage
  | rexpr
  )
  lazy val assign: P[Expr] = (
    lValue ~ Operator("=") ~ expr ^^{
      case left ~ _ ~ right => AssignExpr(left.getPos, left, right)
    }
  )
  def logical: Parser[Expr] = (
    expr ~ Operator("||") ~ expr ^^{
      case left ~ _ ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "||"), right)
    }
    | expr ~ Operator("&&") ~ expr ^^{
      case left ~ _ ~ right => new LogicalExpr(left.getPos, left, ASTOperator(left.getPos, "&&"), right)
    }
    | expr ~ Operator("==") ~ expr ^^{
      case left ~ _ ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "=="), right)
    }
    | expr ~ Operator("!=") ~ expr ^^{
      case left ~ Operator("!=") ~ right => EqualityExpr(left.getPos, left, ASTOperator(left.getPos, "!="), right)
    }
    )
  lazy val relational: P[Expr] = (
    expr ~ Operator(">=") ~ expr ^^{
       case left ~ Operator(">=") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, ">="), right)
     }
       | expr ~ Operator("<=") ~ expr ^^{
       case left ~ Operator("<=") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, "<="), right)
     }
       | expr ~ Operator(">") ~ expr ^^{
       case left ~ Operator(">") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, ">"), right)
     }
    | expr ~ Operator("<") ~ expr ^^{
      case left ~ Operator("<") ~ right => RelationalExpr(left.getPos, left, ASTOperator(left.getPos, "<"), right)
    }
    )
  lazy val arithmatic: P[Expr] = (
    expr ~ Operator("%") ~ arithRhs ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "%"), right)
    }
      | expr ~ Operator("/") ~ arithRhs ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "/"), right)
    }
      | expr ~ Operator("*") ~ arithRhs ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "*"), right)
    }
      | expr ~ Operator("-") ~ arithRhs ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "-"), right)
    }
   | expr ~ Operator("+") ~ arithRhs ^^{
      case left ~ op ~ right => ArithmeticExpr(left.getPos, left, ASTOperator(op.getPos, "+"), right)
    }
    )
  lazy val arithRhs: P[Expr] = (
    arithmatic
    | unary
    | func
    | storage
    | rexpr
    )
  lazy val unary: P[Expr] = (
    Operator("!") ~ expr ^^{ case op ~ e => LogicalExpr(op.getPos, None, ASTOperator(op.getPos, "!"), e)}
    | Operator("-") ~ expr ^^{ case op ~ e => LogicalExpr(op.getPos, None, ASTOperator(op.getPos, "-"), e)} //TODO: make ArithmaticExpr support unary minus (later)
    )
  lazy val func: P[Expr] = (
    call
    | Keyword("ReadLine") ~ Delimiter("(") ~ Delimiter(")") ^^{
      case k ~ Delimiter("(") ~ Delimiter(")") => ReadLineExpr(k.getPos)
    }
    | Keyword("ReadInteger") ~ Delimiter("(") ~ Delimiter(")") ^^{
      case k ~ Delimiter("(") ~ Delimiter(")") => ReadIntegerExpr(k.getPos)
    }
    )
  lazy val storage: P[Expr] = (
      const
    | lValue
    | Keyword("this") ^^{ case k => This(k.getPos) }
    | Keyword("new") ~ ident ^^{
      case Keyword("new") ~ i => NewExpr(i.getPos, NamedType(i))
    }
    | Keyword("NewArray") ~ Delimiter("(") ~ expr ~ Delimiter(",") ~ typ ~ Delimiter(")") ^^{
      case Keyword("NewArray") ~ Delimiter("(") ~ e ~ Delimiter(",") ~ t ~ Delimiter(")") => NewArrayExpr(e.getPos,e,t)
    }
    )
  lazy val rexpr: P[Expr] = ( Delimiter("(") ~> expr <~ Delimiter(")") )

  lazy val const: PackratParser[Expr] = (
    elem("intConst", _.isInstanceOf[IntConstant])
      | elem("doubleConst", _.isInstanceOf[DoubleConstant])
      | elem("BoolConst", _.isInstanceOf[BoolConstant])
      | elem("StringConst", _.isInstanceOf[StringConstant])
      | Keyword("null")
    )^^{
    case i: IntConstant => ASTIntConstant(i.getPos, i.value)
    case d: DoubleConstant => ASTDoubleConstant(d.getPos, d.value)
    case b: BoolConstant => ASTBoolConstant(b.getPos, b.value)
    case s: StringConstant => ASTStringConstant(s.getPos, s.value)
    case n: Keyword => ASTNullConstant(n.getPos)
  }

  lazy val variableDecl: PackratParser[VarDecl] = typ ~ ident ^^ {
    case t ~ e => VarDecl(e, t)
  }

  lazy val ident: PackratParser[ASTIdentifier] = _ident ^^{
    case i: Identifier => ASTIdentifier(Some(i.getPos), i.value)
  }

  lazy val _ident: PackratParser[Elem] = acceptIf(_.isInstanceOf[Identifier])("Identifier token expected but " + _ + " found")

  lazy val atyp: PackratParser[Type] = (
      /* I don't know if Decaf supports multidimensional arrays,
         but if you leave in this typ here at the top, we can do that.
         Otherwise, take it out.
       */
      typ
      | Keyword("int") ^^^ IntType()
      | Keyword("double") ^^^ DoubleType()
      | Keyword("bool") ^^^ BoolType()
      | Keyword("string") ^^^ StringType()
      | ident ^^ { case i => NamedType(i)}
    )

  lazy val typ: PackratParser[Type] = (
      atyp ~ Delimiter("[]") ^^ { case t ~ dims => ArrayType(Some(dims.getPos), t)}
      | atyp
    )
} /*


  def eexpr: Parser[Expr] = (
      const
    )

  def expr: Parser[Expr] = (

    )

  def call: Parser[Call] = (
    ident ~  Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case field ~ Delimiter("(") ~ args ~ Delimiter(")")  => new Call(field.getPos, field, args)
    }
    | expr ~ Delimiter(".") ~ ident ~ Delimiter("(") ~ repsep(expr, Delimiter(",")) ~ Delimiter(")") ^^{
      case base ~ Delimiter(".") ~ field ~ Delimiter("(") ~ args ~ Delimiter(")")  =>
        new Call(base.getPos, base, field, args)
    }
    )
  def functionDecl: Parser[Decl] = returnType ~ ident ~ Delimiter("(") ~ formals.? ~ Delimiter(")") ~ stmtBlock ^^{
      case rt ~ name ~ _ ~ fs ~ _ ~ body => FnDecl( name, rt, fs.getOrElse(Nil), body)
    }
  def classDecl: Parser[Decl] =
    Keyword("class") ~> ident ~ extendPart.? ~ implementsPart ~ Delimiter("{") ~ rep(field) ~ Delimiter("}") ^^{
      case name ~ ext  ~ imp ~ Delimiter("{") ~ fields ~ Delimiter("}") => ClassDecl(name, ext, imp, fields)
    }
  def extendPart: Parser[NamedType] = Keyword("extends") ~> className
  def implementsPart: Parser[List[NamedType]] = Keyword("implements") ~> repsep(className, Delimiter(","))
  def field: Parser[Decl] = (variableDecl | functionDecl)

  def className: Parser[NamedType] = ident ^^{ case i=> NamedType(i) }

  */