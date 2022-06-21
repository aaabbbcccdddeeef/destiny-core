/**
 * Created by smallufo on 2022-06-21.
 */
package destiny.core.chinese.ziwei.rules

import destiny.core.chinese.ziwei.FlowType
import destiny.core.chinese.ziwei.House
import destiny.core.chinese.ziwei.IPlate
import destiny.core.chinese.ziwei.ZiweiConfig
import java.time.chrono.ChronoLocalDateTime
import javax.inject.Named


/**
 * 運限命遷有本命鸞喜 (對星)
 */
@Named
class ZRule10 : AbstractSeqBooleanRule() {

  override fun testSection(sectionPlate: IPlate, lmt: ChronoLocalDateTime<*>, config: ZiweiConfig): Boolean {
    return sectionPlate.getHouseDataOf(House.命宮, FlowType.SECTION)!!.stars.any { 鸞喜.contains(it) }
      && sectionPlate.getHouseDataOf(House.遷移, FlowType.SECTION)!!.stars.any { 鸞喜.contains(it) }
  }
}
