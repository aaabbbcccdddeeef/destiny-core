package destiny.core.chinese.lunarStation

import destiny.tools.ILocaleString
import java.util.*

/**
 * [YearEpoch.EPOCH_1864]
 * 年禽還有一種推法，基本定位為
 * 公元964年為六元甲子，
 * 公元1144年為七元甲子，
 * 公元1324年為一元甲子，
 * 公元1504年為二元上元甲子，
 * 1684年為三元上元甲子，
 * 1864年為四元上元甲子，
 * 按此推法，
 * 則1864年為甲子虛，
 * 1924年為中元甲子奎，
 * 1984年為下元甲子畢。
 * 按二十八宿次序順推即可。按此法則
 * 2008年為奎木狼，
 * 2009年為婁金狗，
 * 2010年為胃土雉，
 * 依次類推。
 */
enum class YearEpoch {
  EPOCH_1564, // 1564年 甲子虛
  EPOCH_1864  // 1864年 甲子虛
}

fun YearEpoch.toString(locale: Locale): String {
  return this.asLocaleString().toString(locale)
}

fun YearEpoch.asLocaleString() = object : ILocaleString {
  override fun toString(locale: Locale): String {
    return ResourceBundle.getBundle(YearEpoch::class.java.name, locale).getString(name)
  }
}
