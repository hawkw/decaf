package decaf
package backend
/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */

import decaf.AST._
import decaf.frontend.ScopeNode

import scala.language.postfixOps
import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Backend for generating Java bytecode using the Jasmin assembler.
 *
 * Thisbackend generates Java bytecode using the [[http://jasmin.sourceforge.net Jasmin]] assembly language.
 * Invoking the Decaf compiler (`dcc`) on a Decaf source code file will produce Jasmin assembly files with
 * the file extension `.j`. In order to produce executable `.class` files, the Jasmin assembler must be
 * invoked on those `.j` files. You can download an executable Jasmin jarfile
 * [[http://sourceforge.net/projects/jasmin/files/ here]].
 *
 * @author Hawk Weisman <hawk@meteorcodelabs.com>
 * Created by hawk on 12/1/14.
 */
object JasminBackend extends Backend{

  private val rand = new util.Random

  override def compile(program: Program, fileName: Option[String]): String = s"${makeHeader(fileName.getOrElse("Program"))}\n${emit(program)}"
  //todo: shouldn't actually work this way (should fork on each class def)

  /**
   * Generates the "header" string of a Jasmin bytecode class definition.
   *
   * Note that this always generates public classes (since the Decaf language doesn't support private inner classes).
   * @param name the name of the class being generated
   * @param sup the super class of the class being generated, defaults to `java.lang.Object`
   * @return a String containing the header for the Jasmin bytecode class file
   */
  private def makeHeader(name: String, sup: String="java/lang/Object") =
    s".class public $name\n.super $sup\n" + makeInitializer(sup)

  /**
   * Generates the initializer for a Jasmin bytecode class definition.
   *
   * TODO: this currently does not support classes with constructor parameters (all initializers are just calls to the java.lang.Object initializer)
   * @param sup the super class of the class being generated
   * @return
   */
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

  /**
   * Finds the [[AST.FnDecl function declaration]] containing the specified [[AST.ASTNode AST node]].
   *
   * Recursively walks the AST backwards from the given node until it finds a [[AST.FnDecl FnDecl node]].
   *
   * TODO: handle nodes not contained in functions - either throw an error, or return None
   *
   * @param node the AST node to find the corresponding FnDecl for
   * @return the [[AST.FnDecl FnDecl]] containing the node
   */
  @tailrec private def getFnDecl(node: ASTNode): FnDecl = node match {
    case d: FnDecl => d
    case _ => getFnDecl(node.parent)
  }

  /**
   * Finds the first scope containing the specified [[AST.ASTNode AST node]].
   *
   * Recursively walks the AST backwards from the given node until it finds a node with a defined scope.
   *
   * @param node the AST node to find the enclosing scope for
   * @return the [[ScopeNode]] for the first scope containing the specified node
   */
  @tailrec private def getEnclosingScope(node: ASTNode): ScopeNode =
    node.state match {
    case Some(st) => st
    case None if node.parent != null => getEnclosingScope(node.parent)
    case _ => throw new Exception(s"Node $node didn't have enclosing scope")
      // this shouldn't happen - Hawk, 12/13/14
      // but it does           - Hawk, 12/15/14
  }

  /**
   * Determines if the specified [[AST.ASTNode AST node]] is within an assignment expression.
   *
   * @param node the node to determine if it is in an assignment expression
   * @return true if the node is in an assign expression, false otherwise
   */
  @tailrec private def inAssignExpr(node: ASTNode): Boolean =
    node match {
      // TODO: this is a hack and probably could be made much less slow
    case AssignExpr(_,_,_) => true
    case Program(_,_) => false
    case _ => inAssignExpr(node.parent)
  }

  /**
   * Gets the name of the function containing an [[AST.ASTNode AST node]].
   * @param node the AST node to find the name of the enclosing function for
   * @return a String containing the name of the node
   */
  private def getFnName(node: ASTNode): String = getFnDecl(node).state.get.boundName

  /**
   * Gets the next variable number from a local variables mapping
   * @param localVars the mapping of String -> Int representing the currently defined var table
   * @return the next integer value for a local var
   */
  private def getNextVar(localVars: mutable.Map[String,Int]) = localVars.unzip._2 match {
    case it if it isEmpty => 0
    case it => it.max + 1
  }

  /**
   * Recursively emit Jasmin assembly code for a [[AST.ASTNode Decaf AST node]].
   *
   * Basically, this is a giant pattern match that recursively walks the AST from the top down.
   * This could be much cleaner if each AST node knew how to emit its' own bytecode, since we could use
   * the visitor pattern, but due to the way our AST tree is implemented, that would be nontrivial
   * (the AST is essentially a direct port of the C++ implementation provided by Dr. Jumadinova). Thus,
   * the giant pattern match. It's really ugly, but may actually be faster (smaller call stack).
   *
   * @param node a [[AST.ASTNode Decaf AST node]] to emite bytecode for
   * @param localVars the local variables mapping (String -> Int) for the current function scope (defaults to empty map)
   * @param tabLevel the indentation level (defaults to 0)
   * @param breakable An optional String the name of any loop that can be broken out of (defaults to None)
   * @return the Jasmin assembly code for the specified AST node
   */
  private def emit(node: ASTNode,
                   localVars: mutable.Map[String, Int] = mutable.Map[String, Int](),
                   tabLevel: Int = 0,
                   breakable: Option[String] = None): String = node match {
    // To those who are about to read my code, I am terribly, terribly sorry.
    // We thank you for your sacrifice.
    //    ~ hawk
    case Program(decls, _) => decls.foldLeft("")((acc, decl) => acc + emit(decl))
    case VarDecl(n, t) => node.parent match {
      case _: Program => s".field public $n ${emit(t)}\n"
      case _ =>
        var fnName = getFnName(node)
        localVars += (n.name -> getNextVar(localVars))
        s".var ${localVars(n.name)} is ${n.name} ${emit(t)} from Begin$fnName to End$fnName\n"

    } // TODO:
    case FnDecl(ASTIdentifier(_,name), rt, args, Some(code)) =>
      s".method public static $name(" +
        (
          if (name == "main") "[Ljava/lang/String;"
          else {args.map(emit(_)).mkString(";")}
          ) +
        s")${emit(rt,localVars,tabLevel)}\n"+
      s"\n.limit locals ${code.decls.length + args.length + (if (name == "main") 1 else 0)}\n" +
      s".limit stack 5\n" + //TODO: dynamically figure out stack sizes
      s"Begin${node.state.get.boundName}:\n" +
      s"${emit(code, localVars, tabLevel + 1)}\n" +
      s"End${node.state.get.boundName}:\n${if (name == "main") "return\n"}.end method\n"
    case FnDecl(name, rt, args, None) => ??? //NYI: interfaces aren't implemented
    case StmtBlock(declarations, code, _) =>
      declarations
        .map(emit(_, localVars, tabLevel, breakable))
        .mkString("\n") +
      code
        .map(emit(_, localVars, tabLevel, breakable))
        .mkString("\n")
    case ReturnStmt(loc, None) =>
      ("\t" * tabLevel) + s".line ${loc.line}\n" +
      ("\t" * tabLevel) + "return"
    case ReturnStmt(loc, Some(EmptyExpr(_))) =>
      ("\t" * tabLevel) + s".line ${loc.line}\n" +
        ("\t" * tabLevel) + "return\n"
    case e: Expr => e match {
      case a: AssignExpr =>
        emit(a.rhs,localVars,tabLevel,breakable) + emit(a.lhs,localVars,tabLevel,breakable)
      case ArithmeticExpr(_, left, op, right) =>
        emit(left, localVars, tabLevel + 1, breakable)    +
          emit(right, localVars, tabLevel + 1, breakable) +
          ("\t" * (tabLevel + 1)) + (op match {
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
      case EqualityExpr(_, left, op, right) =>
        emit(left, localVars, tabLevel + 1) +
          emit(right, localVars, tabLevel + 1) +
          (op match {
            case ASTOperator(_, "==") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType => // the following is a TERRIBLE HACK
                  val lab = rand.nextInt(Integer.MAX_VALUE) // to put a bool on the stack
                                                  // (hey, that rhymes!)
                  ("\t" * (tabLevel + 1)) + s"if_icmpeq\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: StringType | _: NamedType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_acmpeq\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ("\t" * (tabLevel + 1)) + s"dcmpg\n"
              }
            case ASTOperator(_, "!=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmpne\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: StringType | _: NamedType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE);
                  ("\t" * (tabLevel + 1)) + s"if_acmpne\t\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ("\t" * (tabLevel + 1)) + s"dcmpg\n"
              }
          })
      case RelationalExpr(_, left, op, right) =>
        emit(left, localVars, tabLevel + 1) +
          emit(right, localVars, tabLevel + 1) +
          (op match {
            case ASTOperator(_, ">=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE);
                  ("\t" * (tabLevel + 1)) + s"if_icmpge\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, "<=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE);
                  ("\t" * (tabLevel + 1)) + s"if_icmple\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, ">") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE);
                  ("\t" * (tabLevel + 1)) + s"if_icmpgt\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, "<") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt()
                  ("\t" * (tabLevel + 1)) + s"if_icmplt\tCmp$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\t\tCmpDone$lab\n" +
                    ("\t" * tabLevel) + s"Cmp$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
          })
      case PostfixExpr(loc, op, field: FieldAccess) => field match {
        case FieldAccess(_,None,ASTIdentifier(_, name)) =>
        // special case for local vars using "iinc" bytecode
        // should only be valid if the right-hand side is a field access
        ("\t" * tabLevel) + s".line ${loc.line}\n"          +
          ("\t" * tabLevel) + s"iinc\t${localVars(name)}\t" +
          (op match {
              case ASTOperator(_, "++") =>  "0x01"
              case ASTOperator(_, "--") =>  "0xFF"
            }) + "\n"
        case _ => ??? //todo: implement postfix incrdecr on non-local fields
      }
      case LogicalExpr(_, Some(left), op, right) =>
        emit(left, localVars, tabLevel + 1) +
          emit(right, localVars, tabLevel + 1) + (op match {
            // this works because we're assuming logical exprs
            // can only happen when there are two bools on the stack
            // ...I hope
          case ASTOperator(_, "&&") => ("\t" * (tabLevel + 1)) + "iand\n"
          case ASTOperator(_,"||") => ("\t" * (tabLevel + 1)) + "ior\n"
        })
      case LogicalExpr(_, None, op, right) =>
        // AFAIK, unary-not is the only unary logical expr
        emit(right, localVars, tabLevel + 1)    +
          ("\t" * (tabLevel + 1)) + "ldc 0x1\n" +
          ("\t" * (tabLevel + 1)) + "ixor\n"

      case ASTIntConstant(_, value) => ("\t" * tabLevel) + s"ldc\t\t0x${value.toHexString}\n"
      case ASTBoolConstant(_, value) => ("\t" * tabLevel) + "ldc\t\t0x" + (if (value) 1 else 0) + "\n"
      case FieldAccess(_, None, ASTIdentifier(_,name)) =>
        localVars get name match {
          case Some(varNum) => // it's a local var to the function
            ("\t" * tabLevel) + (e.typeof(getEnclosingScope(e)) match {
              case _: IntType | _: BoolType =>
                if (inAssignExpr (e))   "istore"
                else                    "iload"
              case _: DoubleType =>
                if (inAssignExpr (e))   "dstore"
                else                    "dload"
              case _: StringType | _: NamedType =>
                if (inAssignExpr (e))   "astore"
                else                    "aload"
            }) + s"\t\t$varNum\n"
          case None => // it's a field in the class (NYI)
            val className = e.state.get.table.get("this").get.getName
            ("\t" * tabLevel) + (
              if (inAssignExpr(e))  s"putfield\t\t$className/$name ${emit(e typeof getEnclosingScope(e))}"
              else                  s"getfield\t\t$className/$name ${emit(e typeof getEnclosingScope(e))}"
              ) + "\n"
        }
      case FieldAccess(_, Some(otherClass), ASTIdentifier(_, name)) =>
        // IDK if this is right
        val className = emit(otherClass)
        ("\t" * tabLevel) + (
          if (inAssignExpr(e))  s"putfield\t\t$className/$name ${emit(e typeof getEnclosingScope(e))}"
          else                  s"getfield\t\t$className/$name ${emit(e typeof getEnclosingScope(e))}"
          ) + "\n"
     }
    case l: LoopStmt => l match {
      case WhileStmt(test, body) =>
        val label = rand.nextInt(Integer.MAX_VALUE)
        ("\t" * tabLevel) + s"LoopBegin$label:\n"                     +
          emit(body, localVars, tabLevel + 1, Some(label.toString))   +
          emit(test, localVars, tabLevel + 1, Some(label.toString))   +
          ("\t" * (tabLevel + 1)) + "ldc\t\t0x1\n"                    +
          ("\t" * (tabLevel + 1)) + s"if_icmpeq\t\tLoopBegin$label\n" +
          ("\t" * tabLevel) + s"End$label:\n"
      case ForStmt(init,test,step,body) =>
        val label = rand.nextInt(Integer.MAX_VALUE)
        (init match {
          case Some(_: EmptyExpr) => ""
          case Some(expr: Expr) => emit(expr,localVars,tabLevel+1)
          case None => ""
        })                                                            +
          ("\t" * tabLevel) + s"LoopBegin$label:\n"                   +
          emit(body,localVars,tabLevel+1,Some(label.toString))        +
          (step match {
            case Some(_: EmptyExpr) => ""
            case Some(expr: Expr) => emit(expr,localVars,tabLevel+1)
            case None => ""
          })                                                          +
          ("\t" * (tabLevel + 1)) + "ldc\t\t0x1\n"                    +
          ("\t" * (tabLevel + 1)) + s"if_icmpeq\t\tLoopBegin$label\n" +
          ("\t" * tabLevel) + s"End$label:\n"
    }

    case IfStmt(test,testBody,None) =>
      val label = rand.nextInt(Integer.MAX_VALUE);
      ("\t" * tabLevel) + s".line ${test.pos.line}\n"           +
        emit(test,localVars,tabLevel+1,breakable)               +
        ("\t" * (tabLevel + 1)) + "ldc\t\t0x1\n"                +
        ("\t" * (tabLevel + 1)) + s"if_icmpne\t\tIfNot$label\n" +
        ("\t" * tabLevel) + s".line ${testBody.pos.line}\n"     +
        emit(testBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * tabLevel) + s"IfNot$label:\n"

    case IfStmt(test,testBody,Some(elseBody)) =>
      // this is special-cased because if I generate the if
      // and if-else statements seperately, I can save a label
      // and a couple of jumps in the if-without-else case.
      val label= rand.nextInt(Integer.MAX_VALUE);
      ("\t" * tabLevel) + s".line ${test.pos.line}\n"           +
        emit(test,localVars,tabLevel+1,breakable)               +
        ("\t" * (tabLevel + 1)) + "ldc\t\t0x1\n"                +
        ("\t" * (tabLevel + 1)) + s"if_icmpeq\t\tIf$label\n"    +
        ("\t" * (tabLevel + 1)) + s"goto\t\tIfElse$label\n"     +
        ("\t" * tabLevel) + s"If$label:\n"                      +
        ("\t" * tabLevel) + s".line ${testBody.pos.line}\n"     +
        emit(testBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * (tabLevel + 1)) + s"goto\t\tIfDone$label\n"     +
        ("\t" * tabLevel) + s"IfElse$label:\n"                  +
        ("\t" * tabLevel) + s".line ${elseBody.pos.line}\n"     +
        emit(elseBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * tabLevel) + s"IfDone$label:\n"

    case s: Stmt => ("\t" * tabLevel) + s".line ${s.pos.line}\n" + (s match {
      case PrintStmt(exprs, _) => exprs match {
        case e :: Nil => ("\t" * (tabLevel + 1)) + "getstatic\t\tjava/lang/System/out Ljava/io/PrintStream;\n" +
          emit(e, localVars, tabLevel) +
          ("\t" * (tabLevel + 1)) + s"invokevirtual\t\tjava/io/PrintStream/print(${emit(e typeof getEnclosingScope(e) )})V\n"
        case _ => exprs.foreach(emit(_, localVars, tabLevel, breakable))
      }
      case BreakStmt(_) => breakable match {
        case Some(label) => ("\t" * tabLevel) + s"goto\t\tEnd$label\n"
        case None => // this shouldn't happen
          throw new IllegalArgumentException(s"got break statement while not inside loop or switch\n$s")
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
    case _ => println(s"ignored $node"); ""

  }

}
