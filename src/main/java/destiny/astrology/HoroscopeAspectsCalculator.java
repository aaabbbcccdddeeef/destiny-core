/**
 * @author smallufo 
 * Created on 2008/6/27 at 上午 4:28:26
 */ 
package destiny.astrology;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/** 利用 HoroscopeAspectsCalculatorIF , 計算命盤之中，星體所呈現的交角，及其容許度 */
public class HoroscopeAspectsCalculator implements Serializable {

  private final Horoscope horoscope;
  
  private final IHoroscopeAspectsCalculator calculator;

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  public HoroscopeAspectsCalculator(Horoscope horoscope , IHoroscopeAspectsCalculator calculator)
  {
    this.horoscope = horoscope;
    this.calculator = calculator;
    this.calculator.setHoroscope(horoscope);
  }
  
  
  public Collection<HoroscopeAspectData> getAspectDataSet(Point... points)
  {
    return getAspectDataSet(Arrays.asList(points) , null);
  }
  
  public Collection<HoroscopeAspectData> getAspectDataSet(@NotNull Collection<Point> points)
  {
    return getAspectDataSet(points , null);
  }

  
  /** 
   * 計算 points 之間所形成的交角 . aspects 為要計算的交角 , 如果 aspects 為 null , 代表不過濾任何交角 <br/>
   * 如果沒有形成任何交角（不太可能 , 除非 points 很少 ），則傳回 size = 0 之 Set
   *  */
  public Collection<HoroscopeAspectData> getAspectDataSet(@NotNull Collection<Point> points , @Nullable Collection<Aspect> aspects)
  {
    Set<HoroscopeAspectData> dataSet = Collections.synchronizedSet(new HashSet<>());

    for (Point point : points) {
      Map<Point, Aspect> map = calculator.getPointAspect(point, points);
      logger.debug("與 {} 形成所有交角的 pointAspect Map = {}" , point , map);
      for (Map.Entry<Point, Aspect> entry : map.entrySet()) {
        //處理過濾交角的事宜
        if (aspects == null || aspects.size() == 0 || aspects.contains(entry.getValue())) {
          HoroscopeAspectData data = new HoroscopeAspectData(point, entry.getKey(), entry.getValue(), horoscope.getAspectError(point, entry.getKey(), entry.getValue()));
          logger.debug("data : twoPoints = {} 形成 {} 角 , 交角 {} 度" , data.getTwoPoints() , data.getAspect() , data.getOrb());
          dataSet.add(data);
        }
      }
    }
    return dataSet;
  }

}
