/**
 * Created by smallufo on 2017-10-31.
 */
package destiny.core.astrology

import destiny.core.DayNight
import destiny.core.astrology.Planet.*
import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.ILocation
import destiny.core.calendar.JulDayResolver
import destiny.core.calendar.TimeTools
import mu.KotlinLogging
import org.apache.commons.lang3.ArrayUtils
import java.time.chrono.ChronoLocalDateTime
import java.time.temporal.ChronoField

/**
 * 取得當下、當地的「行星時」 Planetary Hour
 * 參考資料
 *
 * http://pansci.asia/archives/126644
 *
 */
interface IPlanetaryHour {

  fun getPlanet(hourIndexOfDay: Int, gmtJulDay: GmtJulDay, loc: ILocation, julDayResolver: JulDayResolver): Planet {

    /** 星期六白天起，七顆行星順序： 土、木、火、日、金、水、月 */
    val seqPlanet = arrayOf(SATURN, JUPITER, MARS, SUN, VENUS, MERCURY, MOON)

    /** 日期順序 */
    val seqDay = intArrayOf(6, 7, 1, 2, 3, 4, 5)

    val lmt = TimeTools.getLmtFromGmt(gmtJulDay, loc, julDayResolver)

    // 1:星期一 , 2:星期二 ... , 6:星期六 , 7:星期日
    val dayOfWeek = lmt.get(ChronoField.DAY_OF_WEEK)

    logger.trace { "dayOfWeek = $dayOfWeek" }

    // from 0 to 6
    val indexOfDayTable = ArrayUtils.indexOf(seqDay, dayOfWeek)
    logger.trace { "indexOfDayTable = $indexOfDayTable" }

    // 0 to (24x7-1)
    val hourIndexFromSaturday = indexOfDayTable * 24 + hourIndexOfDay - 1
    logger.info { "hourIndexFromSaturday = $hourIndexFromSaturday" }

    return seqPlanet[hourIndexFromSaturday % 7]
  }

  data class HourIndexOfDay(
    val hourStart: GmtJulDay,
    val hourEnd: GmtJulDay,
    /** 整天 的 hour index , from 1 to 24 */
    val hourIndex: Int,
    val dayNight: DayNight
  )

  fun getHourIndexOfDay(gmtJulDay: GmtJulDay, loc: ILocation, transConfig: TransConfig = TransConfig()): HourIndexOfDay?

  fun getPlanetaryHour(gmtJulDay: GmtJulDay, loc: ILocation, transConfig: TransConfig = TransConfig()): PlanetaryHour?

  fun getPlanetaryHour(lmt: ChronoLocalDateTime<*>, loc: ILocation, transConfig: TransConfig = TransConfig()): Planet? {
    val gmtJulDay = TimeTools.getGmtJulDay(lmt, loc)
    return getPlanetaryHour(gmtJulDay, loc, transConfig)?.planet
  }

  fun getPlanetaryHours(fromGmt: GmtJulDay, toGmt: GmtJulDay, loc: ILocation, transConfig: TransConfig = TransConfig()): List<PlanetaryHour>

  fun getPlanetaryHours(fromLmt: ChronoLocalDateTime<*>, toLmt: ChronoLocalDateTime<*>, loc: ILocation, transConfig: TransConfig = TransConfig()): List<PlanetaryHour> {
    val fromGmt = TimeTools.getGmtJulDay(fromLmt, loc)
    val toGmt = TimeTools.getGmtJulDay(toLmt, loc)
    return getPlanetaryHours(fromGmt, toGmt, loc, transConfig)
  }

  companion object {
    private val logger = KotlinLogging.logger {  }
  }
}
