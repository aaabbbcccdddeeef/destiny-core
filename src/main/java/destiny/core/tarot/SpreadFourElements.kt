/**
 * Created by smallufo on 2023-04-05.
 */
package destiny.core.tarot

import destiny.core.astrology.Element
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
@SerialName("4ElementsSpread")
data class SpreadFourElements(val fire : CardOrientation,
                              val water : CardOrientation,
                              val earth : CardOrientation,
                              val air : CardOrientation) : ISpread {
  override fun getTitle(locale: Locale): String {
    return when (locale.language) {
      "en" -> "The Four Elements Spread"
      "ja" -> "四元素スプレッド"
      else -> "四要素"
    }
  }

  fun getCardOrientation(element: Element): CardOrientation {
    return when (element) {
      Element.FIRE -> fire
      Element.WATER -> water
      Element.EARTH -> earth
      Element.AIR -> air
    }
  }

  companion object {
    fun of(map : Map<Element, CardOrientation>): SpreadFourElements {
      require(map.size == 4)
      return SpreadFourElements(map[Element.FIRE]!!, map[Element.WATER]!!, map[Element.EARTH]!!, map[Element.AIR]!!)
    }
  }
}
