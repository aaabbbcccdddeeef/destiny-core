/**
 * Created by smallufo on 2022-07-26.
 */
package destiny.core.astrology.prediction

import destiny.core.calendar.GmtJulDay
import destiny.core.calendar.JulDayResolver1582CutoverImpl
import destiny.core.calendar.TimeTools
import destiny.core.calendar.locationOf
import mu.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ProgressionMinorTest {
  val loc = locationOf(Locale.TAIWAN)

  val julDayResolver = JulDayResolver1582CutoverImpl()

  private val progression = ProgressionMinor()

  val logger = KotlinLogging.logger { }

  @Test
  fun test2022() {
    val natalLmt = LocalDateTime.of(2000, 1, 1, 0, 0)
    val natalGmtJulDay = TimeTools.getGmtJulDay(natalLmt, loc)

    val now = LocalDateTime.of(2022, 7, 26, 0, 30)
    val nowGmtJulDay = TimeTools.getGmtJulDay(now, loc)

    progression.getConvergentTime(natalGmtJulDay, nowGmtJulDay).also { convergentJulDay: GmtJulDay ->
      logger.info { "convergentJulDay = $convergentJulDay" }
      val convergentLmt = TimeTools.getLmtFromGmt(convergentJulDay, loc, julDayResolver)
      logger.info { "convergentLmt = $convergentLmt" }
      // 大約 300 個月
      //assertEquals(natalLmt.toLocalDate().plusDays(300), convergentLmt.toLocalDate())

      progression.getDivergentTime(natalGmtJulDay, convergentJulDay).also { divergentJulDay: GmtJulDay ->
        logger.info { "divergentJulDay = $divergentJulDay" }
        val divergentLmt = TimeTools.getLmtFromGmt(divergentJulDay, loc, julDayResolver)
        logger.info { "divergentLmt = $divergentLmt" }

        assertEquals(0, Duration.between(now, divergentLmt).abs().seconds)
      }

      progression.getDivergentTime(natalLmt, convergentLmt, julDayResolver).also { divergentLmt: ChronoLocalDateTime<*> ->
        logger.info { "divergentLmt = $divergentLmt" }
        assertEquals(0, Duration.between(now, divergentLmt).abs().seconds)
      }
    }
  }
}
