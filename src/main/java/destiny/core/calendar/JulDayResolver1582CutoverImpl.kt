/**
 * Created by smallufo on 2017-09-25.
 */
package destiny.core.calendar

import mu.KotlinLogging
import org.threeten.extra.chrono.JulianChronology
import java.io.Serializable
import java.time.*
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.IsoChronology
import kotlin.math.floor

/**
 * 1582-10-04 (含) 之前 , 傳回 [JulianChronology]
 * 1582-10-15 (含) 之後 , 傳回 [IsoChronology]
 * 其切分時間點，與 Java 的 [java.util.GregorianCalendar] 相同
 */
class JulDayResolver1582CutoverImpl : JulDayResolver, Serializable {


  override fun getLocalDateTime(gmtJulDay: Double): ChronoLocalDateTime<*> {
    return getLocalDateTimeStatic(gmtJulDay)
  }

  override fun getLocalDateTime(gmtInstant: Instant): ChronoLocalDateTime<*> {

    return getLocalDateTimeStatic(gmtInstant)
  }

  companion object {

    private val GMT = ZoneId.of("GMT")

    /**
     * Julian Calendar    終止於西元 1582-10-04 , 該日的 Julian Day 是 2299159.5
     * Gregorian Calendar 開始於西元 1582-10-15 , 該日的 Julian Day 是 2299160.5
     */
    private const val GREGORIAN_START_JULIAN_DAY = 2299160.5

    /**
     * 承上， 西元 1582-10-15 0:0 的 instant 「秒數」為 -12219292800L  (from 1970-01-01 逆推)
     */
    private const val GREGORIAN_START_INSTANT = -12219292800000L

    private val logger = KotlinLogging.logger {  }

    /**
     * @param gmtInstant 從 Instant 轉為 日期、時間
     */
    fun getLocalDateTimeStatic(gmtInstant: Instant): ChronoLocalDateTime<*> {
      val epochMilli = gmtInstant.toEpochMilli()
      logger.trace("epochMilli = {}", epochMilli)
      val isGregorian = epochMilli >= GREGORIAN_START_INSTANT
      return if (isGregorian)
        gmtInstant.atZone(GMT).toLocalDateTime()
      else {
        val epochSec = gmtInstant.epochSecond
        val nanoOfSec = gmtInstant.nano
        logger.trace("epoch sec = {} , nanoOfSec = {} ", epochSec, nanoOfSec)
        JulianDateTime.ofEpochSecond(epochSec, nanoOfSec, ZoneOffset.UTC)
      }
    }

    /**
     * 從 Julian Day 建立 [ChronoLocalDateTime] (GMT)
     * http://www.astro.com/ftp/placalc/src/revjul.c
     *
     * reverse function to julday()
     * 1582-10-15 0:00 為界
     * 之前，傳回 [JulianDateTime]
     * 之後，傳回 [LocalDateTime]
     */
    fun getLocalDateTimeStatic(gmtJulDay: Double): ChronoLocalDateTime<*> {

      val isGregorian = gmtJulDay >= GREGORIAN_START_JULIAN_DAY

      var u0: Double
      var u1: Double
      val u2: Double
      val u3: Double
      val u4: Double

      u0 = gmtJulDay + 32082.5

      if (isGregorian) {
        u1 = u0 + floor(u0 / 36525.0) - floor(u0 / 146100.0) - 38.0
        if (gmtJulDay >= 1830691.5) {
          u1 += 1.0
        }
        u0 = u0 + floor(u1 / 36525.0) - floor(u1 / 146100.0) - 38.0
      }
      u2 = floor(u0 + 123.0)
      u3 = floor((u2 - 122.2) / 365.25)
      u4 = floor((u2 - floor(365.25 * u3)) / 30.6001)
      var month = (u4 - 1.0).toInt()
      if (month > 12) {
        month -= 12
      }
      val day = (u2 - floor(365.25 * u3) - floor(30.6001 * u4)).toInt()
      val y = (u3 + floor((u4 - 2.0) / 12.0) - 4800).toInt()

      var ad = true
      val year: Int

      if (y <= 0) {
        ad = false
        year = -(y - 1) // 取正值
      } else {
        year = y
      }

      val h = (gmtJulDay - floor(gmtJulDay + 0.5) + 0.5) * 24.0
      val hour = h.toInt()
      val minute = (h * 60 - hour * 60).toInt()
      val second = h * 3600 - (hour * 3600).toDouble() - (minute * 60).toDouble()

      val pair = TimeTools.splitSecond(second)
      val secsInt = pair.first
      val nanoInt = pair.second

      val localTime = LocalTime.of(hour, minute, secsInt, nanoInt)

      return if (isGregorian) {
        // ad 一定為 true , 不用考慮負數年數
        LocalDate.of(year, month, day).atTime(localTime)
      } else {
        val prolepticYear = TimeTools.getNormalizedYear(ad, year)
        JulianDateTime.of(prolepticYear, month, day, localTime.hour, localTime.minute, localTime.second, localTime.nano)
      }
    }

    fun getDateTime(gmtJulDay: Double): Pair<ChronoLocalDate, LocalTime> {
      val dateTime = getLocalDateTimeStatic(gmtJulDay)
      return Pair(dateTime.toLocalDate(), dateTime.toLocalTime())
    }

    /**
     * 利用一個字串 's' 來建立整個時間 , 格式如下：
     * 0123456789A1234567
     * +YYYYMMDDHHMMSS.SS
     */
    fun fromDebugString(s: String): ChronoLocalDateTime<*>? {
      return when (s[0]) {
        '+' -> true
        '-' -> false
        else -> {
          logger.error("Cannot decode debugString : {}" , s)
          null
        }
      }?.let { ad ->
        val yearOfEra = s.substring(1, 5).trim { it <= ' ' }.toInt()
        val month = s.substring(5, 7).trim { it <= ' ' }.toInt()
        val day = s.substring(7, 9).trim { it <= ' ' }.toInt()
        val hour = s.substring(9, 11).trim { it <= ' ' }.toInt()
        val minute = s.substring(11, 13).trim { it <= ' ' }.toInt()
        val second = s.substring(13).toDouble()
        of(ad, yearOfEra, month, day, hour, minute, second).first
      }
    }

    fun of(ad: Boolean, yearOfEra: Int, month: Int, day: Int, hour: Int, minute: Int, second: Double): Pair<ChronoLocalDateTime<*>, Boolean> {
      val prolepticYear = TimeTools.getNormalizedYear(ad, yearOfEra)

      val gregorian: Boolean
      val pair = TimeTools.splitSecond(second)
      gregorian = when {
        prolepticYear < 1582 -> false
        prolepticYear > 1582 -> true
        else -> // prolepticYear == 1582
          when {
            month < 10 -> false
            month > 10 -> true
            else -> // month == 10
              when {
                day <= 4 -> false
                day >= 15 -> true
                else -> throw RuntimeException("Error Date : $prolepticYear/$month/$day")
              }
          }
      }
      val ldt: ChronoLocalDateTime<*> = if (gregorian)
        LocalDateTime.of(prolepticYear, month, day, hour, minute, pair.first, pair.second)
      else
        JulianDateTime.of(prolepticYear, month, day, hour, minute, pair.first, pair.second)

      return Pair(ldt, gregorian)
    }
  }

}
