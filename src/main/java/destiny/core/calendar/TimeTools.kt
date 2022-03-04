/**
 * Created by smallufo on 2017-09-26.
 */
package destiny.core.calendar

import destiny.core.News.EastWest.EAST
import destiny.core.calendar.GmtJulDay.Companion.toGmtJulDay
import destiny.core.chinese.StemBranch
import destiny.tools.StringTools
import mu.KotlinLogging
import java.time.*
import java.time.chrono.ChronoLocalDate
import java.time.chrono.ChronoLocalDateTime
import java.time.chrono.ChronoZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField.*
import java.time.temporal.ChronoUnit
import java.time.temporal.JulianFields.JULIAN_DAY
import java.time.zone.ZoneRulesException
import java.util.*
import kotlin.math.abs
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeTools {

  private val GMT = ZoneId.of("GMT")

  private val logger = KotlinLogging.logger { }

  /**
   * ======================================== GMT [Instant] -> julian day [GmtJulDay] ========================================
   */

  /**
   * @param instant 從 「GMT」定義的 [Instant] 轉換成 Julian Day
   */
  fun getJulDay(instant: Instant): GmtJulDay {
    // 先取得「被加上 0.5 的」 julian day
    val zdt = instant.atZone(GMT)
    val halfAddedJulDay = zdt.getLong(JULIAN_DAY)
    val localTime = zdt.toLocalTime()
    return getGmtJulDay(halfAddedJulDay, localTime)
  }


  /**
   * 從「帶有 Zone」的時間，查詢當下的 julDay
   * 必須先轉為 GMT
   */
  fun getJulDay(zdt: ChronoZonedDateTime<*>): GmtJulDay {
    val gmt = zdt.withZoneSameInstant(GMT)
    return getGmtJulDay(gmt.toLocalDateTime())
  }

  /**
   * @param instant 將 (GMT) instant 轉換為（GMT）的日期
   */
  fun getLocalDateTime(instant: Instant, resolver: JulDayResolver): ChronoLocalDateTime<*> {
    return resolver.getLocalDateTimeFromInstant(instant)
  }

  /**
   * ======================================== GMT [ChronoLocalDateTime] -> julian day [GmtJulDay] ========================================
   */

  /**
   * astro julian day number 開始於
   * November 24, 4713 BC 當天中午  : proleptic Gregorian calendar
   * (proleptic year = -4712)
   * @param gmt 時刻 (包含 0 year)
   * getLong(JULIAN_DAY)   真正需要的值（左邊減 0.5）
   * | ISO date          |  Julian Day Number | Astronomical Julian Day |
   * | 1970-01-01T00:00  |         2,440,588  |         2,440,587.5     |
   */
  fun getGmtJulDay(gmt: ChronoLocalDateTime<*>): GmtJulDay {
    val gmtInstant = gmt.toInstant(ZoneOffset.UTC)
    return getJulDay(gmtInstant)
  }


  /**
   * 承上， date + time 拆開來的版本
   */
  fun getGmtJulDay(date: ChronoLocalDate, localTime: LocalTime): GmtJulDay {
    return getGmtJulDay(date.atTime(localTime))
  }


  /**
   * ======================================== LMT [ChronoLocalDateTime] -> julian day [GmtJulDay] ========================================
   */


  /**
   * 直接從 LMT 傳回 gmt 的 jul day
   */
  fun getGmtJulDay(lmt: ChronoLocalDateTime<*>, loc: ILocation): GmtJulDay {
    val gmt = getGmtFromLmt(lmt, loc)
    return getGmtJulDay(gmt)
  }



  // ======================================== LMT -> GMT ========================================
  fun getGmtFromZonedDateTime(zonedLmt: ChronoZonedDateTime<*>): ChronoZonedDateTime<*> {
    val zoneOffset = zonedLmt.offset
    val totalSeconds = zoneOffset.totalSeconds
    return zonedLmt.toLocalDateTime().minus(totalSeconds.toLong(), ChronoUnit.SECONDS).atZone(GMT)
  }

  fun getGmtFromLmt(zonedLmt: ChronoZonedDateTime<*>): ChronoLocalDateTime<*> {
    val zoneOffset = zonedLmt.offset
    val totalSeconds = zoneOffset.totalSeconds
    return zonedLmt.toLocalDateTime().minus(totalSeconds.toLong(), ChronoUnit.SECONDS)
  }

  fun getGmtFromLmt(lmt: ChronoLocalDateTime<*>, zoneId: ZoneId): ChronoLocalDateTime<*> {
    return getGmtFromLmt(lmt.atZone(zoneId))
  }

  fun getGmtFromLmt(lmt: ChronoLocalDateTime<*>, loc: ILocation): ChronoLocalDateTime<*> {
    return if (loc.hasMinuteOffset) {
      val secOffset = loc.finalMinuteOffset * 60
      lmt.plus((0 - secOffset).toLong(), ChronoUnit.SECONDS)
    } else {
      getGmtFromLmt(lmt, loc.timeZone.toZoneId())
    }
  }

  /**
   * 直接用秒差來反推 GMT 時刻 , 忽略（不知）tzid 等訊息
   */
  fun getGmtFromLmt(lmt: ChronoLocalDateTime<*>, secondOffset: Int): ChronoLocalDateTime<*> {
    val zfs = ZoneOffset.ofTotalSeconds(secondOffset)
    return getGmtFromLmt(lmt, zfs)
  }

  /**
   * LMT (with TimeZone) to GMT
   *
   * ZoneId.of(string) 可能會出現 ZoneRulesException
   * 例如 : ZoneRulesException: Unknown time-zone ID: CTT
   * 因為某些 三字元的 zoneId 被 deprecated
   * 參照
   * http://stackoverflow.com/a/41683097/298430
   */
  fun getGmtFromLmt(lmt: ChronoLocalDateTime<*>, timeZone: TimeZone): ChronoLocalDateTime<*> {
    val zoneId = try {
      ZoneId.of(timeZone.id)
    } catch (ignored: ZoneRulesException) {
      ZoneId.of("Asia/Taipei") // 若無法 parse , 則採用 Asia/Taipei
    }
    return getGmtFromLmt(lmt, zoneId)
  }


  // ======================================== GMT -> LMT ========================================

  fun getLmtFromGmt(gmt: ChronoLocalDateTime<*>, zoneId: ZoneId): ChronoLocalDateTime<*> {
    val gmtZoned = gmt.atZone(GMT)
    logger.debug("gmtZoned = {}", gmtZoned)
    val newZoned = gmtZoned.withZoneSameInstant(zoneId)
    logger.debug("gmtZoned with {} = {}", zoneId, newZoned)
    return newZoned.toLocalDateTime()
  }

  fun getLmtFromGmt(gmt: ChronoLocalDateTime<*>, loc: ILocation): ChronoLocalDateTime<*> {
    return (if (loc.hasMinuteOffset) {
      val secOffset = loc.finalMinuteOffset * 60
      gmt.plus(secOffset.toLong(), ChronoUnit.SECONDS).atZone(loc.timeZone.toZoneId()).toLocalDateTime()
    } else {
      getLmtFromGmt(gmt, loc.timeZone.toZoneId())
    }).fixError()
  }


  fun getLmtFromGmt(gmtJulDay: GmtJulDay,
                    location: ILocation,
                    julDayResolver: JulDayResolver): ChronoLocalDateTime<*> {
    val gmt = julDayResolver.getLocalDateTime(gmtJulDay)
    return getLmtFromGmt(gmt, location)
  }

  // ======================================== DST 查詢 ========================================


  /**
   * @return 此時刻，此 TimeZone ，是否有日光節約時間
   */
  fun isDst(lmt: ChronoLocalDateTime<*>, tz: TimeZone): Boolean {
    val zdt = lmt.atZone(tz.toZoneId())
    return zdt.zone.rules.isDaylightSavings(zdt.toInstant())
  }

  fun isDst(lmt: ChronoLocalDateTime<*>, loc: ILocation): Boolean {
    return isDst(lmt, loc.timeZone)
  }

  fun getSecondsOffset(lmt: ChronoLocalDateTime<*>, zoneId: ZoneId): Int {
    return lmt.atZone(zoneId).offset.totalSeconds
  }

  fun getOffset(lmt: ChronoLocalDateTime<*>, zoneId: ZoneId): kotlin.time.Duration {
    return lmt.atZone(zoneId).offset.totalSeconds.toDuration(DurationUnit.SECONDS)
  }

  /**
   * @return 取得此地點、此時刻，與 GMT 的「秒差」 (不論是否有日光節約時間）
   *
   * 此演算法得到的結果，與下方相同
   * zoneId.getRules().getOffset(lmt.atZone(zoneId).toInstant()).getTotalSeconds();
   */
  private fun getSecondsOffset(lmt: ChronoLocalDateTime<*>, tz: TimeZone): Int {
    return getSecondsOffset(lmt, tz.toZoneId())
  }

  private fun getOffset(lmt: ChronoLocalDateTime<*>, tz: TimeZone): kotlin.time.Duration {
    return getOffset(lmt, tz.toZoneId())
  }

  /**
   * @return 取得此地點、此時刻，與 GMT 的「秒差」 (不論是否有日光節約時間）
   */
  private fun getSecondsOffset(lmt: ChronoLocalDateTime<*>, loc: ILocation): Int {
    return getSecondsOffset(lmt, loc.timeZone)
  }

  fun getOffset(lmt: ChronoLocalDateTime<*>, loc: ILocation): kotlin.time.Duration {
    return getOffset(lmt, loc.timeZone)
  }

  /** @return 確認此時刻，是否有DST。不論是否有沒有DST，都傳回與GMT誤差幾秒 */
  fun getDstAndOffset(lmt: ChronoLocalDateTime<*>, loc: ILocation): Pair<Boolean, kotlin.time.Duration> {
    return Pair(isDst(lmt, loc), getOffset(lmt, loc))
  }



  // ======================================== misc methods ========================================

  /** 西元年份 to 甲子 (不考慮節氣 / 初一) */
  fun Int.yearToStemBranch() : StemBranch {
    val index = if (this > 0)
      (this - 1984) % 60
    else
      (1 - this - 1984) % 60

    return StemBranch[index]
  }

  /**
   * @return 確認 later 是否真的 after prior 的時刻
   * 相當於 [ChronoLocalDateTime.isAfter]
   */
  fun isAfter(later: ChronoLocalDateTime<*>, prior: ChronoLocalDateTime<*>): Boolean {
    val smaller = getGmtJulDay(prior)
    val bigger = getGmtJulDay(later)
    return bigger > smaller
  }

  fun isBefore(prior: ChronoLocalDateTime<*>, later: ChronoLocalDateTime<*>): Boolean {
    val bigger = getGmtJulDay(later)
    val smaller = getGmtJulDay(prior)
    return smaller < bigger
  }


  /**
   * @return t 是否 處於 t1 與 t2 之間
   *
   * 將這些 t , t1 , t2 視為 GMT , 轉成 jul day 來比較大小
   */
  fun isBetween(t: ChronoLocalDateTime<*>, t1: ChronoLocalDateTime<*>, t2: ChronoLocalDateTime<*>): Boolean {
    val julDay = getGmtJulDay(t)
    val julDay1 = getGmtJulDay(t1)
    val julDay2 = getGmtJulDay(t2)
    return julDay2 > julDay && julDay > julDay1 || julDay1 > julDay && julDay > julDay2
  }


  /**
   * 解碼
   */
  fun decode(s: String): ChronoLocalDateTime<*>? {
    val trimmed = StringTools.clean(s)
    logger.trace("clean '{}' => '{}' ", s, trimmed)
    return when {
      trimmed.startsWith('G') -> LocalDateTime.parse(trimmed.substring(1), DateTimeFormatter.ISO_DATE_TIME)
      trimmed.startsWith('J') -> {
        val date = s.substring(1, s.indexOf('T'))
        val (year, month, day) = date.split("-").let {
          Triple(it[0].toInt(), it[1].toInt(), it[2].toInt())
        }
        val time = LocalTime.parse(s.substring(s.indexOf('T') + 1))
        JulianDateTime.of(year, month, day, time.hour, time.minute, time.second, time.nano)
      }
      else -> JulDayResolver1582CutoverImpl.fromDebugString(trimmed)
    }
  }


  /**
   * 新版 (since 2018-04) 直接以 ISO-8601 編碼 , 但前面加上 G/J 以區別 Gregorian or Julian 曆別
   */
  fun encode(time: ChronoLocalDateTime<*>): String {
    return encodeIso8601(time)
  }

  /**
   * ISO 8601 編碼
   * ex : 2018-04-18T02:43:10
   * 年份為 proleptic year
   * 以公元前1年為0000年，公元前2年為-0001年
   */
  fun encodeIso8601(time: ChronoLocalDateTime<*>): String {
    return StringBuilder().apply {
      if (time is JulianDateTime) {
        append("J")
        // JulianDateTime 不能直接用 DateTimeFormatter , 會被轉成 Gregorian 日期輸出
        if (time.year < 0)
          append('-')
        append(abs(time.year).toString().padStart(4, '0'))
        append('-')
        append(time.month.toString().padStart(2, '0'))
        append('-')
        append(time.dayOfMonth.toString().padStart(2, '0'))
        append('T')
        append(time.hour.toString().padStart(2, '0'))
        append(':')
        append(time.minute.toString().padStart(2, '0'))
        append(':')
        append(time.second.toString().padStart(2, '0'))
        if (time.nano > 0) {
          append('.')
          append(time.nano.toString().dropLastWhile { it == '0' }) // 把尾端的 '0' 都移除
        }
      } else {
        append("G")
        val isoDateTime = time.format(DateTimeFormatter.ISO_DATE_TIME)
        append(isoDateTime)
      }
    }.toString()
  }

  /**
   * 舊版 編碼
   * decode 於 [JulDayResolver1582CutoverImpl.fromDebugString]
   * */
  fun encodeOld(time: ChronoLocalDateTime<*>): String {
    return with(StringBuilder()) {
      append(if (time.get(YEAR_OF_ERA) >= 1) '+' else '-')
      append(time.get(YEAR_OF_ERA).toString().padStart(4, ' '))

      append(time.get(MONTH_OF_YEAR).toString().padStart(2, '0'))
      append(time.get(DAY_OF_MONTH).toString().padStart(2, '0'))
      append(time.get(HOUR_OF_DAY).toString().padStart(2, '0'))
      append(time.get(MINUTE_OF_HOUR).toString().padStart(2, '0'))
      append(time.get(SECOND_OF_MINUTE).toString().padStart(2, '0'))

      append('.')
      if (time.get(NANO_OF_SECOND) == 0) {
        append('0')
      } else {
        // 小數點部分
        val decimal = (time.get(NANO_OF_SECOND) / 1_000_000_000.0).toString()
        append(decimal.substring(2))
      }
    }.toString()
  }

  /** 將 double 的秒數，拆為 long秒數 以及 longNano 兩個值  */
  fun splitSecond(seconds: Double): Pair<Int, Int> {
    val secs = seconds.toInt()
    val nano = ((seconds - secs) * 1000000000).toInt()
    return Pair(secs, nano)
  }

  /**
   * 將「分鐘」拆成「小時」與「分」
   */
  fun splitMinutes(minutes: Int): Pair<Int, Int> {
    val hours = minutes / 60
    val mins = minutes % 60
    return Pair(hours, mins)
  }

  /**
   * 取得不中斷的年份 , 亦即 proleptic year
   * @param yearOfEra 傳入的年，一定大於 0
   * @return proleptic year , 線性的 year : 西元前1年:0 , 西元前2年:-1 ...
   */
  fun getNormalizedYear(ad: Boolean, yearOfEra: Int): Int {
    require(yearOfEra > 0) {
      "year $yearOfEra must > 0"
    }
    return if (!ad)
      -(yearOfEra - 1)
    else
      yearOfEra
  }

  private fun getGmtJulDay(halfAddedJulDay: Long, localTime: LocalTime): GmtJulDay {
    val hour = localTime.hour
    val min = localTime.minute
    val sec = localTime.second
    val nano = localTime.nano
    val dayValue = hour / 24.0 + min / 1440.0 + sec / 86400.0 + nano / (1_000_000_000.0 * 86400)

    return (halfAddedJulDay - 0.5 + dayValue).toGmtJulDay()
  }

  /**
   * 將 LMT 以及經度 轉換為當地真正的時間 , 不包含真太陽時(均時差) 的校正
   * @return 經度時間
   */
  fun getLongitudeTime(lmt: ChronoLocalDateTime<*>, location: ILocation): ChronoLocalDateTime<*> {
    val absLng = abs(location.lng)
    val secondsOffset = getOffset(lmt, location).inWholeSeconds
    val zoneSecondOffset = abs(secondsOffset)
    val longitudeSecondOffset = absLng * 4.0 * 60.0 // 經度與GMT的時差 (秒) , 一分鐘四度

    return if (location.eastWest === EAST) {
      val seconds = longitudeSecondOffset - zoneSecondOffset
      val (first, second) = splitSecond(seconds)
      lmt.plus(first.toLong(), ChronoUnit.SECONDS).plus(second.toLong(), ChronoUnit.NANOS)
    } else {
      val seconds = zoneSecondOffset - longitudeSecondOffset
      val (first, second) = splitSecond(seconds)
      lmt.plus(first.toLong(), ChronoUnit.SECONDS).plus(second.toLong(), ChronoUnit.NANOS)
    }
  }

}

fun ChronoLocalDateTime<*>.fixError(): ChronoLocalDateTime<out ChronoLocalDate> {
  return if (this.get(SECOND_OF_MINUTE) == 59 && this.get(NANO_OF_SECOND) > 999_900_000) {
    this.with(NANO_OF_SECOND, 0).plus(1, ChronoUnit.SECONDS)
  } else if (this.get(SECOND_OF_MINUTE) == 0 && this.get(NANO_OF_SECOND) < 100000) {
    this.with(NANO_OF_SECOND, 0)
  } else {
    this
  }
}
