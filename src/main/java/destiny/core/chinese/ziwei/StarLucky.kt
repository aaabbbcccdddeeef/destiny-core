/**
 * Created by smallufo on 2017-04-11.
 */
package destiny.core.chinese.ziwei

import destiny.astrology.DayNight
import destiny.core.chinese.*
import destiny.core.chinese.Branch.*
import destiny.core.chinese.ziwei.ZStar.Type.*

/**
 * 八吉星
 */
sealed class StarLucky(nameKey: String, type: ZStar.Type) : ZStar(nameKey, ZStar::class.java.name, nameKey + "_ABBR", type) {

  object 文昌 : StarLucky("文昌", 月) // 甲
  object 文曲 : StarLucky("文曲", 時) // 甲
  object 左輔 : StarLucky("左輔", 月) // 甲
  object 右弼 : StarLucky("右弼", 月) // 甲
  object 天魁 : StarLucky("天魁", 年干) // 甲 , 丙火 , 天乙貴人 , 陽貴
  object 天鉞 : StarLucky("天鉞", 年干) // 甲 , 丁火 , 玉堂貴人 , 陰貴
  object 祿存 : StarLucky("祿存", 年干) // 甲
  object 年馬 : StarLucky("年馬", 年支) // 乙級星 (其實就是天馬)
  object 月馬 : StarLucky("月馬", 月)   // 乙級星 (其實就是天馬)


  companion object {

    val values = arrayOf(文昌, 文曲, 左輔, 右弼, 天魁, 天鉞, 祿存, 年馬, 月馬)

    /** 文昌 : 時支 -> 地支  */
    val fun文昌 = { hour: Branch -> Branch.get(10 - hour.index) }

    /** 文曲 : 時支 -> 地支  */
    val fun文曲 = { hour: Branch -> Branch.get(hour.index + 4) }

    /** 左輔 : 月數 -> 地支  */
    val fun左輔_月數 = { month: Int -> Branch.get(month + 3) }

    /** 左輔 : 月支 -> 地支  */
    val fun左輔_月支 = { month: Branch -> Branch.get(month.index + 2) }


    /** 右弼 : 月數 -> 地支  */
    val fun右弼_月數 = { month: Int -> Branch.get(11 - month) }

    /** 右弼 : 月支 -> 地支  */
    val fun右弼_月支 = { month: Branch -> Branch.get(12 - month.index) }


    /**
     * 天魁 (陽貴人) : 年干 -> 地支
     */
    val fun天魁 = { year: Stem, tianyiImpl: TianyiIF -> tianyiImpl.getFirstTianyi(year, DayNight.DAY) }

    /**
     * 天鉞 (陰貴人) : 年干 -> 地支
     */
    val fun天鉞 = { year: Stem, tianyiImpl: TianyiIF -> tianyiImpl.getFirstTianyi(year, DayNight.NIGHT) }

    /** 祿存 : 年干 -> 地支  */
    val fun祿存 = { year: Stem ->
      when (year) {
        Stem.甲 -> 寅
        Stem.乙 -> 卯
        Stem.丙, Stem.戊 -> 巳
        Stem.丁, Stem.己 -> 午
        Stem.庚 -> 申
        Stem.辛 -> 酉
        Stem.壬 -> 亥
        Stem.癸 -> 子
      }
    }

    /** 天馬(年的驛馬) : 年支 -> 地支  */
    val fun年馬_年支 = { year: Branch ->
      when (BranchTools.trilogy(year)) {
        FiveElement.火 -> 申
        FiveElement.木 -> 巳
        FiveElement.水 -> 寅
        FiveElement.金 -> 亥
        else -> throw AssertionError(year)
      }
    }

    /** 天馬(月的驛馬) : 月數 -> 地支  */
    val fun月馬_月數 = { month: Int ->
      when (month) {
        1, 5, 9 -> 申
        2, 6, 10 -> 巳
        3, 7, 11 -> 寅
        4, 8, 12 -> 亥
        else -> throw AssertionError(month)
      }
    }

    /** 天馬(月的驛馬) : 月支 -> 地支  */
    val fun月馬_月支 = { month: Branch ->
      when (BranchTools.trilogy(month)) {
        FiveElement.火 -> 申
        FiveElement.木 -> 巳
        FiveElement.水 -> 寅
        FiveElement.金 -> 亥
        else -> throw AssertionError(month)
      }
    }
  }


}
