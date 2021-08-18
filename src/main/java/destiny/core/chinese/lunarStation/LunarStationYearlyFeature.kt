/**
 * Created by smallufo on 2021-08-19.
 */
package destiny.core.chinese.lunarStation

import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver
import destiny.core.calendar.TimeTools
import destiny.core.calendar.chinese.IChineseDate
import destiny.core.calendar.eightwords.IDayHour
import destiny.core.calendar.eightwords.YearFeature
import destiny.core.chinese.StemBranch
import destiny.core.chinese.YearType
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import destiny.tools.Feature
import kotlinx.serialization.Serializable
import java.time.temporal.ChronoField


@Serializable
data class YearlyConfig(val yearType: YearType = YearType.YEAR_SOLAR,
                        val yearEpoch: YearEpoch = YearEpoch.EPOCH_1564)

@DestinyMarker
class YearlyConfigBuilder : Builder<YearlyConfig> {
  var yearType: YearType = YearType.YEAR_SOLAR
  var yearEpoch: YearEpoch = YearEpoch.EPOCH_1564

  override fun build(): YearlyConfig {
    return YearlyConfig(yearType, yearEpoch)
  }

  companion object {
    fun yearly(block: YearlyConfigBuilder.() -> Unit = {}) : YearlyConfig {
      return YearlyConfigBuilder().apply(block).build()
    }
  }
}

class LunarStationYearlyFeature(private val yearFeature: YearFeature,
                                private val chineseDateImpl: IChineseDate,
                                private val dayHourImpl: IDayHour,
                                private val julDayResolver: JulDayResolver) : Feature<YearlyConfig, YearIndex> {
  override val key: String = "lsYearly"

  override val defaultConfig: YearlyConfig = YearlyConfig()

  override fun getModel(gmtJulDay: GmtJulDay, loc: ILocation, config: YearlyConfig): YearIndex {

    val lmt = TimeTools.getLmtFromGmt(gmtJulDay, loc, julDayResolver)

    val epoch = when (config.yearEpoch) {
      YearEpoch.EPOCH_1564 -> 1564
      YearEpoch.EPOCH_1864 -> 1864
    }

    val diffValue = lmt.get(ChronoField.YEAR) - epoch


    val (yearSb, yearSb2) = if (config.yearType == YearType.YEAR_SOLAR) {
      // 節氣立春換年
      val yearSb: StemBranch = yearFeature.getModel(gmtJulDay, loc)
      // 以七月再算一次 年干支
      val yearSb2: StemBranch = yearFeature.getModel(lmt.with(ChronoField.MONTH_OF_YEAR, 7), loc)
      yearSb to yearSb2
    } else {
      // 陰曆初一換年
      val yearSb = chineseDateImpl.getChineseDate(lmt, loc, dayHourImpl).year
      // 以七月再算一次 年干支
      val yearSb2: StemBranch =
        chineseDateImpl.getChineseDate(lmt.with(ChronoField.MONTH_OF_YEAR, 7), loc, dayHourImpl).year
      yearSb to yearSb2
    }

    val value = (if (yearSb == yearSb2)
      diffValue
    else
      diffValue - 1).let {
      it % 420
    }.let {
      if (it < 0)
        it + 420
      else
        it
    }

    return YearIndex(value, epoch)

  }
}
