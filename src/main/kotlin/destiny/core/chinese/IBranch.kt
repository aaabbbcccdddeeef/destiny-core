/**
 * @author smallufo
 * Created on 2005/7/5 at 上午 11:35:32
 */
package destiny.core.chinese

interface IBranch<T> {

  val branch: Branch

  fun getAheadOf(other: T): Int
}
