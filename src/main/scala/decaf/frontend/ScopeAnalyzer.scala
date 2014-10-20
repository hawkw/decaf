package decaf.frontend
import scala.collection.mutable.{HashMap, Set}
import scala.collection.AbstractMap

/**
 * Created by hawk on 10/20/14.
 */
object ScopeAnalyzer extends DecafAST {

  def scope(ast: ASTNode):

}

/**
 * Scala re-implementation of Max's ClobberTable
 * Created by hawk on 10/15/14.
 */
class ForkTable[K, V](val parent: ForkTable[K, V] = null) extends AbstractMap[K, V] {
  val whiteouts = Set[K]()
  val back = HashMap[K, V]()

  def put(key: K, value: V): Option[V] = {
    if (whiteouts contains key) whiteouts -= key
    back.put(key, value)
  }

  override def get(key: K): Option[V] = if (whiteouts contains key) {
    None
  } else if (parent != null && (parent contains key)) {
    parent get key
  } else {
    back get key
  }

  def remove(key: K): Option[V] = {
    if (back contains key) {
      back remove key
    }
    else {
      if (parent contains key) whiteouts += key
      None
    }
  }

  override def + (kv: (K, V)): ForkTable[K, V] = {
    put(kv._1, kv._2); this
  }

  override def - (key: K): ForkTable[K, V] = {
    remove(key); this
  }

  override def iterator = back.iterator

  override def contains(key: K): Boolean = (back contains key) || (parent != null && (parent contains key))

  override def apply(key: K) = back(key)

  def fork(): ForkTable[K, V] = new ForkTable[K, V](parent = this)
}