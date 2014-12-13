package decaf.backend

import decaf.AST.Program

/**
 * Common traits for backends.
 * Created by hawk on 12/13/14.
 */
trait Backend {
  /**
   * Attempt to perform code generation on the AST
   * @param program a Program (top-level node in an AST) to compile
   * @return a String containing the assembly output from the AST
   */
  def compile(program: Program): String
}
