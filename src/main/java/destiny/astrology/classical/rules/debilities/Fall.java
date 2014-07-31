/**
 * @author smallufo 
 * Created on 2007/12/30 at 上午 4:51:31
 */ 
package destiny.astrology.classical.rules.debilities;

import destiny.astrology.HoroscopeContext;
import destiny.astrology.Planet;
import destiny.astrology.ZodiacSign;
import destiny.astrology.classical.Dignity;
import destiny.utils.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** In Fall. */
public final class Fall extends EssentialRule
{
  public Fall()
  {
  }

  @Nullable
  @Override
  protected Tuple<String, Object[]> getResult(Planet planet, @NotNull HoroscopeContext horoscopeContext)
  {
    //取得此 Planet 在什麼星座
    ZodiacSign sign = horoscopeContext.getZodiacSign(planet);
    
    if (planet == essentialImpl.getPoint(sign, Dignity.FALL))
    {
      //addComment(Locale.TAIWAN , planet + " 位於 " + sign + " , 為其 Fall");
      return new Tuple<>("comment" , new Object[]{planet , sign});
    }
    return null;
  }

}
