/** 2009/11/26 上午10:38:15 by smallufo */
package destiny.utils.location;

import destiny.utils.Tuple;

import java.io.IOException;
import java.util.Optional;

/** 從地名尋找經緯度 */
public interface GeocodingIF
{
  public Optional<Tuple<Double , Double>> getLongLat(String placeName) throws IOException;
}

