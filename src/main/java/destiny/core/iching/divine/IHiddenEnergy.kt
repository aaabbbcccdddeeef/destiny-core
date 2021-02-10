/**
 * @author smallufo
 * @date 2002/8/20
 * @time 下午 04:04:28
 */
package destiny.core.iching.divine

import destiny.core.Descriptive
import destiny.core.chinese.StemBranch
import destiny.core.iching.IHexagram

/** 伏神介面  */
interface IHiddenEnergy : Descriptive {
  /**
   * 取得第幾爻的伏神
   * @param lineIndex 1 <= lineIndex <=6
   */
  fun getStemBranch(hexagram: IHexagram, settings: ISettingsOfStemBranch, lineIndex: Int): StemBranch?
}
