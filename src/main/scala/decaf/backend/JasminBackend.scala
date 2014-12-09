package decaf.backend

import decaf.AST._

import scala.collection.mutable

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

  private def getFnScope(node: ASTNode): String = node match {
    case _: FnDecl => node.state.get.boundName
    case _ => getFnScope(node.parent)
  }
  private def getNextVar(localVars: mutable.Map[String,Int]) = localVars.unzip._2.max + 1
  private def emit(node: ASTNode, localVars: mutable.Map[String, Int] = mutable.Map[String, Int]()): String = node match {
    case Program(decls, _) => decls.reduceLeft((acc, decl) => acc + emit(decl))
    case VarDecl(n, t) => node.parent match {
      case _: Program => s".field public $n ${emit(t)}\n"
      case _: StmtBlock => val fnName = getFnScope(node)
        localVars += (n.name -> getNextVar(localVars))
        s".var ${getNextVar(localVars)} is $n $t from Begin$fnName to End$fnName"
    } // TODO:
    case FnDecl(name, rt, args, Some(code)) => s".method public static $name(${args.map(emit(_)).mkString(";")})" +
      s"\n.limit locals ${code.decls.length}\n${emit(code)}\nEnd${node.state.get.boundName}\n.end method\n"
    case StmtBlock(declarations, code, _) => {
      val vars = mutable.Map[String, Int]() // not very functional but w/e
      declarations.map(emit(_, vars)).mkString("\n") + code.map(emit(_, vars)).mkString("\n")
    } // todo: finish
    case FnDecl(name, rt, args, None) => ??? //NYI: interfaces aren't implemented
    case _ => println(s"ignored $node"); ""

  }

}
