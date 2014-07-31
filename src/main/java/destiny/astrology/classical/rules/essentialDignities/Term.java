/**
 * @author smallufo 
 * Created on 2007/12/30 at 上午 12:26:46
 */ 
package destiny.astrology.classical.rules.essentialDignities;

import destiny.astrology.HoroscopeContext;
import destiny.astrology.Planet;
import destiny.utils.Tuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A planet in itw own term. */
public final class Term extends Rule
{
  public Term()
  {
  }

  @Nullable
  @Override
  protected Tuple<String, Object[]> getResult(Planet planet, @NotNull HoroscopeContext horoscopeContext)
  {
    if (planet == essentialImpl.getTermsPoint(horoscopeContext.getPosition(planet).getLongitude()))
    {
      return new Tuple<>("comment" , new Object[]{planet , horoscopeContext.getPosition(planet).getLongitude()});
    }
    return null;
  }
}
