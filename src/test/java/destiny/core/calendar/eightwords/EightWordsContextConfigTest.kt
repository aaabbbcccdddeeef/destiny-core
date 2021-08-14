/**
 * Created by smallufo on 2021-08-14.
 */
package destiny.core.calendar.eightwords

import destiny.core.AbstractConfigTest
import destiny.core.astrology.*
import destiny.core.calendar.eightwords.EightWordsContextConfigBuilder.Companion.ewContext
import kotlinx.serialization.KSerializer
import kotlin.test.assertTrue

internal class EightWordsContextConfigTest : AbstractConfigTest<EightWordsContextConfig>() {
  override val serializer: KSerializer<EightWordsContextConfig> = EightWordsContextConfig.serializer()

  override val configByConstructor: EightWordsContextConfig = EightWordsContextConfig(
    EightWordsConfig(
      YearMonthConfig(YearConfig(270.0), true , HemisphereBy.DECLINATION ,YearMonthConfig.MoonImpl.SunSign),
      DayHourConfig(
        DayConfig(changeDayAfterZi = false , DayConfig.MidnightImpl.CLOCK0),
        DayHourConfig.HourImpl.LMT
      )
    ),
    RisingSignConfig(
      HouseConfig(HouseSystem.EQUAL , Coordinate.SIDEREAL),
      TradChineseRisingSignConfig(DayHourConfig.HourImpl.LMT),
      RisingSignConfig.Impl.TradChinese
    ),
    ZodiacSignConfig(Planet.SUN),
    HouseConfig(HouseSystem.EQUAL , Coordinate.SIDEREAL),
    "台北市"
  )

  override val configByFunction: EightWordsContextConfig = ewContext {
    ewConfig {
      yearMonth {
        year {
          changeYearDegree = 270.0
        }
        southernHemisphereOpposition = true
        hemisphereBy = HemisphereBy.DECLINATION
        monthImpl = YearMonthConfig.MoonImpl.SunSign
      }

      dayHour {
        day {
          changeDayAfterZi = false
          midnight = DayConfig.MidnightImpl.CLOCK0
        }
        hourImpl = DayHourConfig.HourImpl.LMT
      }
    }
    risingSign {
      houseCusp {
        houseSystem = HouseSystem.EQUAL
        coordinate = Coordinate.SIDEREAL
      }
      tradChinese {
        hourImpl = DayHourConfig.HourImpl.LMT
      }
    }
    zodiacSign {
      star = Planet.SUN
    }
    house {
      houseSystem = HouseSystem.EQUAL
      coordinate = Coordinate.SIDEREAL
    }
    place = "台北市"
  }

  override val assertion = { raw: String ->
    logger.info { raw }
    assertTrue(raw.contains(""""changeYearDegree":\s*270.0""".toRegex()))
    assertTrue(raw.contains(""""southernHemisphereOpposition":\s*true""".toRegex()))
    assertTrue(raw.contains(""""hemisphereBy":\s*"DECLINATION""".toRegex()))
    assertTrue(raw.contains(""""moonImpl":\s*"SunSign""".toRegex()))

    assertTrue(raw.contains(""""changeDayAfterZi":\s*false""".toRegex()))
    assertTrue(raw.contains(""""midnight":\s*"CLOCK0""".toRegex()))
    assertTrue(raw.contains(""""hourImpl":\s*"LMT""".toRegex()))

    assertTrue(raw.contains(""""tradChineseRisingSignConfig""".toRegex()))

    assertTrue(raw.contains(""""place":\s*"台北市""".toRegex()))
  }
}
