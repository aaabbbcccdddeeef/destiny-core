/**
 * Created by smallufo on 2021-08-15.
 */
package destiny.core.astrology.classical

import destiny.core.astrology.*
import destiny.core.astrology.classical.rules.Misc.VoidCourse
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.GmtJulDay.Companion.toGmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver
import destiny.tools.AbstractCachedFeature
import destiny.tools.Builder
import destiny.tools.Feature
import destiny.tools.serializers.AstroPointSerializer
import jakarta.inject.Named
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import kotlin.math.min

enum class VoidCourseImpl {
  Hellenistic,
  Medieval,
  WilliamLilly
}

@Serializable
data class VoidCourseConfig(@Serializable(with = AstroPointSerializer::class)
                            val planet: Planet = Planet.MOON,
                            val centric: Centric = Centric.GEO,
                            val vocImpl: VoidCourseImpl = VoidCourseImpl.Medieval): java.io.Serializable

class VoidCourseConfigBuilder : Builder<VoidCourseConfig> {
  var planet: Planet = Planet.MOON
  var centric: Centric = Centric.GEO
  var impl: VoidCourseImpl = VoidCourseImpl.Medieval

  override fun build(): VoidCourseConfig {
    return VoidCourseConfig(planet, centric, impl)
  }

  companion object {
    fun voidCourse(block : VoidCourseConfigBuilder.() -> Unit = {}) : VoidCourseConfig {
      return VoidCourseConfigBuilder().apply(block).build()
    }
  }

}

/**
 * 星體空亡 interface
 */
interface IVoidCourseFeature : Feature<VoidCourseConfig , VoidCourse?> {

  fun getVocMap(gmtJulDay: GmtJulDay, loc: ILocation , points: Collection<AstroPoint> , config: VoidCourseConfig): Map<Planet, VoidCourse> {
    return points.filterIsInstance<Planet>()
      .map { planet ->

        planet to getModel(gmtJulDay, loc, config.copy(planet = planet))
      }
      .filter { (_, voc) -> voc != null }
      .associate { (planet, voc) -> planet to voc!! }
  }

  fun getVoidCourses(fromGmt: GmtJulDay, toGmt: GmtJulDay, loc: ILocation, relativeTransitImpl: IRelativeTransit, config: VoidCourseConfig): List<VoidCourse>
}

/**
 * 星體空亡
 */
@Named
class VoidCourseFeature(private val vocMap: Map<VoidCourseImpl, IVoidCourse>,
                        private val pointPosFuncMap: Map<AstroPoint, IPosition<*>>,
                        private val julDayResolver: JulDayResolver) : IVoidCourseFeature , AbstractCachedFeature<VoidCourseConfig , VoidCourse?>() {
  
  override val key: String = "voidCourse"

  override val defaultConfig: VoidCourseConfig = VoidCourseConfig()

  override fun calculate(gmtJulDay: GmtJulDay, loc: ILocation, config: VoidCourseConfig): VoidCourse? {

    return vocMap[config.vocImpl]!!.getVoidCourse(gmtJulDay, loc, pointPosFuncMap, config.planet, config.centric)
  }

  override fun getVoidCourses(fromGmt: GmtJulDay, toGmt: GmtJulDay, loc: ILocation, relativeTransitImpl: IRelativeTransit, config: VoidCourseConfig): List<VoidCourse> {

    val planets = Planet.classicalList
    val aspects = Aspect.getAspects(Aspect.Importance.HIGH)

    fun getNextVoc(gmt : GmtJulDay): VoidCourse? {

      return relativeTransitImpl.getNearestRelativeTransitGmtJulDay(config.planet, planets, gmt, aspects, true)
        ?.takeIf { nextAspectData -> nextAspectData.gmtJulDay < toGmt }
        ?.takeIf { nextAspectData -> nextAspectData.gmtJulDay > fromGmt }
        ?.let { nextAspectData ->
          logger.trace { "接下來將在 ${julDayResolver.getLocalDateTime(nextAspectData.gmtJulDay)} 與 ${nextAspectData.points} 形成 ${nextAspectData.aspect}" }

          val nextTime: GmtJulDay = nextAspectData.gmtJulDay + 0.01
          logger.trace { "推進計算時刻 ${julDayResolver.getLocalDateTime(nextTime)}" }
          getModel(nextTime , loc , config)?:getNextVoc(nextTime)
        }
    }

    fun getVoc(gmt: GmtJulDay , config: VoidCourseConfig) : VoidCourse? {
      val gmtVoc: VoidCourse? = getModel(gmt, loc , config)

      return if (gmtVoc == null) {
        logger.trace { "沒有 VOC : ${julDayResolver.getLocalDateTime(gmt)} " }

        getNextVoc(gmt)?.takeIf { nextVoc ->
          nextVoc.beginGmt < toGmt
        }
      } else {
        logger.trace { "免進下一步，直接得到 VOC , 開始於 ${gmtVoc.beginGmt.let { julDayResolver.getLocalDateTime(it) }}" }
        gmtVoc
      }
    }

    return generateSequence(getVoc(fromGmt , config)) {
      val newGmt = (min(it.endGmt.value, it.exactAspectAfter.gmtJulDay.value) + 0.01).toGmtJulDay()
      if (newGmt < toGmt) {
        getVoc(newGmt, config)?.takeIf { voc -> voc.beginGmt < toGmt }
      } else
        null
    }.toList()
  }

  companion object {
    private val logger = KotlinLogging.logger { }
  }

}
