/**
 * Created by smallufo on 2017-04-12.
 */
package destiny.core.chinese.ziwei

import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import destiny.core.chinese.Stem
import destiny.core.chinese.Stem.*
import destiny.core.chinese.ziwei.ITransFour.Value.*
import java.util.*

/**
 * 紫微斗數全書、閩派、南派
 */
class TransFourFullBookImpl : TransFourAbstractImpl() {

  override fun getTable(): Table<Stem, ITransFour.Value, ZStar> {
    return dataTable
  }

  override fun getDescription(locale: Locale): String {
    return "紫微斗數全書、閩派、南派"
  }

  companion object {

    private val dataTable = ImmutableTable.Builder<Stem, ITransFour.Value, ZStar>()
      .put(甲, 祿, StarMain.廉貞)
      .put(甲, 權, StarMain.破軍)
      .put(甲, 科, StarMain.武曲)
      .put(甲, 忌, StarMain.太陽)

      .put(乙, 祿, StarMain.天機)
      .put(乙, 權, StarMain.天梁)
      .put(乙, 科, StarMain.紫微)
      .put(乙, 忌, StarMain.太陰)

      .put(丙, 祿, StarMain.天同)
      .put(丙, 權, StarMain.天機)
      .put(丙, 科, StarLucky.文昌)
      .put(丙, 忌, StarMain.廉貞)

      .put(丁, 祿, StarMain.太陰)
      .put(丁, 權, StarMain.天同)
      .put(丁, 科, StarMain.天機)
      .put(丁, 忌, StarMain.巨門)

      // 戊 有差別
      .put(戊, 祿, StarMain.貪狼)
      .put(戊, 權, StarMain.太陰)
      .put(戊, 科, StarLucky.右弼)
      .put(戊, 忌, StarMain.天機)

      .put(己, 祿, StarMain.武曲)
      .put(己, 權, StarMain.貪狼)
      .put(己, 科, StarMain.天梁)
      .put(己, 忌, StarLucky.文曲)

      // 庚 有差別
      .put(庚, 祿, StarMain.太陽)
      .put(庚, 權, StarMain.武曲)
      .put(庚, 科, StarMain.天同)
      .put(庚, 忌, StarMain.太陰)

      .put(辛, 祿, StarMain.巨門)
      .put(辛, 權, StarMain.太陽)
      .put(辛, 科, StarLucky.文曲)
      .put(辛, 忌, StarLucky.文昌)

      // 壬 有差別
      .put(壬, 祿, StarMain.天梁)
      .put(壬, 權, StarMain.紫微)
      .put(壬, 科, StarMain.天府)
      .put(壬, 忌, StarMain.武曲)

      .put(癸, 祿, StarMain.破軍)
      .put(癸, 權, StarMain.巨門)
      .put(癸, 科, StarMain.太陰)
      .put(癸, 忌, StarMain.貪狼)

      .build()
  }
}