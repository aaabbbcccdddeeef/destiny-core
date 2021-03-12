/**
 * Created by smallufo on 2017-04-09.
 */
package destiny.tools

object ArrayTools {

  operator fun <T> get(array: Array<T>, index: Int): T {
    val length = array.size
    return when {
      index < 0 -> get(array, Math.floorMod(index, length))
      index >= length -> get(array, index % length)
      else -> array[index]
    }
  }
}
