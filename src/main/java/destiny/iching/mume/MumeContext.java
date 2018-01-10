/**
 * @author smallufo 
 * Created on 2007/2/10 at 上午 8:42:33
 */
package destiny.iching.mume;

import destiny.iching.Hexagram;
import destiny.iching.IHexagram;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 梅花易 , 梅花的學名為 Prunus(李屬) mume(梅種)
 */
public class MumeContext implements Serializable
{
  private Hexagram hexagram;
  private int        motivate;     // 動爻

  /**
   * @param hexagram
   *          本卦
   * @param motivate
   *          動爻
   */
  public MumeContext(@NotNull IHexagram hexagram, int motivate)
  {
    this.hexagram = Hexagram.Companion.getHexagram(hexagram.getYinYangs());
    this.motivate = motivate;
  }

  /**
   * @return 變卦
   */
  @NotNull
  public IHexagram getTargetHexagram()
  {
    boolean[] yinyangs = new boolean[6];
    for (int i = 1; i <= 6; i++)
      yinyangs[i - 1]  = hexagram.getLine(i);

    yinyangs[motivate - 1] = !hexagram.getLine(motivate);
    return Hexagram.Companion.getHexagram(yinyangs);
  }

  public Hexagram getHexagram()
  {
    return hexagram;
  }

  /** 設定本卦 */
  public void setHexagram(@NotNull IHexagram hexagram)
  {
    this.hexagram = Hexagram.Companion.getHexagram(hexagram.getYinYangs());
  }

  /** 動爻 , 1~6 */
  public int getMotivate()
  {
    return motivate;
  }

  /** 1-based */
  public void setMotivate(int motivate)
  {
    this.motivate = motivate;
  }

}
