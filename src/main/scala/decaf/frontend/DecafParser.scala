package decaf.frontend

/**
 * Wrapper for parsing whole programs into Decaf ASTs
 * Created by hawk on 10/1/14.
 */
object DecafParser extends DecafSyntactical {

  def parseAll(source: String): Option[Program] =
    phrase(program)(new lexical.Scanner(source)) match {
      case Success(result, _) => Some(result)
      case _ => None
    }

}
