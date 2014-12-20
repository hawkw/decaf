/*                                        *\
**                                        **
** Decaf: Like Java but less so           **
**                                        **
**    by Hawk Weisman & Max Clive         **
**    for Prof. Jumadinova's CMPSC420     **
**    at Allegheny College                **
**                                        **
\*                                        */

package decaf

import language.implicitConversions

import com.meteorcode.common.ForkTable
import decaf.AST.annotations.TypeAnnotation
import decaf.frontend.TypeErrorException

/**
 * Contains the Decaf Abstract Syntax Tree and related types
 * Created by hawk on 11/14/14.
 */
package object AST {
  type ScopeTable = ForkTable[String, TypeAnnotation]
  implicit def errorType2TypeError(e: ErrorType): List[Exception] = e.unpack()
}
