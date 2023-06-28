/**
 * Created by smallufo on 2015-05-31.
 */
package destiny.core.chinese.liuren.golden

import destiny.core.Descriptive
import destiny.core.chinese.Branch
import destiny.core.chinese.StemBranch
import destiny.core.chinese.StemBranch.*
import destiny.core.chinese.liuren.General
import destiny.core.chinese.liuren.General.*
import destiny.core.chinese.liuren.GeneralStemBranch
import destiny.core.chinese.liuren.IGeneralStemBranch
import destiny.tools.asDescriptive
import java.io.Serializable

/**
 * 金口訣：壬子玄武 癸亥天后
 */
class GeneralStemBranchPithy : IGeneralStemBranch,
                               Descriptive by GeneralStemBranch.Pithy.asDescriptive(),
                               Serializable {

  private val map = mapOf(
    貴人 to 己丑,
    螣蛇 to 丁巳,
    朱雀 to 丙午,
    六合 to 乙卯,
    勾陳 to 戊辰,
    青龍 to 甲寅,
    天空 to 戊戌,
    白虎 to 庚申,
    太常 to 己未,
    玄武 to 壬子,
    太陰 to 辛酉,
    天后 to 癸亥
  )

  override fun getStemBranch(general: General): StemBranch {
    return map.getValue(general)
  }

  /**
   * 從地支，找尋「天將」
   * 因為 map value 為「干支」，因此需要 filter
   */
  override fun get(branch: Branch): General {
    return map.filter { it.value.branch == branch }
      .keys.first()
  }
}
