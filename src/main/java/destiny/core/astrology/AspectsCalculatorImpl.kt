/**
 * Created by smallufo on 2019-10-18.
 */
package destiny.core.astrology

import com.google.common.collect.Sets
import destiny.core.astrology.IPointAspectPattern.Type.APPLYING
import destiny.core.astrology.IPointAspectPattern.Type.SEPARATING
import java.io.Serializable
import kotlin.math.abs

class AspectsCalculatorImpl(val aspectEffectiveImpl: IAspectEffective,
                            private val pointPosFuncMap: Map<AstroPoint, IPosition<*>>) : IAspectsCalculator, Serializable {


  override fun getAspectPatterns(p1: AstroPoint, p2: AstroPoint,
                                 p1PosMap: Map<AstroPoint, IPos>, p2PosMap: Map<AstroPoint, IPos>,
                                 laterForP1: () -> IPos?, laterForP2: () -> IPos?, aspects: Collection<Aspect>): IPointAspectPattern? {
    return aspects
      .intersect(aspectEffectiveImpl.applicableAspects)
      .asSequence()
      .map { aspect ->
        aspect to aspectEffectiveImpl.getEffectiveErrorAndScore(p1, p1PosMap.getValue(p1).lngDeg, p2, p2PosMap.getValue(p2).lngDeg, aspect)
      }.firstOrNull { (_, maybeErrorAndScore) -> maybeErrorAndScore != null }
      ?.let { (aspect, maybeErrorAndScore) -> aspect to maybeErrorAndScore!! }
      ?.let { (aspect, errorAndScore) ->
        val error = errorAndScore.first
        val score = errorAndScore.second

        laterForP1.invoke()?.lngDeg?.let { deg1Next ->
          laterForP2.invoke()?.lngDeg?.let { deg2Next ->
            val planetsAngleNext = deg1Next.getAngle(deg2Next)
            val errorNext = abs(planetsAngleNext - aspect.degree)

            val type = if (errorNext <= error) APPLYING else SEPARATING
            PointAspectPattern.of(p1, p2, aspect, type, error, score)
            //AspectData.of(p1, p2, aspect, error, score, type)
          }
        }

      }
  }


  private fun IHoroscopeModel.getAspectData(twoPoints: Set<AstroPoint>, aspects: Collection<Aspect>): IPointAspectPattern? {

    val posMap: Map<AstroPoint, IPosWithAzimuth> = this.positionMap

    return twoPoints
      .takeIf { it.size == 2 } // 確保裡面只有兩個 Point
      ?.takeIf { set -> posMap.keys.containsAll(set) }
      ?.takeIf { set -> !set.all { it is Axis } } // 過濾四角點互相形成的交角
      ?.takeIf { set -> !set.all { it is LunarNode } } // 過濾南北交點對沖
      ?.let {
        val (p1, p2) = twoPoints.iterator().let { it.next() to it.next() }


        // 8.64 seconds
        val later = this.gmtJulDay.plus(0.0001)

        val laterForP1: () -> IPos? = { pointPosFuncMap[p1]?.getPosition(later, location) }
        val laterForP2: () -> IPos? = { pointPosFuncMap[p2]?.getPosition(later, location) }

        getAspectPatterns(p1, p2, posMap, posMap, laterForP1, laterForP2, aspects)
      }

  }

  /** 針對整體 */
  override fun IHoroscopeModel.getAspectPatterns(points: Collection<AstroPoint>, aspects: Collection<Aspect>): Set<IPointAspectPattern> {
    return Sets.combinations(points.toSet(), 2)
      .asSequence()
      .mapNotNull { this.getAspectData(it, aspects) }
      .toSet()
  }

  /** 針對單一 */
  override fun getAspectPatterns(point: AstroPoint,
                                 h: IHoroscopeModel,
                                 points: Collection<AstroPoint>,
                                 aspects: Collection<Aspect>): Set<IPointAspectPattern> {
    return points
      .asSequence()
      .map { eachPoint -> setOf(point, eachPoint) }
      .mapNotNull { twoPoints -> h.getAspectData(twoPoints, aspects) }
      .toSet()
  }

  override fun getPointAspectAndScore(point: AstroPoint,
                                      positionMap: Map<AstroPoint, IPos>,
                                      points: Collection<AstroPoint>,
                                      aspects: Collection<Aspect>): Set<Triple<AstroPoint, Aspect, Double>> {
    return positionMap[point]?.lngDeg?.let { starDeg ->
      points
        .filter { it !== point }
        .filter { positionMap.containsKey(it) }
        .filter { eachPoint -> !(point is Axis && eachPoint is Axis) }  // 過濾四角點互相形成的交角
        .filter { eachPoint -> !(point is LunarNode && eachPoint is LunarNode) } // 過濾南北交點對沖
        .flatMap { eachPoint ->
          val eachDeg = positionMap.getValue(eachPoint).lngDeg
          aspects
            .intersect(aspectEffectiveImpl.applicableAspects)
            .map { eachAspect ->
              eachAspect to aspectEffectiveImpl.getEffectiveErrorAndScore(point, starDeg, eachPoint, eachDeg, eachAspect)
            }
            .filter { (_, maybeErrorAndScore) -> maybeErrorAndScore != null }
            .map { (aspect, maybeErrorAndScore) -> aspect to maybeErrorAndScore!!.second }
            .map { (aspect, score) ->
              Triple(eachPoint, aspect, score)
            }
        }.toSet()
    } ?: emptySet()
  }
}


