/**
 * @author smallufo
 * Created on 2008/1/16 at 上午 12:21:43
 */
package destiny.core.astrology

import mu.KotlinLogging
import java.util.*
import kotlin.test.*

class FixedStarTest {

  private val logger = KotlinLogging.logger { }

  @Test
  fun testFixedStar() {

    assertEquals("畢宿五", FixedStar.ALDEBARAN.toString(Locale.TAIWAN))
    assertEquals("毕宿五", FixedStar.ALDEBARAN.toString(Locale.SIMPLIFIED_CHINESE))
    assertEquals("Aldebaran", FixedStar.ALDEBARAN.toString(Locale.ENGLISH))
    //assertEquals("Aldebaran", FixedStar.ALDEBARAN.toString(Locale.FRANCE))

    assertEquals("畢", FixedStar.ALDEBARAN.getAbbreviation(Locale.TAIWAN))
    assertEquals("毕", FixedStar.ALDEBARAN.getAbbreviation(Locale.SIMPLIFIED_CHINESE))
    assertEquals("Ald", FixedStar.ALDEBARAN.getAbbreviation(Locale.ENGLISH))
    //assertEquals("Ald", FixedStar.ALDEBARAN.getAbbreviation(Locale.FRANCE))


    FixedStar.values.forEach {
      assertNotNull(it)
    }

    for (each in FixedStar.values) {
      assertNotNull(each.toString())
      assertNotSame('!', each.toString()[0])

      val locale = Locale.ENGLISH
      assertNotNull(each.toString(locale))
      assertNotSame('!', each.toString(locale)[0])

      //System.out.println(each.toString() + ":" + each.getAbbreviation() + ":" + each.getAbbreviation(locale));
      assertNotNull(each.getAbbreviation(Locale.TAIWAN))
      assertNotSame('!', each.getAbbreviation(Locale.TAIWAN)[0])

      assertNotNull(each.getAbbreviation(locale))
      assertNotSame('!', each.getAbbreviation(locale)[0])
    }
  }

  @Test
  fun testStringConvert() {
    FixedStar.values.forEach { star ->
      logger.info { "$star = ${star.toString(Locale.ENGLISH)}" }
      assertSame(star, FixedStar.fromString(star.toString(Locale.ENGLISH)))
    }
  }
}
