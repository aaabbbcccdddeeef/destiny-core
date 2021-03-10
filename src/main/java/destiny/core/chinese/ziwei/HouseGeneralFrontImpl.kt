/** Created by smallufo on 2017-12-11. */
package destiny.core.chinese.ziwei

import destiny.core.Gender
import destiny.core.calendar.SolarTerms
import destiny.core.chinese.Branch
import destiny.core.chinese.StemBranch
import destiny.core.chinese.YearType

/**
 * 用以計算 年支系星 : [StarGeneralFront] 將前12星
 *
 * 其中的「年支」，可能是陰曆、也可能是節氣
 * 鍾義明 的書籍特別提出，年系星，應該用立春分界 , 參考截圖 http://imgur.com/WVUxCc8
 */
class HouseGeneralFrontImpl(star: StarGeneralFront) : HouseAbstractImpl<Branch>(star) {
  override fun getBranch(t: Branch): Branch {
    return StarGeneralFront.starFuncMap[star]!!.invoke(t)
  }

  override fun getBranch(lunarYear: StemBranch,
                         solarYear: StemBranch,
                         monthBranch: Branch,
                         finalMonthNumForMonthStars: Int,
                         solarTerms: SolarTerms,
                         days: Int,
                         hour: Branch,
                         state: Int,
                         gender: Gender,
                         leap: Boolean,
                         prevMonthDays: Int,
                         predefinedMainHouse: Branch?,
                         context: IZiweiContext): Branch {
    val yearBranch = if (context.yearType == YearType.YEAR_LUNAR) lunarYear.branch else solarYear.branch
    return getBranch(yearBranch)
  }

}
