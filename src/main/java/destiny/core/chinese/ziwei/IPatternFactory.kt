/**
 * Created by smallufo on 2018-07-03.
 */
package destiny.core.chinese.ziwei

import destiny.core.IPattern
import destiny.core.IPatternParasDescription
import destiny.core.Paragraph
import destiny.core.chinese.Branch
import java.io.Serializable
import java.util.*


interface IPatternContext {

  enum class Target {
    /** 只針對命宮 */
    MAIN,
    /** 計算所有宮位 */
    EVERY
  }

  val target: Target
}


interface IPlateDescriptionsFactory {
  fun getPatternDescriptions(plate: IPlate, pContext: IPatternContext): List<IPatternParasDescription>

  fun getDescription(pattern: IPattern): IPatternParasDescription?
}

data class PatternParasDescription(
  override val pattern: IPattern,
  override val paras: List<Paragraph>) : Serializable, IPatternParasDescription, IPattern by pattern {

  /**
   * 沒有其他語系，就傳中文的 [IPattern.name] 即可
   */
  override fun toString(locale: Locale): String {
    return pattern.name
  }

  override fun getDescription(locale: Locale): String {
    return pattern.notes ?: ""
  }
}


class PatternContext(
  override val target: IPatternContext.Target) : IPatternContext, Serializable

interface IPatternFactory {
  /** 可以指定宮位 (傳入地支) */
  fun getPattern(it: IPlate, pContext: IPatternContext): IPattern?
}

/** 單純命宮實作 */
abstract class PatternSingleImpl : IPatternFactory, Serializable {
  override fun getPattern(it: IPlate, pContext: IPatternContext): IPattern? {
    return getSingle(it, pContext)
  }

  abstract fun getSingle(it: IPlate, pContext: IPatternContext): IPattern?
}

/** 支援多重宮位 */
abstract class PatternMultipleImpl : IPatternFactory, Serializable {
  override fun getPattern(it: IPlate, pContext: IPatternContext): IPattern? {
    return when (pContext.target) {
      IPatternContext.Target.MAIN -> getMultiple(it, setOf(it.mainHouse.branch), pContext)
      IPatternContext.Target.EVERY -> getMultiple(it, Branch.values().toSet(), pContext)
    }
  }

  abstract fun getMultiple(it: IPlate, branches: Set<Branch>, pContext: IPatternContext): IPattern?
}

