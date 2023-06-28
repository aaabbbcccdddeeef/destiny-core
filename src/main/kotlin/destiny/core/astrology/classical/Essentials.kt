/**
 * Created by smallufo on 2017-12-21.
 */
package destiny.core.astrology.classical

import destiny.core.DayNight
import destiny.core.astrology.*
import destiny.core.astrology.classical.Dignity.RULER

/** Ruler , +5 */
interface IRuler {

  /** @param dayNight 若有傳值，取得「日夜區分版本」的 [RULER] (nullable), 否則取得一般版本的 [RULER] (非null) */
  fun ZodiacSign.getRulerPoint(dayNight: DayNight? = null): AstroPoint?

  /** 不分日夜，取得此行星為哪個星座的主人 , 傳回的為 非null值 . size 固定為 2  */
  fun Planet.getRulingSigns(): Set<ZodiacSign>

  /**
   * 取得此行星在日、夜 是什麼星座的 [RULER]
   * @param dayNight 若為 null 則取得不分日夜的版本(non-null)
   */
  fun Planet.getRulingSign(dayNight: DayNight): ZodiacSign?
}

/** Detriment , -5 */
interface IDetriment {

  /** 在此星座 陷(-5) 的行星為何 */
  fun ZodiacSign.getDetrimentPoint(): Planet

  /** 此行星在哪些星座 陷 (-5) */
  fun Planet.getDetrimentSigns(): Set<ZodiacSign>

  /** 在此星座 ，區分日夜， 陷(-5) 的行星為何?  (好像沒看過 Detriment 還區分日夜) */
  fun ZodiacSign.getDetrimentPoint(dayNight: DayNight?): Planet?

}


/** Exaltation , +4 */
interface IExaltation {

  /** 哪顆星體在此星座 擢升 (EXALT , +4) , 必定為 1 or 0 顆星 */
  fun ZodiacSign.getExaltPoint(): AstroPoint?

  /** 此星體在哪個星座 擢升 (EXALT , +4) , 前者逆函數 */
  fun AstroPoint.getExaltSign(): ZodiacSign?

  /** 取得在此星座得到 擢升 (EXALT , +4) 的星體及度數 */
  fun ZodiacSign.getExaltPointDegree(): PointDegree?

}


/** Fall , -4 */
interface IFall {

  /** 哪顆星體在此星座 落 (FALL , -4) , 必定為 1 or 0 顆星 */
  fun ZodiacSign.getFallPoint(): AstroPoint?

  /** 此星體在哪個星座 落 (FALL , -4) , 前者逆函數 */
  fun AstroPoint.getFallingSign(): ZodiacSign?

  /** 取得在此星座得到 落 (FALL , -4) 的星體及度數 */
  fun ZodiacSign.getPointDegree(): PointDegree?
}


/** Triplicity , +3 */
interface ITriplicity {

  /** 哪顆星在此星座得到三分相 (+3) */
  fun ZodiacSign.getTriplicityPoint(dayNight: DayNight): AstroPoint

  /** 共管 , Partner */
  fun ZodiacSign.getPartner() : AstroPoint?
}


/** Term , +2 */
interface ITerm {

  /** 取得黃道帶上的某點，其 Terms 是哪顆星 , 0<=degree<360  */
  fun getPoint(degree: ZodiacDegree): AstroPoint

  /** 取得某星座某度，其 Terms 是哪顆星 , 0<=degree<30  */
  fun ZodiacSign.getTermPoint(degree: Double): AstroPoint
}

/** Face (十度區 , Decans ) , +1 */
interface IFace {

  /** 取得黃道帶上的某點，其 Face 是哪顆星 , 0<=degree<360  */
  fun getPoint(degree: ZodiacDegree): AstroPoint

  /** 取得某星座某度，其 Face 是哪顆星 , 0<=degree<30  */
  fun ZodiacSign.getFacePoint(degree: Double): AstroPoint
}
