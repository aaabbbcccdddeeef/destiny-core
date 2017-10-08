/**
 * @author smallufo 
 * Created on 2008/5/29 at 上午 3:15:13
 */ 
package destiny.astrology.prediction;

import destiny.astrology.*;
import destiny.core.calendar.Location;
import destiny.core.calendar.TimeTools;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.chrono.ChronoLocalDateTime;

/** 
 * 返照法演算法 , 可以計算 Planet 的返照
 */
public class ReturnContext implements DiscreteIF , Conversable , Serializable {


  private final IHoroscope horoscopeImpl;

  /** 返照法所採用的行星 , 太陽/太陰 , 或是其他 */
  private Planet planet = Planet.SUN;
  
  /** 是否逆推，內定是順推 */
  private boolean converse = false;
  
  /** 是否消除歲差，內定是不計算歲差 */
  private boolean precession = false;
  
  /** 計算星體的介面 */
  private StarPositionWithAzimuthIF starPositionWithAzimuthImpl;
  
  /** 計算星體到黃道幾度的時刻，的介面 */
  private StarTransitIF starTransitImpl;
  
  private final HouseCuspIF houseCuspImpl;
  
  private final ApsisWithAzimuthIF apsisWithAzimuthImpl;
  
  /** 出生時間 , LMT */
  private final ChronoLocalDateTime natalLmt;
  
  /** 出生地點 */
  private final Location natalLoc;
  
  /** 欲計算的目標時間，通常是當下，now，以LMT型態 */
  private final ChronoLocalDateTime nowLmt;
  
  /** 現在所處的地點 */
  private final Location nowLoc;
  
  /** 交角 , 通常是 0 , 代表回歸到原始度數 */
  private double orb = 0;
  
  /** 最完整的 constructor , 連是否逆推 , 是否考慮歲差，都要帶入 */
  public ReturnContext(IHoroscope horoscopeImpl, StarPositionWithAzimuthIF positionWithAzimuthImpl,
                       StarTransitIF starTransitImpl, HouseCuspIF houseCuspImpl, ApsisWithAzimuthIF apsisWithAzimuthImpl,
                       ChronoLocalDateTime natalLmt, Location natalLoc,
                       ChronoLocalDateTime nowLmt, Location nowLoc,
                       Planet planet, double orb, boolean converse, boolean precession)
  {
    this.horoscopeImpl = horoscopeImpl;
    this.starPositionWithAzimuthImpl = positionWithAzimuthImpl;
    this.starTransitImpl = starTransitImpl;
    this.houseCuspImpl = houseCuspImpl;
    this.apsisWithAzimuthImpl = apsisWithAzimuthImpl;
    this.natalLmt = natalLmt;
    this.natalLoc = natalLoc;
    this.nowLmt = nowLmt;
    this.nowLoc = nowLoc;
    this.planet = planet;
    this.orb = orb;
    this.converse = converse;
    this.precession = precession;
  }


  
  /** 對外主要的 method , 取得 return 盤 */
  @NotNull
  public Horoscope getReturnHoroscope() {
    ChronoLocalDateTime natalGmt = TimeTools.getGmtFromLmt(natalLmt , natalLoc);
    ChronoLocalDateTime   nowGmt = TimeTools.getGmtFromLmt(nowLmt , nowLoc);

    ChronoLocalDateTime convergentGmt = getConvergentTime(natalGmt , nowGmt);
    ChronoLocalDateTime convergentLmt = TimeTools.getLmtFromGmt(convergentGmt , nowLoc);

    HouseSystem houseSystem = HouseSystem.PLACIDUS;
    Coordinate coordinate = Coordinate.ECLIPTIC;
    Centric centric = Centric.GEO;
    double temperature = 20;
    double pressure = 1013.25;
    NodeType nodeType = NodeType.MEAN;

    return horoscopeImpl.getHoroscope(convergentLmt , nowLoc , houseSystem , centric , coordinate );
    //return new HoroscopeContext(convergentLmt , nowLoc , houseSystem , coordinate , centric , temperature , pressure , starPositionWithAzimuthImpl , houseCuspImpl , apsisWithAzimuthImpl , nodeType);
  }
  
  
  /**
   * 實作 {@link Mappable }, 注意，在 {@link AbstractProgression}的實作中，並未要求是GMT；但在這裡，必須<b>要求是GMT</b> ！
   * 傳回值也是GMT！
   */
  @Override
  public ChronoLocalDateTime getConvergentTime(@NotNull ChronoLocalDateTime natalGmtTime, @NotNull ChronoLocalDateTime nowGmtTime) {
    double nowGmtJulDay = TimeTools.getGmtJulDay(nowGmtTime);

    Coordinate coordinate = (precession) ? Coordinate.SIDEREAL : Coordinate.ECLIPTIC;
    //先計算出生盤中，該星體的黃道位置
    double natalPlanetDegree = starPositionWithAzimuthImpl.getPosition(planet , natalGmtTime , Centric.GEO , coordinate).getLng();

    //再從現在的時刻，往前(prior , before) 推 , 取得 planet 與 natal planet 呈現 orb 的時刻
    if (!converse) {
      //順推
      return starTransitImpl.getNextTransitGmtDateTime(planet , Utils.getNormalizeDegree(natalPlanetDegree+orb) , coordinate , nowGmtJulDay, false); //false 代表逆推，往before算
    }
    else {
      // converse == true , 逆推
      //從出生時間往前(before)推
      Duration d = Duration.between(nowGmtTime , natalGmtTime).abs();
      double beforeNatalGmtJulDay = TimeTools.getGmtJulDay(nowGmtTime.minus(d));
      //要確認最後一個參數，到底是要用 true , 還是 false , 要找相關定義 , 我覺得這裡應該是順推
      return starTransitImpl.getNextTransitGmtDateTime(planet , Utils.getNormalizeDegree(natalPlanetDegree+orb) , coordinate , beforeNatalGmtJulDay, true); //true 代表順推 , 往 after 算
    }
  }


  
  @Override
  public void setConverse(boolean value)
  {
    this.converse = value;
  }

  /** 是否逆推 , true 代表「是」，逆推！ */
  @Override
  public boolean isConverse()
  {
    return converse;
  }

  /** 是否消除歲差 */
  public boolean isPrecession()
  {
    return precession;
  }

  /** 設定是否消除歲差 */
  public void setPrecession(boolean value)
  {
    this.precession = value;
  }

  public void setStarPositionWithAzimuthImpl(StarPositionWithAzimuthIF starPositionWithAzimuthImpl)
  {
    this.starPositionWithAzimuthImpl = starPositionWithAzimuthImpl;
  }

  public void setStarTransitImpl(StarTransitIF starTransitImpl)
  {
    this.starTransitImpl = starTransitImpl;
  }

  public double getOrb()
  {
    return orb;
  }

  public void setOrb(double orb)
  {
    this.orb = orb;
  }



}
