/**
 * Created by smallufo on 2015-05-27.
 */
package destiny.core.chinese.liuren

import destiny.core.chinese.liuren.General.*
import destiny.tools.*
import destiny.tools.converters.Domains.Pithy.KEY_GENERAL_SEQ
import java.io.Serializable
import java.util.*

@Impl([Domain(KEY_GENERAL_SEQ, GeneralSeqZhaoImpl.VALUE)])
class GeneralSeqZhaoImpl : IGeneralSeq, Serializable {

  override fun toString(locale: Locale): String {
    return GeneralSeq.Zhao.getTitle(locale)
  }

  override fun getDescription(locale: Locale): String {
    return GeneralSeq.Zhao.getDescription(locale)
  }

  override fun next(from: General, n: Int): General {
    return get(getIndex(from) + n)
  }

  companion object {

    const val VALUE = "zhao"

    private val ARRAY = arrayOf(貴人, 青龍, 六合, 勾陳, 螣蛇, 朱雀, 太常, 白虎, 太陰, 天空, 玄武, 天后)

    private val list = listOf(*ARRAY)

    private operator fun get(index: Int): General {
      return ArrayTools[ARRAY, index]
    }

    private fun getIndex(g: General): Int {
      return list.indexOf(g)
    }
  }
}
