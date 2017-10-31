/**
 * Created by smallufo on 2017-06-22.
 */
package destiny.core.chinese.ziwei;

import destiny.astrology.*;
import destiny.core.calendar.Location;
import destiny.core.calendar.eightwords.IRisingSign;
import destiny.core.chinese.Branch;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import java.io.Serializable;
import java.time.chrono.ChronoLocalDateTime;

/**
 * 利用上升星座，計算命宮
 * 利用月亮星座，計算身宮
 */
public class MainBodyHouseAstroImpl implements IMainBodyHouse, Serializable {

  private final IRisingSign risingSignImpl;

  private final IStarPosition starPositionImpl;

  public MainBodyHouseAstroImpl(IRisingSign risingSignImpl, IStarPosition starPositionImpl) {
    this.risingSignImpl = risingSignImpl;
    this.starPositionImpl = starPositionImpl;
  }

  @Override
  public Tuple2<Branch , Branch> getMainBodyHouse(ChronoLocalDateTime lmt, Location loc) {
    Branch mainHouse = risingSignImpl.getRisingSign(lmt , loc , HouseSystem.PLACIDUS , Coordinate.ECLIPTIC).getBranch();
    Position moonPos = starPositionImpl.getPosition(Planet.MOON , lmt , loc , Centric.GEO , Coordinate.ECLIPTIC);

    ZodiacSign zodiacSign = ZodiacSign.getZodiacSign(moonPos.getLng());

    Branch bodyHouse = zodiacSign.getBranch();

    return Tuple.tuple(mainHouse , bodyHouse);
  }
}
