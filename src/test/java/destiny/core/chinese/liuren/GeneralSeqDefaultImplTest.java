/**
 * Created by smallufo on 2015-05-27.
 */
package destiny.core.chinese.liuren;

import org.junit.Test;

import static destiny.core.chinese.Branch.*;
import static destiny.core.chinese.liuren.General.*;
import static org.junit.Assert.assertSame;

public class GeneralSeqDefaultImplTest {

  private GeneralSeqIF seq = new GeneralSeqDefaultImpl();

  @Test
  public void testGet() {
    assertSame(貴人 , get(丑));
    assertSame(螣蛇 , get(巳));
    assertSame(朱雀 , get(午));
    assertSame(六合 , get(卯));
    assertSame(勾陳 , get(辰));
    assertSame(青龍 , get(寅));
    assertSame(天空 , get(戌));
    assertSame(白虎 , get(申));
    assertSame(太常 , get(未));
    assertSame(玄武 , get(亥));
    assertSame(太陰 , get(酉));
    assertSame(天后 , get(子));
  }

  @Test
  public void testNext() {
    assertSame(貴人, 貴人.next(-12, seq));
    assertSame(螣蛇, 貴人.next(-11, seq));
    assertSame(太陰, 貴人.next(-2, seq));
    assertSame(天后, 貴人.next(-1, seq));
    assertSame(貴人, 貴人.next(0, seq));
    assertSame(螣蛇, 貴人.next(1, seq));
    assertSame(朱雀, 貴人.next(2, seq));
    assertSame(天后, 貴人.next(11, seq));
    assertSame(貴人, 貴人.next(12, seq));
  }

  @Test
  public void testPrev() {
    assertSame(貴人 , 貴人.prev(-12 , seq));
    assertSame(天后 , 貴人.prev(-11 , seq));
    assertSame(朱雀 , 貴人.prev(-2 , seq));
    assertSame(螣蛇 , 貴人.prev(-1 , seq));
    assertSame(貴人 , 貴人.prev(0 , seq));
    assertSame(天后 , 貴人.prev(1 , seq));
    assertSame(太陰 , 貴人.prev(2 , seq));
    assertSame(螣蛇 , 貴人.prev(11 , seq));
    assertSame(貴人 , 貴人.prev(12 , seq));
  }
}