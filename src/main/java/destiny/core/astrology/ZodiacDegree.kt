/**
 * Created by smallufo on 2021-05-19.
 */
package destiny.core.astrology

import destiny.tools.CircleTools.aheadOf
import destiny.tools.CircleTools.normalize
import java.io.Serializable

/** 黃道帶度數 */
@JvmInline
value class ZodiacDegree private constructor(val value: Double) : Serializable {

  val sign: ZodiacSign
    get() = ZodiacSign.of(value)

  val signDegree: Pair<ZodiacSign, Double>
    get() = sign to value % 30

  val intDeg : Int
    get() = (value % 30).toInt()

  val intMin: Int
    get() = ((value - value.toInt()) * 60).toInt()

  fun getAngle(to: ZodiacDegree): Double {
    return Companion.getAngle(this.value, to.value)
  }

  fun getAngle(to: Double): Double {
    return Companion.getAngle(this.value, to)
  }

  /**
   * @return 計算 此點 是否在 [to] 的東邊 (度數小，為東邊) , true 就是東邊 , false 就是西邊(含對沖/合相)
   */
  fun isOriental(to: ZodiacDegree): Boolean {
    return if (value < to.value && to.value - value < 180)
      true
    else
      value > to.value && value - to.value > 180
  }


  /**
   * @return 計算 此點 是否在 [to] 的西邊 (度數大，為西邊) , true 就是西邊 , false 就是東邊(含對沖/合相)
   */
  fun isOccidental(to: ZodiacDegree): Boolean {
    return if (value < to.value && to.value - value > 180)
      true
    else
      value > to.value && value - to.value < 180
  }

  fun normalize(): ZodiacDegree {
    return when {
      value >= 360 || value < 0 -> ZodiacDegree(value.normalize())
      else                      -> this
    }
  }

  fun aheadOf(other: ZodiacDegree): Double {
    return value.aheadOf(other.value)
  }

  /**
   * 此度數是否被 [from] 與 [to] 包圍 ( 180度(不含) 以內)
   * 其中 oriental 者 inclusive , 另一邊 exclusive
   */
  fun between(from: ZodiacDegree, to: ZodiacDegree): Boolean {
    return if (from.isOriental(to)) {
      from.value == this.value ||
        from.isOriental(this) && this.isOriental(to)
    } else {
      to.value == this.value ||
        from.isOccidental(this) && this.isOccidental(to)
    }
  }

  operator fun compareTo(other: ZodiacDegree): Int {
    val v = value - other.value
    return when {
      v == 0.0  -> 0
      value < 0 -> -1
      else      -> 1
    }
  }

  operator fun plus(other: ZodiacDegree): ZodiacDegree {
    return (value + other.value).toZodiacDegree()
  }

  operator fun plus(other: Int): ZodiacDegree {
    return (value + other).toZodiacDegree()
  }

  operator fun plus(other: Double): ZodiacDegree {
    return (value + other).toZodiacDegree()
  }

  operator fun minus(other: ZodiacDegree): ZodiacDegree {
    return (value - other.value).toZodiacDegree()
  }

  operator fun minus(other: Int): ZodiacDegree {
    return (value - other).toZodiacDegree()
  }

  operator fun minus(other: Double): ZodiacDegree {
    return (value - other).toZodiacDegree()
  }


  companion object {

    fun Number.toZodiacDegree(): ZodiacDegree {
      return ZodiacDegree(this.toDouble().normalize())
    }

    /**
     * @return 計算黃道帶上兩個度數的交角 , 其值必定小於等於 180度
     */
    fun getAngle(from: Double, to: Double): Double {
      return when {
        from - to >= 180  -> 360 - from + to
        from - to >= 0    -> from - to
        from - to >= -180 -> to - from
        else              -> from + 360 - to  // (from - to < -180)
      }
    }

    object OrientalComparator : Comparator<ZodiacDegree> {
      override fun compare(o1: ZodiacDegree, o2: ZodiacDegree): Int {
        return if (o1.value == o2.value) {
          0
        } else if (o1.isOriental(o2))
          -1
        else
          1
      }

    }
  }
}
