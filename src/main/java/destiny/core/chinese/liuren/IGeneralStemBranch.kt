/**
 * Created by smallufo on 2015-05-31.
 */
package destiny.core.chinese.liuren

import destiny.core.Descriptive
import destiny.core.chinese.Branch
import destiny.core.chinese.StemBranch

/** 「天將」的所屬干支設定 , 有爭議者為
 * 金口訣：壬子玄武 癸亥天后
 * 大六壬：壬子天后 癸亥玄武
 */
interface IGeneralStemBranch : Descriptive {

  fun getStemBranch(general: General): StemBranch

  /** 從地支，找尋「天將」  */
  operator fun get(branch: Branch): General
}
