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
import decaf.AST.annotations.MethodAnnotation
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

  // this is a hack to put the Decaf readInt/readLine methods
  // at the top of every class's namespace. I have to do it this way
  // because the Java stdlib is sufficiently braindead that reading
  // an int from stdin requires creating two objects and like five
  // method calls.
  private val readInt = ".method public static readInt()I\n" +
    "\t.limit stack 5\n" +
    "\t.limit locals 1\n" +
    "\t.line 4\n" +
    "ReadIntBegin:\n" +
    "\tnew\tjava/io/BufferedReader\n" +
    "\tdup\n" +
    "\tnew\tjava/io/InputStreamReader\n" +
    "\tdup\n" +
    "\tgetstatic\tjava/lang/System/in Ljava/io/InputStream;\n" +
    "\tinvokespecial\tjava/io/InputStreamReader/<init>(Ljava/io/InputStream;)V\n" +
    "\tinvokespecial\tjava/io/BufferedReader/<init>(Ljava/io/Reader;)V\n" +
    "\tinvokevirtual\tjava/io/BufferedReader/readLine()Ljava/lang/String;\n" +
    "\tinvokestatic\tjava/lang/Integer/parseInt(Ljava/lang/String;)I\n" +
    "ReadIntReturn:\n" +
    "\tireturn\n" +
    "ReadIntCatch:\n" +
    "\tastore_0\n" +
    "\ticonst_0\n" +
    "\tireturn\n" +
    ".catch java/lang/Exception from ReadIntBegin to ReadIntReturn using ReadIntCatch\n" +
    ".end method\n"

  private val readLine = ".method public static readLine()Ljava/lang/String;\n" +
    "\t.limit stack 5\n" +
    ".limit locals 1\n" +
    "ReadLineBegin:\n" +
    "\tnew java/io/BufferedReader\n" +
    "\tdup\n" +
    "\tnew\tjava/io/InputStreamReader\n" +
    "\tdup\n" +
    "\tgetstatic\tjava/lang/System/in Ljava/io/InputStream;\n" +
    "\tinvokespecial\tjava/io/InputStreamReader/<init>(Ljava/io/InputStream;)V\n" +
    "\tinvokespecial\tjava/io/BufferedReader/<init>(Ljava/io/Reader;)V\n" +
    "\tinvokevirtual\tjava/io/BufferedReader/readLine()Ljava/lang/String;\n" +
    "ReadLineReturn:\n" +
    "\tareturn\n" +
    "ReadLineCatch:\n" +
    "\tastore_0\n" +
    "\tldc\t\"Fail\"\n" +
    "\tareturn\n" +
    ".catch\tjava/lang/Exception from ReadLineBegin to ReadLineReturn using ReadLineCatch" +
    "\n.end method  "

  override def compile(program: Program, fileName: Option[String]): String =
    s"${makeHeader(fileName.getOrElse("Program"))}\n${emit(fileName.getOrElse("Program"), program)}"
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
    s".source $name.decaf\n.class public $name\n.super $sup\n${makeInitializer(sup)}$readInt\n$readLine\n"

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
  private def inAssignExpr(node: ASTNode): Boolean =
    node.parent match {
    case AssignExpr(_,lhs,_) => lhs == node
    case _ => false
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
  private def emit(className: String,
                   node: ASTNode,
                   localVars: mutable.Map[String, Int] = mutable.Map[String, Int](),
                   tabLevel: Int = 0,
                   breakable: Option[String] = None): String = node match {
    // To those who are about to read my code, I am terribly, terribly sorry.
    // We thank you for your sacrifice.
    //    ~ hawk
    case Program(decls, _) => decls.foldLeft("")((acc, decl) => acc + emit(className, decl))
    case VarDecl(n, t) => node.parent match {
      case _: Program => s".field public $n ${emit(className, t)}\n"
      case _ =>
        var fnName = getFnName(node)
        localVars += (n.name -> getNextVar(localVars))
        s".var ${localVars(n.name)} is ${n.name} ${emit(className,t)} from Begin$fnName to End$fnName\n"

    } // TODO:
    case FnDecl(ASTIdentifier(_,name), rt, args, Some(code)) =>
      s".method public static $name(" +
        (
          if (name == "main") "[Ljava/lang/String;"
          else {args.map(v => emit(className,v.t)).mkString}
          ) +
        s")${emit(className,rt,localVars,tabLevel)}\n"+
      s"\n.limit locals ${code.decls.length + args.length + (if (name == "main") 1 else 0)}\n" +
      s".limit stack 5\n" + //TODO: dynamically figure out stack sizes
      args.map(v=> emit(className,v,localVars)).mkString    +
      s"Begin${node.state.get.boundName}:\n"      +
      s"${emit(className,code, localVars, tabLevel + 1)}\n" +
      s"End${node.state.get.boundName}:\n${if (name == "main") "return\n" else ""}.end method\n"
    case FnDecl(name, rt, args, None) => ??? //NYI: interfaces aren't implemented
    case StmtBlock(declarations, code, _) =>
      declarations
        .map(emit(className,_, localVars, tabLevel, breakable))
        .mkString("\n") +
      code
        .map(emit(className,_, localVars, tabLevel, breakable))
        .mkString("\n")
    case ReturnStmt(loc, None) =>
      ("\t" * tabLevel) + s".line ${loc.line}\n" +
      ("\t" * tabLevel) + "return"
    case ReturnStmt(loc, Some(EmptyExpr(_))) =>
      ("\t" * tabLevel) + s".line ${loc.line}\n" +
        ("\t" * tabLevel) + "return\n"
    case ReturnStmt(loc, Some(expr)) =>
      emit(className,expr,localVars, tabLevel, breakable) +
        ("\t" * tabLevel) + (expr.typeof(getEnclosingScope(expr)) match {
          case _:DoubleType => "dreturn\n"
          case _:IntType | _:BoolType=> "ireturn\n"
          case _:ArrayType | _:NamedType | _:StringType =>"areturn\n"

      })
    case e: Expr => e match {
      case a: AssignExpr =>
        emit(className,a.rhs,localVars,tabLevel,breakable) + emit(className,a.lhs,localVars,tabLevel,breakable)
      case ArithmeticExpr(_, left, op, right) =>
        emit(className,left, localVars, tabLevel + 1, breakable)    +
          emit(className,right, localVars, tabLevel + 1, breakable) +
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
        emit(className,left, localVars, tabLevel + 1) +
          emit(className,right, localVars, tabLevel + 1) +
          (op match {
            case ASTOperator(_, "==") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType => // the following is a TERRIBLE HACK
                  val lab = rand.nextInt(Integer.MAX_VALUE) // to put a bool on the stack
                                                  // (hey, that rhymes!)
                  ("\t" * (tabLevel + 1)) + s"if_icmpeq\tCmpEQ$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpEQDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpEQ$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpEQDone$lab:\n"
                case _: StringType | _: NamedType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_acmpeq\tCmpEQ$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpEQDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpEQ$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpEQDone$lab:\n"
                case _: DoubleType => ("\t" * (tabLevel + 1)) + s"dcmpg\n"
              }
            case ASTOperator(_, "!=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmpne\tCmpNE$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpNEDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpNE$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpNEDone$lab:\n"
                case _: StringType | _: NamedType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_acmpne\tCmpNE$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpNEDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpNE$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpNEDone$lab:\n"
                case _: DoubleType => ("\t" * (tabLevel + 1)) + s"dcmpg\n"
              }
          })
      case RelationalExpr(_, left, op, right) =>
        emit(className,left, localVars, tabLevel + 1) +
          emit(className,right, localVars, tabLevel + 1) +
          (op match {
            case ASTOperator(_, ">=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmpge\tCmpGE$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpGEDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpGE$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpGEDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, "<=") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmple\tCmpLE$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpLEDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpLE$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpLEDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, ">") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmpgt\tCmpGT$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpGTDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpGT$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpGTDone$lab:\n"
                case _: DoubleType => ??? //todo: implement for doubles
              }
            case ASTOperator(_, "<") =>
              e.typeof(getEnclosingScope(e)) match {
                case _: IntType | _: BoolType =>
                  val lab = rand.nextInt(Integer.MAX_VALUE)
                  ("\t" * (tabLevel + 1)) + s"if_icmplt\tCmpLT$lab\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x0\n" +
                    ("\t" * (tabLevel + 1)) + s"goto\tCmpLTDone$lab\n" +
                    ("\t" * tabLevel) + s"CmpLT$lab:\n" +
                    ("\t" * (tabLevel + 1)) + s"ldc\t0x1\n" +
                    ("\t" * tabLevel) + s"CmpLTDone$lab:\n"
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
        emit(className,left, localVars, tabLevel + 1) +
          emit(className,right, localVars, tabLevel + 1) + (op match {
            // this works because we're assuming logical exprs
            // can only happen when there are two bools on the stack
            // ...I hope
          case ASTOperator(_, "&&") => ("\t" * (tabLevel + 1)) + "iand\n"
          case ASTOperator(_,"||") => ("\t" * (tabLevel + 1)) + "ior\n"
        })
      case LogicalExpr(_, None, op, right) =>
        // AFAIK, unary-not is the only unary logical expr
        emit(className,right, localVars, tabLevel + 1)    +
          ("\t" * (tabLevel + 1)) + "ldc 0x1\n" +
          ("\t" * (tabLevel + 1)) + "ixor\n"

      case ASTIntConstant(_, value) => ("\t" * tabLevel) + s"ldc\t0x${value.toHexString.toUpperCase}\n"
      case ASTBoolConstant(_, value) => ("\t" * tabLevel) + "ldc\t0x" + (if (value) 1 else 0) + "\n"
      case ASTStringConstant(_,chars) => ("\t" * tabLevel) + s"ldc\t$chars\n"
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
            }) + s"\t$varNum\n"
          case None => // it's a field in the class (NYI)
            val className = e.state.get.table.get("this").get.getName
            ("\t" * tabLevel) + (
              if (inAssignExpr(e))  s"putfield\t$className/$name ${emit(className,e typeof getEnclosingScope(e))}"
              else                  s"getfield\t$className/$name ${emit(className,e typeof getEnclosingScope(e))}"
              ) + "\n"
        }
      case FieldAccess(_, Some(otherClass), ASTIdentifier(_, name)) =>
        // IDK if this is right
        val owner = emit(className,otherClass)
        ("\t" * tabLevel) + (
          if (inAssignExpr(e))  s"putfield\t$owner/$name ${emit(className,e typeof getEnclosingScope(e))}"
          else                  s"getfield\t$owner/$name ${emit(className,e typeof getEnclosingScope(e))}"
          ) + "\n"
      case r: ReadIntegerExpr =>
          ("\t" * tabLevel) + s"invokestatic\t$className/readInt()I\n"
      case r: ReadLineExpr =>
        ("\t" * tabLevel) + s"invokestatic\t$className/readLine()I\n"
      case Call(loc, None, ASTIdentifier(_,name), exprs) =>
        ("\t" * tabLevel) + s".line ${loc.line}\n"                  +
          exprs.map(emit(className,_,localVars,tabLevel,breakable)).mkString  +
          ("\t" * tabLevel) + (getEnclosingScope(node).table.get(name) match {
            case Some(MethodAnnotation(mname,rt,formals,_)) =>
              s"invokestatic $className/$mname"                   +
                s"(${formals.map(emit(className, _)).mkString})"  +
                s"${emit(className,rt)}"
            case Some(_) => throw new Exception(s"Name $name was not a method")
            case None => throw new Exception(s"Could not find method annotation for $name")
          }) + "\n"
      case Call(loc, base, ASTIdentifier(_,name), exprs) =>
        ("\t" * tabLevel) + s".line ${loc.line}\n"                  +
          exprs.map(emit(className,_,localVars,tabLevel,breakable)).mkString  +
          ("\t" * tabLevel) + (getEnclosingScope(node).table.get(name) match {
          case Some(MethodAnnotation(mname,rt,formals,_)) =>
            s"invokestatic $base/$mname"                        +
              s"(${formals.map(emit(className, _)).mkString})"  +
              s"${emit(className,rt)}"
          case Some(_) => throw new Exception(s"Name $name was not a method")
          case None => throw new Exception(s"Could not find method annotation for $name")
        }) + "\n"

      case Call(loc, base, ASTIdentifier(_,name), exprs) =>
        ("\t" * tabLevel) + s".line ${loc.line}\n"                  +
          exprs.map(emit(className,_,localVars,tabLevel,breakable)).mkString  +
          ("\t" * tabLevel) + (getEnclosingScope(node).table.get(name) match {
          case Some(MethodAnnotation(mname,rt,formals,_)) =>
            // todo: determine whether static/virtual/nonvirtual ...somehow`
            s"invokestatic $base/$mname"                        +
              s"(${formals.map(emit(className, _)).mkString})"  +
              s"${emit(className,rt)}\n"
          case Some(_) => throw new Exception(s"Name $name was not a method")
          case None => throw new Exception(s"Could not find method annotation for $name")
        })
     }
    case l: LoopStmt => l match {
      case WhileStmt(test, body) =>
        val label = rand.nextInt(Integer.MAX_VALUE)
        ("\t" * tabLevel) + s"LoopBegin$label:\n"                     +
          emit(className,body, localVars, tabLevel + 1, Some(label.toString))   +
          emit(className,test, localVars, tabLevel + 1, Some(label.toString))   +
          ("\t" * (tabLevel + 1)) + "ldc\t0x1\n"                    +
          ("\t" * (tabLevel + 1)) + s"if_icmpeq\tLoopBegin$label\n" +
          ("\t" * tabLevel) + s"End$label:\n"
      case ForStmt(init,test,step,body) =>
        val label = rand.nextInt(Integer.MAX_VALUE)
        (init match {
          case Some(_: EmptyExpr) => ""
          case Some(expr: Expr) => emit(className,expr,localVars,tabLevel+1)
          case None => ""
        })                                                            +
          ("\t" * tabLevel) + s"LoopBegin$label:\n"                   +
          emit(className,body,localVars,tabLevel+1,Some(label.toString))        +
          (step match {
            case Some(_: EmptyExpr) => ""
            case Some(expr: Expr) =>
              emit(className,expr,localVars,tabLevel+1, breakable)
            case None => ""
          })                                                          +
          emit(className,test,localVars,tabLevel+1,breakable)                   +
          ("\t" * (tabLevel + 1)) + "ldc\t0x1\n"                    +
          ("\t" * (tabLevel + 1)) + s"if_icmpeq\tLoopBegin$label\n" +
          ("\t" * tabLevel) + s"End$label:\n"
    }

    case IfStmt(test,testBody,None) =>
      val label = rand.nextInt(Integer.MAX_VALUE);
      ("\t" * tabLevel) + s".line ${test.pos.line}\n"           +
        emit(className,test,localVars,tabLevel+1,breakable)               +
        ("\t" * (tabLevel + 1)) + "ldc\t0x1\n"                +
        ("\t" * (tabLevel + 1)) + s"if_icmpne\tIfNot$label\n" +
        ("\t" * tabLevel) + s".line ${testBody.pos.line}\n"     +
        emit(className,testBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * tabLevel) + s"IfNot$label:\n"

    case IfStmt(test,testBody,Some(elseBody)) =>
      // this is special-cased because if I generate the if
      // and if-else statements seperately, I can save a label
      // and a couple of jumps in the if-without-else case.
      val label= rand.nextInt(Integer.MAX_VALUE);
      ("\t" * tabLevel) + s".line ${test.pos.line}\n"           +
        emit(className,test,localVars,tabLevel+1,breakable)               +
        ("\t" * (tabLevel + 1)) + "ldc\t0x1\n"                +
        ("\t" * (tabLevel + 1)) + s"if_icmpeq\tIf$label\n"    +
        ("\t" * (tabLevel + 1)) + s"goto\tIfElse$label\n"     +
        ("\t" * tabLevel) + s"If$label:\n"                      +
        ("\t" * tabLevel) + s".line ${testBody.pos.line}\n"     +
        emit(className,testBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * (tabLevel + 1)) + s"goto\tIfDone$label\n"     +
        ("\t" * tabLevel) + s"IfElse$label:\n"                  +
        ("\t" * tabLevel) + s".line ${elseBody.pos.line}\n"     +
        emit(className,elseBody,localVars,tabLevel + 1, breakable)        +
        ("\t" * tabLevel) + s"IfDone$label:\n"

    case s: Stmt => ("\t" * tabLevel) + s".line ${s.pos.line}\n" + (s match {
      case PrintStmt(exprs, _) => /*exprs match {
        case e :: Nil => ("\t" * (tabLevel + 1)) + "getstatic\tjava/lang/System/out Ljava/io/PrintStream;\n" +
          emit(className,e, localVars, tabLevel) +
          ("\t" * (tabLevel + 1)) + s"invokevirtual\tjava/io/PrintStream/print(${emit(className,e typeof getEnclosingScope(e) )})V\n"
        case _ =>*/
          exprs.map(e =>
            ("\t" * (tabLevel+1)) + "getstatic\tjava/lang/System/out Ljava/io/PrintStream;\n" +
            emit(className,e, localVars, tabLevel+1, breakable) +
              ("\t" * (tabLevel+1)) + s"invokevirtual\tjava/io/PrintStream/print(${emit(className,e typeof getEnclosingScope(e) )})V\n"
          ).mkString + "\n"
      //}
      case BreakStmt(_) => breakable match {
        case Some(label) => ("\t" * tabLevel) + s"goto\tEnd$label\n"
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
      case ArrayType(_, elemType) => s"[${emit(className,elemType)}"
      case _: NamedType => ??? //todo: implement
  }
    case _ => println(s"ignored $node"); ""

  }

}
