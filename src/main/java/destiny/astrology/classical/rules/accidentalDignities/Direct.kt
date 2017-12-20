/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 4:50:05
 */
package destiny.astrology.classical.rules.accidentalDignities

import destiny.astrology.Horoscope
import destiny.astrology.Planet
import destiny.astrology.Planet.MOON
import destiny.astrology.Planet.SUN

/** Direct in motion (does not apply to Sun and Moon).  */
class Direct : Rule() {

  override fun getResult(planet: Planet, h: Horoscope): Pair<String, Array<Any>>? {
    return planet.takeIf { it !== SUN && it !== MOON }
      ?.let { h.getPosition(it) }?.speedLng
      ?.takeIf { it > 0 }
      ?.let { "comment" to arrayOf<Any>(planet) }
  }
}