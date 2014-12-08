package decaf.backend

import decaf.AST._

/**
 * Created by hawk on 12/1/14.
 */
object JasminBackend {

  def compile(program: Program): String = ???

  private def makeHeader(name: String, sup: String="java/lang/Object") = s".class public $name\n.super $sup\n" + makeInitializer(sup)
  private def makeInitializer(sup: String) = sup match {
    case "java/lang/Object" =>   ";\n; standard initializer (calls java.lang.Object's initializer)\n" +
      ";\n" +
      ".method public <init>()V\n" +
        "aload_0\n" +
        "invokenonvirtual java/lang/Object/<init>()V\n" +
        "return\n" +
      ".end method\n"
    case _ => ??? //TODO: this is where classes would actually happen
  }

  private def emit(node: ASTNode): String = node match {
    case Program(decls, _) => decls.reduceLeft((acc, decl) => acc + emit(decl))
    case VarDecl(n, t) => node.parent match {
      case _: Program => s".field public $n ${emit(t)}\n"
      case _: FnDecl => s".var"
    } // TODO:
    case FnDecl(name, rt, args, Some(code)) => s".method public static $name(${args.map(emit(_)).mkString(";")})" +
      s"\n.line ${name.loc.line}\n${emit(code)}"
    case StmtBlock(declarations, code, _) => ???
    case FnDecl(name, rt, args, None) => ??? //NYI: interfaces aren't implemented
    case _ => println(s"ignored $node"); ""

  }

}
