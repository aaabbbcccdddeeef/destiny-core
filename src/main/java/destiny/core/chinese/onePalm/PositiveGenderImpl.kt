/**
 * Created by smallufo on 2015-05-18.
 */
package destiny.core.chinese.onePalm

import destiny.core.Gender
import destiny.core.chinese.Branch
import destiny.tools.Domain
import destiny.tools.Impl
import destiny.tools.converters.Domains.Palm.KEY_POSITIVE_IMPL
import destiny.tools.getDescription
import destiny.tools.getTitle
import java.io.Serializable
import java.util.*

@Impl([Domain(KEY_POSITIVE_IMPL , PositiveGenderImpl.VALUE , default = true)])
class PositiveGenderImpl : IPositive, Serializable {

  override fun isPositive(gender: Gender, yearBranch: Branch): Boolean {
    return gender === Gender.男
  }

  override fun toString(locale: Locale): String {
    return PositiveImpl.Gender.getTitle(locale)
  }

  override fun getDescription(locale: Locale): String {
    return PositiveImpl.Gender.getDescription(locale)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }

  companion object {
    const val VALUE = "gen"
  }
}
