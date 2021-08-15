/**
 * Created by smallufo on 2021-08-12.
 */
package destiny.core.calendar.eightwords

import destiny.core.astrology.IRiseTrans
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver
import destiny.core.calendar.TimeTools
import destiny.core.chinese.Branch
import destiny.core.chinese.Stem
import destiny.core.chinese.StemBranch
import destiny.core.chinese.StemBranchUtils
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import destiny.tools.Feature
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.chrono.ChronoLocalDateTime
import java.time.temporal.ChronoUnit


@Serializable
data class DayConfig(val changeDayAfterZi: Boolean = true ,
                     val midnight : MidnightImpl = MidnightImpl.NADIR) {
  enum class MidnightImpl {
    CLOCK0, // 當地時間手錶零時
    NADIR   // 太陽劃過天底(子午線)
  }
}

@DestinyMarker
class DayConfigBuilder : Builder<DayConfig> {

  var changeDayAfterZi: Boolean = true

  var midnight : DayConfig.MidnightImpl = DayConfig.MidnightImpl.NADIR

  override fun build() : DayConfig {
    return DayConfig(changeDayAfterZi, midnight)
  }

  companion object {
    fun dayConfig(block : DayConfigBuilder.() -> Unit = {}): DayConfig {
      return DayConfigBuilder().apply(block).build()
    }
  }
}



@Serializable
data class DayHourConfig(
  val dayConfig: DayConfig = DayConfig(),
  val hourBranchConfig: HourBranchConfig = HourBranchConfig()
)


@DestinyMarker
class DayHourConfigBuilder : Builder<DayHourConfig> {

  var dayConfig = DayConfig()

  fun day(block: DayConfigBuilder.() -> Unit) {
    this.dayConfig = DayConfigBuilder.dayConfig(block)
  }

  var hourBranchConfig: HourBranchConfig = HourBranchConfig()
  fun hourBranch(block : HourBranchConfigBuilder.() -> Unit = {}) {
    hourBranchConfig = HourBranchConfigBuilder.hourBranch(block)
  }

  override fun build(): DayHourConfig {
    return DayHourConfig(dayConfig, hourBranchConfig)
  }

  companion object {
    fun dayHour(block: DayHourConfigBuilder.() -> Unit = {}): DayHourConfig {
      return DayHourConfigBuilder().apply(block).build()
    }
  }
}


class DayHourFeature(val midnightFeature: MidnightFeature,
                     private val hourBranchFeature: HourBranchFeature,
                     private val riseTransImpl: IRiseTrans,
                     private val julDayResolver: JulDayResolver) : Feature<DayHourConfig, Pair<StemBranch, StemBranch>> {

  override val key: String = "dayHour"

  override val defaultConfig: DayHourConfig = DayHourConfig()

  override fun getModel(gmtJulDay: GmtJulDay, loc: ILocation, config: DayHourConfig): Pair<StemBranch, StemBranch> {

    val lmt = TimeTools.getLmtFromGmt(gmtJulDay, loc, julDayResolver)
    return getModel(lmt, loc, config)
  }

  override fun getModel(lmt: ChronoLocalDateTime<*>, loc: ILocation, config: DayHourConfig): Pair<StemBranch, StemBranch> {

    val hourImpl = getHourImpl(config.hourBranchConfig.hourImpl , riseTransImpl, julDayResolver)
    logger.trace { "[LMT] hourImpl = $hourImpl" }

    // 下個子初時刻
    val nextZiStart = hourImpl.getLmtNextStartOf(lmt, loc, Branch.子, julDayResolver)

    // 下個子正時刻
    val nextMidnightLmt = TimeTools.getLmtFromGmt(midnightFeature.getModel(lmt, loc, config.dayConfig) , loc, julDayResolver)
      .let { dstSwitchCheck.invoke(it, nextZiStart) }

    val day: StemBranch = getDay(lmt, loc, hourImpl, nextZiStart, nextMidnightLmt, config.dayConfig.changeDayAfterZi, julDayResolver)

    val hourBranch = hourBranchFeature.getModel(lmt, loc, config.hourBranchConfig)

    val hourStem = getHourStem(hourImpl, lmt, loc, day, hourBranch, config.dayConfig.changeDayAfterZi, nextZiStart, nextMidnightLmt, julDayResolver)

    val hour = StemBranch[hourStem, hourBranch]
    return day to hour
  }

  private val dstSwitchCheck = { nextMn : ChronoLocalDateTime<*> , nextZiStart : ChronoLocalDateTime<*> ->
    val dur = Duration.between(nextZiStart, nextMn).abs()
    if (dur.toMinutes() <= 1) {
      logger.warn("子初子正 幾乎重疊！ 可能是 DST 切換. 下個子初 = {} , 下個子正 = {} . 相隔秒 = {}", nextZiStart, nextMn, dur.seconds) // DST 結束前一天，可能會出錯
      nextMn.plus(1, ChronoUnit.HOURS)
    } else {
      nextMn
    }
  }

  private fun getHourStem(
    hourImpl: IHour,
    lmt: ChronoLocalDateTime<*>,
    loc: ILocation,
    day: StemBranch,
    hourBranch: Branch,
    cdaz: Boolean,
    nextZiStart: ChronoLocalDateTime<*>,
    nextMidnightLmt : ChronoLocalDateTime<*>,
    julDayResolver: JulDayResolver
  ): Stem {
    val nextZi: ChronoLocalDateTime<*> = hourImpl.getLmtNextStartOf(lmt, loc, Branch.子, julDayResolver)

    val tempDayStem = day.stem.let {
      // 如果「子正」才換日
      if (!cdaz) {
        /**
         * <pre>
         * 而且 LMT 的八字日柱 不同於 下一個子初的八字日柱 發生情況有兩種：
         * 第一： LMT 零時 > 子正 > LMT > 子初 ,（即下圖之 LMT1)
         * 第二： 子正 > LMT > LMT 零時 (> 子初) , （即下圖之 LMT3)
         *
         * 子末(通常1)  LMT4    子正      LMT3       0|24     LMT2        子正    LMT1    子初（通常23)
         * |------------------|--------------------|--------------------|------------------|
        </pre> *
         */

        if (day !== getDay(nextZi, loc, hourImpl, nextZiStart, nextMidnightLmt, cdaz, julDayResolver))
          it.next
        else
          it
      } else
        it
    }
    // 時干
    return StemBranchUtils.getHourStem(tempDayStem, hourBranch)
  }
}