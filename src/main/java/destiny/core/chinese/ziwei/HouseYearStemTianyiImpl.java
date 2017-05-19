/**
 * Created by smallufo on 2017-04-15.
 */
package destiny.core.chinese.ziwei;

import destiny.core.Gender;
import destiny.core.calendar.SolarTerms;
import destiny.core.chinese.Branch;
import destiny.core.chinese.Stem;
import destiny.core.chinese.StemBranch;
import destiny.core.chinese.TianyiIF;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

/**
 * (年干,天乙貴人設定) -> 地支
 * 適用於 {@link StarLucky#天魁} (陽貴人) , {@link StarLucky#天鉞} (陰貴人)
 * 2017-05-20 新增 {@link ZContext.YearType} 的判斷
 */
public abstract class HouseYearStemTianyiImpl extends HouseAbstractImpl<Tuple2<Stem, TianyiIF>> {

  HouseYearStemTianyiImpl(ZStar star) {
    super(star);
  }

  @Override
  public Branch getBranch(StemBranch lunarYear, StemBranch solarYear, Branch monthBranch, int monthNum, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, boolean leap, int prevMonthDays, ZContext context) {
    Stem yearStem = context.getYearType() == ZContext.YearType.YEAR_LUNAR ? lunarYear.getStem() : solarYear.getStem();
    return getBranch(Tuple.tuple(yearStem, context.getTianyiImpl()));
  }
}
