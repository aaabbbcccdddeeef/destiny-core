/**
 * Created by smallufo on 2021-08-15.
 */
package destiny.core.astrology

import destiny.core.astrology.classical.IVoidCourseFeature
import destiny.core.astrology.classical.VoidCourseConfig
import destiny.core.astrology.classical.VoidCourseImpl
import destiny.core.astrology.classical.rules.Misc
import destiny.core.astrology.prediction.IProgressionModel
import destiny.core.astrology.prediction.ProgressedAspect
import destiny.core.astrology.prediction.ProgressionModel
import destiny.core.astrology.prediction.ProgressionSecondary
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver1582CutoverImpl
import destiny.tools.AbstractCachedFeature
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import destiny.tools.Feature
import destiny.tools.serializers.AstroPointSerializer
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import javax.cache.Cache
import javax.inject.Named


@Serializable
data class HoroscopeConfig(
  val points: Set<@Serializable(with = AstroPointSerializer::class) AstroPoint> = setOf(*Planet.values, *LunarNode.values, Axis.RISING, Axis.MERIDIAN),
  val houseSystem: HouseSystem = HouseSystem.PLACIDUS,
  val coordinate: Coordinate = Coordinate.ECLIPTIC,
  val centric: Centric = Centric.GEO,
  val temperature: Double = 0.0,
  val pressure: Double = 1013.25,
  val vocImpl: VoidCourseImpl = VoidCourseImpl.Medieval,
  val place: String? = null
): java.io.Serializable

@DestinyMarker
class HoroscopeConfigBuilder : Builder<HoroscopeConfig> {
  var points: Set<AstroPoint> = setOf(*Planet.values, *LunarNode.values)
  var houseSystem: HouseSystem = HouseSystem.PLACIDUS
  var coordinate: Coordinate = Coordinate.ECLIPTIC
  var centric: Centric = Centric.GEO
  var temperature: Double = 0.0
  var pressure: Double = 1013.25
  var vocImpl: VoidCourseImpl = VoidCourseImpl.Medieval
  var place: String? = null

  override fun build(): HoroscopeConfig {
    return HoroscopeConfig(points, houseSystem, coordinate, centric, temperature, pressure, vocImpl, place)
  }

  companion object {
    fun horoscope(block : HoroscopeConfigBuilder.() -> Unit = {}) : HoroscopeConfig {
      return HoroscopeConfigBuilder().apply(block).build()
    }
  }
}


interface IHoroscopeFeature : Feature<HoroscopeConfig, IHoroscopeModel> {

  /**
   * secondary progression calculation
   */
  fun getSecondaryProgression(model: IHoroscopeModel, progressionTime: GmtJulDay, aspects: Collection<Aspect>,
                              aspectsCalculator : IAspectsCalculator , config: HoroscopeConfig, converse: Boolean = false) : IProgressionModel {
    val progression = ProgressionSecondary(converse)
    val posMapInner = model.positionMap

    return progression.getConvergentTime(model.gmtJulDay, progressionTime).let { convergentTime ->

      logger.trace { "convergentTime = $convergentTime" }

      logger.info { "convergentGmt = ${JulDayResolver1582CutoverImpl().getLocalDateTime(convergentTime)}" }

      val convergentModel = getModel(convergentTime , model.location, config)

      val posMapOuter = convergentModel.positionMap

      // 2.4 hours later
      val later = progressionTime.plus(0.1)
      progression.getConvergentTime(model.gmtJulDay, later).let { laterConvergentTime ->
        val laterModel = getModel(laterConvergentTime, model.location, config)
        val posMapLater = laterModel.positionMap


        val progressedAspects = config.points.asSequence().flatMap { p1 -> config.points.asSequence().map { p2 -> p1 to p2 } }
          .mapNotNull { (p1,p2) ->
            aspectsCalculator.getAspectData(p1, p2, posMapOuter, posMapInner, { posMapLater[p1] }, { posMapInner[p2] }, aspects)?.let { ad: AspectData ->
              ProgressedAspect(p1, p2 , ad.aspect, ad.orb, ad.type!!, ad.score)
            }
          }.toSet()

        ProgressionModel(model.gmtJulDay, progressionTime, convergentTime, progressedAspects)
      }
    }
  }


  companion object {
    private val logger = KotlinLogging.logger { }
  }
}

@Named
class HoroscopeFeature(private val pointPosFuncMap: Map<AstroPoint, IPosition<*>> ,
                       private val houseCuspFeature: IHouseCuspFeature,
                       private val voidCourseFeature: IVoidCourseFeature,
                       @Transient
                       private val horoscopeFeatureCache : Cache<GmtCacheKey<*>, IHoroscopeModel>) : AbstractCachedFeature<HoroscopeConfig, IHoroscopeModel>(), IHoroscopeFeature {
  override val key: String = "horoscope"

  override val defaultConfig: HoroscopeConfig = HoroscopeConfig()

  @Suppress("UNCHECKED_CAST")
  override val gmtCache: Cache<GmtCacheKey<HoroscopeConfig>, IHoroscopeModel>
    get() = horoscopeFeatureCache as Cache<GmtCacheKey<HoroscopeConfig>, IHoroscopeModel>

  override fun calculate(gmtJulDay: GmtJulDay, loc: ILocation, config: HoroscopeConfig): IHoroscopeModel {

    val positionMap: Map<AstroPoint, IPosWithAzimuth> = config.points.map { point ->
      point to pointPosFuncMap[point]?.getPosition(gmtJulDay, loc, config.centric, config.coordinate, config.temperature, config.pressure)
    }.filter { (_, v) -> v != null }.associate { (point, pos) -> point to pos!! as IPosWithAzimuth }


    // [1] 到 [12] 宮首黃道度數
    val cuspDegreeMap: Map<Int, ZodiacDegree> = houseCuspFeature.getModel(gmtJulDay, loc, HouseConfig(config.houseSystem, config.coordinate))

    // 行星空亡表
    val vocMap: Map<Planet, Misc.VoidCourse> = voidCourseFeature.getVocMap(gmtJulDay, loc, config.points, VoidCourseConfig(vocImpl = config.vocImpl))

    return HoroscopeModel(gmtJulDay, loc, config, positionMap, cuspDegreeMap, vocMap)
  }

  companion object {
    const val CACHE_HOROSCOPE_FEATURE = "horoscopeFeatureCache"
  }
}
