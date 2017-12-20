/**
 * @author smallufo
 * Created on 2007/12/31 at 上午 3:38:12
 */
package destiny.astrology.classical.rules.debilities

import destiny.astrology.Aspect.OPPOSITION
import destiny.astrology.AspectEffectiveModern
import destiny.astrology.Horoscope
import destiny.astrology.Planet
import destiny.astrology.Planet.MARS
import destiny.astrology.Planet.SATURN

/** Partile opposite Mars or Saturn.  */
class Partile_Oppo_Mars_Saturn : Rule() {

  private val aspect = OPPOSITION

  override fun getResult(planet: Planet, h: Horoscope): Pair<String, Array<Any>>? {
    val planetDeg = h.getPosition(planet)?.lng
    val marsDeg = h.getPosition(MARS)?.lng
    val saturnDeg = h.getPosition(SATURN)?.lng

    return planetDeg?.let {
      val marResult = marsDeg?.takeIf {
        planet !== MARS && AspectEffectiveModern.isEffective(planetDeg, marsDeg, aspect, 1.0)
      }?.let { "comment" to arrayOf(planet, MARS, aspect) }

      val satResult = saturnDeg?.takeIf {
        planet != SATURN && AspectEffectiveModern.isEffective(planetDeg, saturnDeg, aspect, 1.0)
      }?.let { "comment" to arrayOf(planet, SATURN, aspect) }

      if (marResult != null)
        return@let marResult
      else
        return@let satResult
    }
  }
}