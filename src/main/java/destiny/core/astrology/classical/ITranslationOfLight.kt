/**
 * Created by smallufo on 2014-07-15.
 */
package destiny.core.astrology.classical

import destiny.core.astrology.IAspectData
import destiny.core.astrology.IHoroscopeModel
import destiny.core.astrology.Planet

interface ITranslationOfLight {

  /**
   * 此星盤中 , planet 是否有傳遞光線 , 若有的話 , 從哪顆星 (triple.first) 傳遞到 哪顆星 (triple.second) , 以及 , 這兩顆星是否有形成什麼交角 (可能為null)
   */
  fun getResult(planet: Planet, h: IHoroscopeModel): Triple<Planet, Planet, IAspectData.Type?>?
}
