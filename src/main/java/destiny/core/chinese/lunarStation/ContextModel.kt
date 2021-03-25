/**
 * Created by smallufo on 2021-03-22.
 */
package destiny.core.chinese.lunarStation

import destiny.core.Gender
import destiny.core.ITimeLoc
import destiny.core.Scale
import destiny.core.astrology.LunarStation
import destiny.core.calendar.eightwords.IEightWords
import java.io.Serializable
import java.time.LocalDateTime


interface IContextModel : Serializable {
  val eightwords: IEightWords

  val year: LunarStation
  val month: LunarStation
  val day: LunarStation
  val hour: LunarStation

  fun getStation(scale: Scale): LunarStation {
    return when (scale) {
      Scale.YEAR -> year
      Scale.MONTH -> month
      Scale.DAY -> day
      Scale.HOUR -> hour
    }
  }

  /** 翻禽（彼禽） */
  val oppo: LunarStation

  /** 翻禽（彼禽）所在宮位 */
  val oppoHouse: OppoHouse

  /** 活曜（我禽） */
  val self: LunarStation

  /** 活曜（我禽）所在宮位 */
  val selfHouse: SelfHouse

  /** 暗金伏斷 */
  val hiddenVenusFoe: Set<Pair<Scale, Scale>>
}

data class ContextModel(override val eightwords: IEightWords,
                        override val year: LunarStation,
                        override val month: LunarStation,
                        override val day: LunarStation,
                        override val hour: LunarStation,
                        override val oppo: LunarStation,
                        override val oppoHouse: OppoHouse,
                        override val self: LunarStation,
                        override val selfHouse: SelfHouse,
                        override val hiddenVenusFoe: Set<Pair<Scale, Scale>>) : IContextModel, Serializable

interface IModernContextModel : IContextModel {

  val gender: Gender

  val created: LocalDateTime

  val timeLoc: ITimeLoc

  val place: String?

  enum class Method {
    /** 當下時間 [created] 排盤 */
    NOW,

    /** 指定時間 (maybe 出生盤) */
    SPECIFIED,

    /** 今日抽時 */
    RANDOM_HOUR,

    /** 隨機時刻 */
    RANDOM_TIME
  }

  val method: Method

  val description: String?
}

data class ModernContextModel(val contextModel: IContextModel,
                              override val gender: Gender,
                              override val created: LocalDateTime,
                              override val timeLoc: ITimeLoc,
                              override val place: String?,
                              override val method: IModernContextModel.Method,
                              override val description: String?) : IModernContextModel, IContextModel by contextModel

