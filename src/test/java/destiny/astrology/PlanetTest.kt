/**
 * @author smallufo
 * Created on 2007/6/12 at 上午 6:09:35
 */
package destiny.astrology


import java.util.*
import kotlin.test.*

class PlanetTest {

  /** 測試從 "sun" 取得 Planet.SUN  */
  @Test
  fun testGetPlanetFromString() {

    assertSame(Planet.MOON, Planet.fromString("MOON"))

    assertSame(Planet.SUN, Planet.fromString("sun"))
    assertSame(Planet.SUN, Planet.fromString("SUN"))
    assertSame(Planet.SUN, Planet.fromString("Sun"))
    assertNull(Planet.fromString("xxx"))
  }

  /** 將 太陽 up-case 再 down-cast , 比對 equality 以及 same  */
  @Test
  fun testPlanetEqual() {
    val sun1 = Planet.SUN
    val sun2 = Planet.SUN

    val points = setOf<Point>(sun2)

    val pointsIt = points.iterator()
    while (pointsIt.hasNext()) {
      val p = pointsIt.next()

      if (p is Planet) {
        assertSame(p, sun1)
        assertSame(p, sun1)
      } else
        throw RuntimeException("Error , it should be Planet ")
    }
  }


  @Test
  fun testPlanet() {
    assertEquals("太陽", Planet.SUN.getName(Locale.TAIWAN))
    assertEquals("日", Planet.SUN.getAbbreviation(Locale.TAIWAN))

    val locale = Locale.ENGLISH
    assertEquals("Sun", Planet.SUN.getName(locale))
    assertEquals("Su", Planet.SUN.getAbbreviation(locale))
  }

  @Test
  fun testPlanets() {


    println(Planet.SUN)
    println(Planet.MOON)
    println(Planet.MERCURY)
    println(Planet.VENUS)
    println(Planet.MARS)
    println(Planet.JUPITER)
    println(Planet.SATURN)
    println(Planet.URANUS)
    println(Planet.NEPTUNE)
    println(Planet.PLUTO)

    Planet.classicalValues.forEach { println(it) }


    println("\nclassical values :")
    for (planet in Planet.classicalValues) {
      assertNotNull(planet)
      assertNotNull(planet.toString())
      println(planet.toString())
    }

    println("\nall values :")
    for (planet in Planet.values) {
      assertNotNull(planet)
      assertNotNull(planet.toString())
    }

    val points = setOf<Point>(*Planet.values , *FixedStar.values) as Collection<Point>
    println(points)
    points.forEach { println(it) }
  }

  @Test
  fun testCompare() {
    assertTrue(Planet.SUN < Planet.MOON)
    assertTrue(Planet.MOON < Planet.MERCURY)
    assertTrue(Planet.MERCURY < Planet.VENUS)
    assertTrue(Planet.VENUS < Planet.MARS)
    assertTrue(Planet.MARS < Planet.JUPITER)
    assertTrue(Planet.JUPITER < Planet.SATURN)
    assertTrue(Planet.SATURN < Planet.URANUS)
    assertTrue(Planet.URANUS < Planet.NEPTUNE)
    assertTrue(Planet.NEPTUNE < Planet.PLUTO)
  }

}