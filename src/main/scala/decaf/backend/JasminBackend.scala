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
      // TODO: everthing in between
    case VarDecl => ??? // TODO:
    case FnDecl(name, rt, args, Some(code)) => s".method public static $name(${})"
    case FnDecl(name, rt, args, None) => ??? //TODO: interfaces aren't implemented
    case _ => println(s"ignored $node"); ""

  }

}
