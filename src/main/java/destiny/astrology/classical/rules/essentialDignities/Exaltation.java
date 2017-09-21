/**
 * @author smallufo
 * Created on 2007/12/29 at 上午 4:29:49
 */
package destiny.astrology.classical.rules.essentialDignities;

import destiny.astrology.*;
import destiny.astrology.classical.Dignity;
import destiny.astrology.classical.EssentialUtils;
import org.jetbrains.annotations.NotNull;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/** A planet in its exaltation , or mutual reception with another planet by exaltation */
public final class Exaltation extends Rule {
  private final DayNightDifferentiator dayNightDifferentiatorImpl;
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Exaltation(DayNightDifferentiator dayNightDifferentiatorImpl) {
    this.dayNightDifferentiatorImpl = dayNightDifferentiatorImpl;
  }

  @Override
  public Optional<Tuple2<String, Object[]>> getResult(Planet planet, @NotNull Horoscope h) {

    //取得此 Planet 在什麼星座
    return h.getZodiacSign(planet).flatMap(sign -> {
      //Exaltation (廟)
      if (planet == essentialImpl.getPoint(sign, Dignity.EXALTATION)) {
        //addComment(Locale.TAIWAN , planet + " 位於其 Exaltation 的星座 " + sign);
        return Optional.of(Tuple.tuple("commentBasic", new Object[]{planet, sign}));
      }
      else {
        // Exaltation 互容 , mutual reception
        return exaltMutualReception(h , planet);
      }
    });
  }


  /**
   * 廟廟互容
   * EXALTATION / EXALTATION 互容
   */
  private Optional<Tuple2<String, Object[]>> exaltMutualReception(Horoscope h , Planet planet) {
    return h.getZodiacSign(planet).flatMap(sign1 -> {
      // planet 在 sign1 , 計算 sign1 的 Exaltation :
      Point signExaltation = essentialImpl.getPoint(sign1, Dignity.EXALTATION);
      if (signExaltation != null) {
        EssentialUtils utils = new EssentialUtils(dayNightDifferentiatorImpl);
        utils.setEssentialImpl(essentialImpl);

        return h.getZodiacSign(signExaltation).flatMap(sign2 -> {
          Point planet2 = essentialImpl.getPoint(sign2, Dignity.EXALTATION);
          if (planet == planet2) {
            //已確定 Exaltation 互容，要排除互陷
            //只要兩顆星都不是陷落，就算互容。其中一顆星陷落無妨
            if (!utils.isBothInBadSituation(planet , sign1 , signExaltation , sign2)) {
              //addComment(Locale.TAIWAN , planet + " 位於 " + sign1 + " , 與其 Exaltation " + signExaltation + " 飛至 " + sign2 + " , 形成互容");
              return Optional.of(Tuple.tuple("commentReception" , new Object[]{planet , sign1 , signExaltation , sign2}));
            }
          }
          return Optional.empty();
        });
      }
      return Optional.empty();
    });
  }

}
