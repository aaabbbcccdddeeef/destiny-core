/**
 * Created by smallufo on 2020-03-11.
 */
package destiny.tools.converters

import destiny.core.chinese.IClockwise
import destiny.core.chinese.onePalm.IPositive
import destiny.core.chinese.ziwei.*

object Domains {

  /** 紫微 */
  object Ziwei {

    /** 紫微流年 [IFlowYear] */
    const val KEY_FLOW_YEAR = "flowYear"

    /** 紫微流月 [IFlowMonth] */
    const val KEY_FLOW_MONTH = "flowMonth"

    /** 紫微流日 [IFlowDay] */
    const val KEY_FLOW_DAY = "flowDay"

    /** 紫微流時 [IFlowHour] */
    const val KEY_FLOW_HOUR = "flowHour"

    /** 紫微起大運 [IBigRange] */
    const val KEY_BIG_RANGE = "bigRange"
  }

  /** 金口訣 */
  object Pithy {

    /** 貴神順推逆推 [IClockwise] */
    const val KEY_CLOCKWISE = "clockwise"

  }

  /** 一掌經 */
  object Palm {

    /** 一掌經：順逆算法 [IPositive] */
    const val KEY_POSITIVE_IMPL = "posImpl"
  }

  /** 二十八星宿 */
  object LunarStation {

    /** 月禽 */
    object MonthImpl {
      const val KEY_GENERAL = "lsM"
      const val KEY_SELECT = "lsM_sel"
    }

    /** 時禽 */
    object HourImpl {
      const val KEY_GENERAL = "lsH"
      const val KEY_SELECT = "lsH_Sel"
    }

    const val KEY_HIDDEN_VENUS_FOE = "lsHvf"
  }
}
