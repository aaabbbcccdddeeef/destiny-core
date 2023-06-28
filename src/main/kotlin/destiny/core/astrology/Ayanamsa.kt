/**
 * @author smallufo
 * Created on 2007/12/5 at 上午 1:40:46
 */
package destiny.core.astrology

import destiny.tools.ILocaleString
import java.util.*

fun Ayanamsa.asLocaleString() = object : ILocaleString {
  private val resource = "destiny.core.astrology.Astrology"
  override fun getTitle(locale: Locale): String {
    return ResourceBundle.getBundle(resource, locale).getString(this@asLocaleString.nameKey)
  }
}

fun Ayanamsa.getTitle(locale: Locale): String {
  return this.asLocaleString().getTitle(locale)
}

/**
 * Ayanamsha is a Sanskrit word and is to be pronounced with the third "a" long, so: "Ayana-amsha".
 * "ayana" means "precession" and "amsha" means "component".
 */
enum class Ayanamsa(val nameKey: String) {
  FAGAN_BRADLEY("Ayana.FAGAN_BRADLEY"),
  LAHIRI("Ayana.LAHIRI"),
  DELUCE("Ayana.DELUCE"),
  RAMAN("Ayana.RAMAN"),
  USHASHASHI("Ayana.USHASHASHI"),
  KRISHNAMURTI("Ayana.KRISHNAMURTI"),
  DJWHAL_KHUL("Ayana.DJWHAL_KHUL"),
  YUKTESHWR("Ayana.YUKTESWAR"),
  JN_BHASIN("Ayana.JN_BHASIN"),
  BABYL_KUGLER1("Ayana.BABYL_KUGLER1"),
  BABYL_KUGLER2("Ayana.BABYL_KUGLER2"),
  BABYL_KUGLER3("Ayana.BABYL_KUGLER3"),
  BABYL_HUBER("Ayana.BABYL_HUBER"),
  BABYL_ETPSC("Ayana.BABYL_ETPSC"),
  ALDEBARAN_15TAU("Ayana.ALDEBARAN_15TAU"),
  HIPPARCHOS("Ayana.HIPPARCHOS"),
  SASSANIAN("Ayana.SASSANIAN"),
  GALCENT_0SAG("Ayana.GALCENT_0SAG"),
  J2000("Ayana.J2000"),
  J1900("Ayana.J1900"),
  B1950("Ayana.B1950");
}
