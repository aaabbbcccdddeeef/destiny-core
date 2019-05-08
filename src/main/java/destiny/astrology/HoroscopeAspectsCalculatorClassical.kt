/**
 * @author smallufo
 * Created on 2008/6/19 at 上午 2:19:45
 */
package destiny.astrology

import destiny.astrology.classical.PointDiameterAlBiruniImpl
import destiny.astrology.classical.PointDiameterLillyImpl

import destiny.astrology.classical.AspectEffectiveClassical
import destiny.astrology.classical.IPointDiameter

import java.io.Serializable
import java.util.*

/** 古典占星術，列出一張星盤中呈現交角的星體以及角度 的實作  */
class HoroscopeAspectsCalculatorClassical(private val classical: AspectEffectiveClassical) : IHoroscopeAspectsCalculator, Serializable {

  /** 取得交角容許度的實作，例如 ( [PointDiameterAlBiruniImpl] 或是 [PointDiameterLillyImpl] )  */
  var planetOrbsImpl: IPointDiameter = classical.planetOrbsImpl

  override fun getPointAspect(point: Point, positionMap: Map<Point, IPos>, points: Collection<Point>): Map<Point, Aspect> {

    return if (point is Planet) {
      val planetDeg = positionMap.getValue(point).lng
      //只比對 0 , 60 , 90 , 120 , 180 五個度數
      points
        .filter { it !== point }
        .flatMap { eachPoint ->
          val eachPlanetDeg = positionMap.getValue(eachPoint).lng
          Aspect.getAngles(Aspect.Importance.HIGH)
            .filter { classical.isEffective(point, planetDeg, eachPoint, eachPlanetDeg, it) }
            .map { eachPoint to it }
        }.toMap()
    } else {
      // 非行星不計算
      emptyMap()
    }
  }

  override fun getTitle(locale: Locale): String {
    return "古典占星術 : " + classical.planetOrbsImpl.getTitle(locale)
  }

  override fun getDescription(locale: Locale): String {
    return "古典占星術實作 : " + classical.planetOrbsImpl.getDescription(locale)
  }


}
