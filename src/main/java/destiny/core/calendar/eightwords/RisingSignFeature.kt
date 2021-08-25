/**
 * Created by smallufo on 2021-08-13.
 */
package destiny.core.calendar.eightwords

import destiny.core.astrology.*
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.chinese.Branch
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import destiny.tools.Feature
import kotlinx.serialization.Serializable

@Serializable
data class TradChineseRisingSignConfig(
  val hourImpl: HourBranchConfig.HourImpl = HourBranchConfig.HourImpl.TST
): java.io.Serializable

@Serializable
data class RisingSignConfig(
  val houseConfig: HouseConfig = HouseConfig(),
  val tradChineseRisingSignConfig: TradChineseRisingSignConfig = TradChineseRisingSignConfig(),
  val impl: Impl = Impl.HouseCusp
): java.io.Serializable {
  enum class Impl {
    HouseCusp,
    TradChinese
  }
}

@DestinyMarker
class RisingSignConfigBuilder : Builder<RisingSignConfig> {

  var impl: RisingSignConfig.Impl = RisingSignConfig.Impl.HouseCusp

  private var houseConfig: HouseConfig = HouseConfig()
  fun houseCusp(block: HouseConfigBuilder.() -> Unit = {}) {
    houseConfig = HouseConfigBuilder.houseCusp(block)
    impl = RisingSignConfig.Impl.HouseCusp
  }

  @DestinyMarker
  class TradChineseRisingSignConfigBuilder : Builder<TradChineseRisingSignConfig> {

    var hourImpl : HourBranchConfig.HourImpl = HourBranchConfig.HourImpl.TST

    override fun build(): TradChineseRisingSignConfig {
      return TradChineseRisingSignConfig(hourImpl)
    }
  }

  private var tradChineseRisingSignConfig: TradChineseRisingSignConfig = TradChineseRisingSignConfig()
  fun tradChinese(block: TradChineseRisingSignConfigBuilder.() -> Unit = {}) {
    tradChineseRisingSignConfig = TradChineseRisingSignConfigBuilder().apply(block).build()
    impl = RisingSignConfig.Impl.TradChinese
  }

  override fun build(): RisingSignConfig {
    return RisingSignConfig(houseConfig, tradChineseRisingSignConfig, impl)
  }

  companion object {
    fun risingSign(block: RisingSignConfigBuilder.() -> Unit = {}): RisingSignConfig {
      return RisingSignConfigBuilder().apply(block).build()
    }
  }
}


class RisingSignFeature(
  private val houseCuspFeature: IHouseCuspFeature,
  private val tradChineseRisingSignFeature: TradChineseRisingSignFeature
) : Feature<RisingSignConfig, ZodiacSign> {

  class TradChineseRisingSignFeature(private val starPositionImpl: IStarPosition<*>,
                                     private val dayHourFeature: IDayHourFeature) : Feature<TradChineseRisingSignConfig , ZodiacSign> {
    override val key: String = "tradChinese"

    override val defaultConfig: TradChineseRisingSignConfig = TradChineseRisingSignConfig()

    override fun getModel(gmtJulDay: GmtJulDay, loc: ILocation, config: TradChineseRisingSignConfig): ZodiacSign {
      // 太陽星座
      val sunSign = starPositionImpl.getPosition(Planet.SUN, gmtJulDay, Centric.GEO, Coordinate.ECLIPTIC).lngDeg.sign

      // 時支

      val hour = dayHourFeature.getModel(gmtJulDay, loc, DayHourConfig(hourBranchConfig = HourBranchConfig(hourImpl = config.hourImpl))).second.branch

      val gap = (hour.index - sunSign.branch.index).let {
        if (it < 0 )
          it + 12
        else
          it
      }

      val target = (Branch.卯.index - gap)

      val resultEB = Branch[target]

      return ZodiacSign.of(resultEB)
    }
  }


  override val key: String = "risingSign"

  override val defaultConfig: RisingSignConfig = RisingSignConfig()

  override fun getModel(gmtJulDay: GmtJulDay, loc: ILocation, config: RisingSignConfig): ZodiacSign {
    return when (config.impl) {
      RisingSignConfig.Impl.HouseCusp   -> houseCuspFeature.getRisingSign(gmtJulDay, loc, config.houseConfig)
      RisingSignConfig.Impl.TradChinese -> tradChineseRisingSignFeature.getModel(gmtJulDay, loc, config.tradChineseRisingSignConfig)
    }
  }
}
