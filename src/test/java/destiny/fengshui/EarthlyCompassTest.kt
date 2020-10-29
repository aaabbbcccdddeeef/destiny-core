/**
 * @author smallufo
 * @date 2002/9/23
 * @time 上午 11:21:27
 */
package destiny.fengshui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class EarthlyCompassTest {

  @Test
  fun testEarthlyCompass() {
    val compass = EarthlyCompass()

    //子山開始度數 (352.5)
    assertEquals(352.5, compass.getStartDegree(Mountain.子))
    assertEquals(7.5, compass.getEndDegree(Mountain.子))

    //卯山開始度數 ( 82.5)
    assertEquals(82.5, compass.getStartDegree(Mountain.卯))
    assertEquals(97.5, compass.getEndDegree(Mountain.卯))

    //癸山開始度數 (  7.5)
    assertEquals(7.5, compass.getStartDegree(Mountain.癸))
    assertEquals(22.5, compass.getEndDegree(Mountain.癸))

    //午山開始度數 (172.5)
    assertEquals(172.5, compass.getStartDegree(Mountain.午))
    assertEquals(187.5, compass.getEndDegree(Mountain.午))

    //乾山開始度數 (307.5)
    assertEquals(307.5, compass.getStartDegree(Mountain.乾))
    assertEquals(322.5, compass.getEndDegree(Mountain.乾))


    //359度是屬於 (子)
    assertSame(Mountain.子, compass.get(359.0))
    //  0度是屬於 (子)
    assertSame(Mountain.子, compass.get(0.0))
    //  9度是屬於 (癸)
    assertSame(Mountain.癸, compass.get(9.0))
    //128度是屬於 (巽)
    assertSame(Mountain.巽, compass.get(128.0))
    //325度是屬於 (亥)
    assertSame(Mountain.亥, compass.get(325.0))

    assertSame(Mountain.午 , compass.get(180.0))
    assertSame(Mountain.午 , compass.get(180.0 - 7.5))
    assertSame(Mountain.午 , compass.get(180.0 + 7.4999))

    assertSame(Mountain.丁 , compass.get(180.0 + 7.5))

  }
}
