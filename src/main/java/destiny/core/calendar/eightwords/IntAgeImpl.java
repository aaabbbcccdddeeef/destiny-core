/**
 * Created by smallufo on 2017-10-20.
 */
package destiny.core.calendar.eightwords;

import destiny.core.Gender;
import destiny.core.IntAge;
import destiny.core.calendar.Location;
import destiny.core.calendar.SolarTermsIF;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import static destiny.core.calendar.SolarTerms.立春;

/**
 * 八字的「虛歲」大運
 * 出生當下，即為一歲。（故， age 不可以 <= 0）
 * 「一歲」終止於「順推」的立春之時
 */
public class IntAgeImpl implements IntAge, Serializable {

  private final SolarTermsIF solarTermsImpl;

  private Logger logger = LoggerFactory.getLogger(getClass());

  public IntAgeImpl(SolarTermsIF solarTermsImpl) {this.solarTermsImpl = solarTermsImpl;}

  @Override
  public Tuple2<Double, Double> getRange(Gender gender, double gmtJulDay, Location loc, int age) {
    Tuple2<Double , Double> age1 = Tuple.tuple(gmtJulDay , solarTermsImpl.getSolarTermsTime(立春 , gmtJulDay , true));

    return getRangeInner(age1 , age);
  }

  private Tuple2<Double , Double> getRangeInner(Tuple2<Double , Double> prevResult , int count) {
    if (count == 1) {
      return prevResult;
    } else {
      double stepDay = prevResult.v2()+1; // 臨時的日子，以此日子，分別往前、後 推算立春日期
      double start = solarTermsImpl.getSolarTermsTime(立春 , stepDay, false);
      double end = solarTermsImpl.getSolarTermsTime(立春 , stepDay , true);
      return getRangeInner(Tuple.tuple(start , end) , count -1);
    }
  }
}