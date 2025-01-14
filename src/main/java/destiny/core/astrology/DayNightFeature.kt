/**
 * Created by smallufo on 2021-08-18.
 */
package destiny.core.astrology

import destiny.core.DayNight
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.tools.AbstractCachedFeature
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import jakarta.inject.Named
import kotlinx.serialization.Serializable

enum class DayNightImpl {
  StarPos,  // 日升日落 : 太陽升起至落下，為晝；太陽落下至昇起，為夜
  Half,     // 前半天後半天 : 夜半子正至午正（前半天）為晝；中午至半夜（後半天）為夜 (極區內可能不適用)
  Simple    // 固定晝夜六點為界 : 僅能作為 Test 使用
}

@Serializable
data class DayNightConfig(val impl : DayNightImpl = DayNightImpl.StarPos,
                          /** for [DayNightImpl.StarPos] and [DayNightImpl.Half] */
                          val transConfig: TransConfig = TransConfig()): java.io.Serializable

@DestinyMarker
class DayNightConfigBuilder : Builder<DayNightConfig> {

  var impl : DayNightImpl = DayNightImpl.StarPos

  var transConfig: TransConfig = TransConfig()

  fun trans(block : TransConfigBuilder.() -> Unit = {}) {
    transConfig = TransConfigBuilder.trans(block)
  }

  override fun build(): DayNightConfig {
    return DayNightConfig(impl, transConfig)
  }

  companion object {
    fun dayNight(block : DayNightConfigBuilder.() -> Unit = {}) : DayNightConfig {
      return DayNightConfigBuilder().apply(block).build()
    }
  }
}

@Named
class DayNightFeature(private val dayNightImplMap: Map<DayNightImpl, IDayNight>) : AbstractCachedFeature<DayNightConfig, DayNight>() {

  override val key: String = "dayNight"

  override val defaultConfig: DayNightConfig = DayNightConfig()

  override fun calculate(gmtJulDay: GmtJulDay, loc: ILocation, config: DayNightConfig): DayNight {

    return dayNightImplMap[config.impl]!!.getDayNight(gmtJulDay, loc, config.transConfig)
  }
}
