/**
 * Created by smallufo on 2018-02-03.
 */
package destiny.core.iching.divine

import destiny.core.chinese.StemBranch
import destiny.core.iching.Hexagram.*
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.assertSame

class HiddenEnergyWangImplTest {

  val impl = HiddenEnergyWangImpl()

  @Test
  fun `1 乾為天 六爻都沒有伏神`() {
    impl.run {
      assertNull(getStemBranch(乾, SettingsGingFang(), 6))
      assertNull(getStemBranch(乾, SettingsGingFang(), 5))
      assertNull(getStemBranch(乾, SettingsGingFang(), 4))
      assertNull(getStemBranch(乾, SettingsGingFang(), 3))
      assertNull(getStemBranch(乾, SettingsGingFang(), 2))
      assertNull(getStemBranch(乾, SettingsGingFang(), 1))
    }
  }

  @Test
  fun `2 天風姤 二爻伏妻財甲寅`() {
    impl.run {
      assertNull(getStemBranch(姤, SettingsGingFang(), 6))
      assertNull(getStemBranch(姤, SettingsGingFang(), 5))
      assertNull(getStemBranch(姤, SettingsGingFang(), 4))
      assertNull(getStemBranch(姤, SettingsGingFang(), 3))
      assertSame(StemBranch.甲寅, getStemBranch(姤, SettingsGingFang(), 2))
      assertNull(getStemBranch(姤, SettingsGingFang(), 1))
    }
  }

  @Test
  fun `3 天山遯 初爻二爻伏神`() {
    impl.run {
      assertNull(getStemBranch(遯, SettingsGingFang(), 6))
      assertNull(getStemBranch(遯, SettingsGingFang(), 5))
      assertNull(getStemBranch(遯, SettingsGingFang(), 4))
      assertNull(getStemBranch(遯, SettingsGingFang(), 3))
      assertSame(StemBranch.甲寅, getStemBranch(遯, SettingsGingFang(), 2))
      assertSame(StemBranch.甲子, getStemBranch(遯, SettingsGingFang(), 1))
    }
  }

  @Test
  fun `4 天地否 初爻伏甲子`() {
    impl.run {
      assertNull(getStemBranch(否, SettingsGingFang(), 6))
      assertNull(getStemBranch(否, SettingsGingFang(), 5))
      assertNull(getStemBranch(否, SettingsGingFang(), 4))
      assertNull(getStemBranch(否, SettingsGingFang(), 3))
      assertNull(getStemBranch(否, SettingsGingFang(), 2))
      assertSame(StemBranch.甲子, getStemBranch(否, SettingsGingFang(), 1))
    }
  }

  @Test
  fun `5 風地觀 初爻伏甲子 五爻伏壬申`() {
    impl.run {
      assertNull(getStemBranch(觀, SettingsGingFang(), 6))
      assertSame(StemBranch.壬申, getStemBranch(觀, SettingsGingFang(), 5))
      assertNull(getStemBranch(觀, SettingsGingFang(), 4))
      assertNull(getStemBranch(觀, SettingsGingFang(), 3))
      assertNull(getStemBranch(觀, SettingsGingFang(), 2))
      assertSame(StemBranch.甲子, getStemBranch(觀, SettingsGingFang(), 1))
    }

  }

  @Test
  fun `6 山地剝 五爻伏壬申`() {
    impl.run {
      assertNull(getStemBranch(剝, SettingsGingFang(), 6))
      assertSame(StemBranch.壬申, getStemBranch(剝, SettingsGingFang(), 5))
      assertNull(getStemBranch(剝, SettingsGingFang(), 4))
      assertNull(getStemBranch(剝, SettingsGingFang(), 3))
      assertNull(getStemBranch(剝, SettingsGingFang(), 2))
      assertNull(getStemBranch(剝, SettingsGingFang(), 1))
    }

  }

  @Test
  fun `7 火地晉 初爻伏甲子`() {
    impl.run {
      assertNull(getStemBranch(晉, SettingsGingFang(), 6))
      assertNull(getStemBranch(晉, SettingsGingFang(), 5))
      assertNull(getStemBranch(晉, SettingsGingFang(), 4))
      assertNull(getStemBranch(晉, SettingsGingFang(), 3))
      assertNull(getStemBranch(晉, SettingsGingFang(), 2))
      assertSame(StemBranch.甲子, getStemBranch(晉, SettingsGingFang(), 1))
    }
  }

  @Test
  fun `8 火天大有 六爻都沒有伏神`() {
    impl.run {
      assertNull(getStemBranch(大有, SettingsGingFang(), 6))
      assertNull(getStemBranch(大有, SettingsGingFang(), 5))
      assertNull(getStemBranch(大有, SettingsGingFang(), 4))
      assertNull(getStemBranch(大有, SettingsGingFang(), 3))
      assertNull(getStemBranch(大有, SettingsGingFang(), 2))
      assertNull(getStemBranch(大有, SettingsGingFang(), 1))
    }
  }
}
