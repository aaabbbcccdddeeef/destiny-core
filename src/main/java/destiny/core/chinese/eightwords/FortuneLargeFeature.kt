/**
 * Created by smallufo on 2021-08-22.
 */
package destiny.core.chinese.eightwords

import destiny.core.Gender
import destiny.core.IntAgeNote
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver
import destiny.core.calendar.TimeTools
import destiny.core.calendar.eightwords.EightWordsConfig
import destiny.core.calendar.eightwords.EightWordsConfigBuilder
import destiny.core.chinese.IStemBranch
import destiny.tools.AbstractCachedPersonFeature
import destiny.tools.Builder
import destiny.tools.DestinyMarker
import destiny.tools.PersonFeature
import jakarta.inject.Named
import kotlinx.serialization.Serializable
import java.time.chrono.ChronoLocalDateTime

enum class FortuneLargeImpl {
  DefaultSpan,    // 傳統、標準大運 (每柱十年)
  SolarTermsSpan  // 節氣星座過運法 (每柱五年)
}

@Serializable
data class FortuneLargeConfig(val impl: FortuneLargeImpl = FortuneLargeImpl.DefaultSpan,
                              val span : Double = 120.0,
                              val intAgeNotes: List<IntAgeNote> = listOf(IntAgeNote.WestYear, IntAgeNote.Minguo),
                              val eightWordsConfig: EightWordsConfig = EightWordsConfig()): java.io.Serializable

@DestinyMarker
class FortuneLargeConfigBuilder : Builder<FortuneLargeConfig> {

  var impl: FortuneLargeImpl = FortuneLargeImpl.DefaultSpan

  var span : Double = 120.0

  var intAgeNotes: List<IntAgeNote> = listOf(IntAgeNote.WestYear, IntAgeNote.Minguo)
  fun intAgeNotes(impls: List<IntAgeNote>) {
    intAgeNotes = impls
  }

  var eightWordsConfig: EightWordsConfig = EightWordsConfig()
  fun ewConfig(block : EightWordsConfigBuilder.() -> Unit = {}) {
    this.eightWordsConfig = EightWordsConfigBuilder.ewConfig(block)
  }


  override fun build(): FortuneLargeConfig {
    return FortuneLargeConfig(impl, span, intAgeNotes, eightWordsConfig)
  }

  companion object {
    fun fortuneLarge(block: FortuneLargeConfigBuilder.() -> Unit = {}) : FortuneLargeConfig {
      return FortuneLargeConfigBuilder().apply(block).build()
    }
  }
}

interface IFortuneLargeFeature : PersonFeature<FortuneLargeConfig, List<FortuneData>> {
  /**
   * 逆推大運
   * 由 GMT 反推月大運
   * @param fromGmtJulDay 計算此時刻是屬於哪條月大運當中
   * 實際會與 [IPersonContextModel.getStemBranchOfFortuneMonth] 結果相同
   * */
  fun getStemBranch(gmtJulDay: GmtJulDay, loc: ILocation, gender: Gender, fromGmtJulDay: GmtJulDay, config: FortuneLargeConfig): IStemBranch?

  fun getStemBranch(lmt: ChronoLocalDateTime<*>, loc: ILocation, gender: Gender, fromGmtJulDay: GmtJulDay, config: FortuneLargeConfig): IStemBranch? {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, loc)
    return getStemBranch(gmtJulDay, loc, gender, fromGmtJulDay, config)
  }
}

@Named
class FortuneLargeFeature(private val implMap : Map<FortuneLargeImpl, IPersonFortuneLarge>,
                          private val julDayResolver: JulDayResolver) : IFortuneLargeFeature, AbstractCachedPersonFeature<FortuneLargeConfig, List<FortuneData>>() {

  override val defaultConfig: FortuneLargeConfig = FortuneLargeConfig()

  override fun calculate(gmtJulDay: GmtJulDay, loc: ILocation, gender: Gender, name: String?, place: String?, config: FortuneLargeConfig): List<FortuneData> {
    val lmt = TimeTools.getLmtFromGmt(gmtJulDay, loc, julDayResolver)

    val count = when (config.impl) {
      FortuneLargeImpl.DefaultSpan    -> 9
      FortuneLargeImpl.SolarTermsSpan -> 18
    }

    return implMap[config.impl]!!.getFortuneDataList(lmt, loc, gender, count, config)
  }

  override fun getStemBranch(gmtJulDay: GmtJulDay, loc: ILocation, gender: Gender, fromGmtJulDay: GmtJulDay, config: FortuneLargeConfig): IStemBranch? {

    return implMap[config.impl]!!.getStemBranch(gmtJulDay, loc, gender, julDayResolver.getLocalDateTime(maxOf(gmtJulDay , fromGmtJulDay)), config)
  }
}
