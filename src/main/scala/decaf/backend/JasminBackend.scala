package decaf.backend

import decaf.AST._
import decaf.frontend.ScopeNode

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Created by hawk on 12/1/14.
 */
object JasminBackend {

  def compile(program: Program): String = s"${makeHeader(program.getName)}\n${emit(program)}"
  //todo: shouldn't actually work this way (should fork on each class def)

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

  @tailrec private def getFnName(node: ASTNode): String = node match {
    case _: FnDecl => node.state.get.boundName
    case _ => getFnName(node.parent)
  }

  @tailrec private def getEnclosingScope(node: ASTNode): ScopeNode = node.state match {
    case Some(st) => st
    case _ => getEnclosingScope(node.parent)
  }

  private def getNextVar(localVars: mutable.Map[String,Int]) = localVars.unzip._2 match {
    case it if it isEmpty => 1
    case it => it.max + 1
  }
  private def emit(node: ASTNode,
                   localVars: mutable.Map[String, Int] = mutable.Map[String, Int](),
                   tabLevel: Int = 0): String = node match {
    case Program(decls, _) => decls.foldLeft("")((acc, decl) => acc + emit(decl))
    case VarDecl(n, t) => node.parent match {
      case _: Program => s".field public $n ${emit(t)}\n"
      case _: StmtBlock => val fnName = getFnName(node)
        localVars += (n.name -> getNextVar(localVars))
        s".var ${getNextVar(localVars)} is ${n.name} ${t.typeName} from Begin$fnName to End$fnName"
    } // TODO:
    case FnDecl(ASTIdentifier(_,name), rt, args, Some(code)) =>
      s".method public static $name(" +
        (if (name == "main") "[Ljava/lang/String;" else {args.map(emit(_)).mkString(";")}) +
        s")${emit(rt,localVars,tabLevel)}\n"+
      s"\n.limit locals ${code.decls.length + args.length + (if (name == "main") 1 else 0)}\n" +
      s".limit stack 5\n" + //TODO: dynamically figure out stack sizes
      s"Begin${node.state.get.boundName}:\n" +
      s"${emit(code, localVars, tabLevel + 1)}\n" +
      s"End${node.state.get.boundName}:\n${if (name == "main") "return\n"}.end method\n"
    case StmtBlock(declarations, code, _) =>
      val vars = mutable.Map[String, Int]() // not very functional but w/e
      declarations.map(emit(_, vars, tabLevel)).mkString("\n") + code.map(emit(_, vars, tabLevel)).mkString("\n")
     // todo: finish
    case ReturnStmt(loc, None) => ("\t" * tabLevel) + s".line ${loc.line}\n" + ("\t" * tabLevel) + "return"
    case e: Expr => e match {
      case ArithmeticExpr(_, left, op, right) =>
        emit(left, localVars, tabLevel + 1) + emit(right, localVars, tabLevel + 1) + ("\t" * (tabLevel + 1)) + (op match {
          case ASTOperator(_, "+") => e.typeof(getEnclosingScope(e)) match {
            case _: IntType => "iadd\n"
            case _: DoubleType => "dadd\n"
          }
          case ASTOperator(_, "-") => e.typeof(getEnclosingScope(e)) match {
            case _: IntType => "isub\n"
            case _: DoubleType => "dsub\n"
          }
          case ASTOperator(_, "/") => e.typeof(getEnclosingScope(e)) match {
            case _: IntType => "idiv\n"
            case _: DoubleType => "ddiv\n"
          }
          case ASTOperator(_, "*") => e.typeof(getEnclosingScope(e)) match {
            case _: IntType => "imul\n"
            case _: DoubleType => "dmul\n"
          }
          case ASTOperator(_, "%") => e.typeof(getEnclosingScope(e)) match {
            case _: IntType => "irem\n"
            case _: DoubleType => "drem\n"
          }
        })
      case ASTIntConstant(_, value) => ("\t" * tabLevel) + s"ldc 0x${value.toHexString}\n"
    }
    case s: Stmt => ("\t" * tabLevel) + s".line ${s.pos.line}\n" + (s match {
      case PrintStmt(exprs, _) => exprs match {
        case e :: Nil => ("\t" * (tabLevel + 1)) + "getstatic java/lang/System/out Ljava/io/PrintStream;\n" +
          emit(e, localVars, tabLevel) +
          ("\t" * (tabLevel + 1)) + s"invokevirtual java/io/PrintStream/println(${emit(e typeof getEnclosingScope(e) )})V\n"
        case _ => exprs.foreach(emit(_, localVars, tabLevel))
      }

    })
    case t: Type => t match {
      case _: IntType => "I"
      case _: DoubleType => "D"
      case _: VoidType => "V"
      case _: BoolType => "Z"
      case _: StringType => "Ljava/lang/String;"
      case ArrayType(_, elemType) => s"[${emit(elemType)}"
  }

    case FnDecl(name, rt, args, None) => ??? //NYI: interfaces aren't implemented
    case _ => println(s"ignored $node"); ""

  }

}
