/**
 * Created by smallufo on 2017-04-13.
 */
package destiny.core.chinese.ziwei;

import destiny.core.Gender;
import destiny.core.calendar.SolarTerms;
import destiny.core.chinese.Branch;
import destiny.core.chinese.StemBranch;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import static destiny.core.chinese.ziwei.FuncType.SET_DAY_NUM;

/**
 * 14 顆主星
 * (局數,生日)
 */
public abstract class HouseMainStarImpl extends HouseAbstractImpl<Tuple2<Integer, Integer>> {

  protected HouseMainStarImpl(ZStar star) {
    super(star);
  }

  @Override
  public FuncType getFuncType() {
    return SET_DAY_NUM;
  }

  @Override
  public Branch getBranch(StemBranch year, Branch monthBranch, int monthNum, SolarTerms solarTerms, int days, Branch hour, int set, Gender gender, Settings settings) {
    return getBranch(Tuple.tuple(set , days));
  }
}
