package destiny.core.calendar

import destiny.core.calendar.Constants.SECONDS_OF_DAY
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.absoluteValue

data class SolarTermsTimePos(
  /** 目前時刻 */
  val gmtJulDay : Double,

  /** 前一個（也是目前）的「節」, 及其 GMT JulDay */
  val prevMajor: Pair<SolarTerms, Double>,

  /** 下一個「中氣」，及其 GMT JulDay */
  val middle: Pair<SolarTerms, Double>,

  /** 下一個「節」， 及其 GMT JulDay */
  val nextMajor: Pair<SolarTerms, Double>) : Serializable {

  /** 前半部 (節 to 中氣) */
  val firstHalf : Boolean by lazy {
    gmtJulDay < middle.second
  }

  /** 後半部 (中氣 to 節) */
  val secondHalf : Boolean by lazy {
    !firstHalf
  }

  /** 取得「節」或「氣」, 若在前半部，則取 [prevMajor] , 若在後半部，則取 [middle] */
  val solarTerms : SolarTerms by lazy {
    if (firstHalf)
      prevMajor.first
    else
      middle.first
  }

  /** 距離「節」的開始有幾秒 */
  val toPrevMajorSeconds : Double by lazy {
    (gmtJulDay - prevMajor.second) * SECONDS_OF_DAY
  }

  /** 距離「節」的節數有幾秒 */
  val toNextMajorSeconds : Double by lazy {
    (nextMajor.second - gmtJulDay) * SECONDS_OF_DAY
  }

  /** 距離「中氣」有幾秒 , 這裡採用絕對值，必須配合 [firstHalf] 或是 [secondHalf] 才能知道是在「中氣」之前還是之後 */
  val toMiddleSeconds : Double by lazy {
    (middle.second - gmtJulDay).absoluteValue
  }

  override fun toString(): String {
    return StringBuilder().apply {
      append("距離 ").append(prevMajor.first).append("節 (${prevMajor.first.branch})有")
      append((gmtJulDay - prevMajor.second).toString().take(5))
      append("日")

      append("，")

      append("距離 ").append(nextMajor.first).append("節 (${nextMajor.first.branch})有")
      append((nextMajor.second-gmtJulDay).toString().take(5))
      append("日；")

      append("在中氣").append(middle.first)
      if (firstHalf) {
        append("之前")
      } else {
        append("之後")
      }
      append(abs(middle.second - gmtJulDay).toString().take(5)).append("日。")
    }.toString()

  }
}
