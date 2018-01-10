/**
 * @author smallufo
 * Created on 2002/8/13 at 下午 01:08:17
 */
package destiny.iching;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import destiny.core.chinese.FiveElement;
import destiny.core.chinese.IFiveElement;
import destiny.core.chinese.IYinYang;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * 八卦基本符號以及其資料
 */
public enum Symbol implements Serializable , ISymbol, IFiveElement {
  乾('乾', new boolean[] {true  , true  , true  } ),
  兌('兌', new boolean[] {true  , true  , false } ),
  離('離', new boolean[] {true  , false , true  } ),
  震('震', new boolean[] {true  , false , false } ),
  巽('巽', new boolean[] {false , true  , true  } ),
  坎('坎', new boolean[] {false , true  , false } ),
  艮('艮', new boolean[] {false , false , true  } ),
  坤('坤', new boolean[] {false , false , false } );
  
  private char name;
  private boolean[] yinYangs = new boolean[3];
  
  private static final Symbol[] symbolArray = {乾 , 兌 , 離 , 震 , 巽 , 坎 , 艮 , 坤};

  /** 先天八卦 -> 後天八卦 bi-mapping */
  private final static BiMap<Symbol, Symbol> con2AcqMap =  new ImmutableBiMap.Builder<Symbol, Symbol>()
      .put(乾 , 離)
      .put(兌 , 巽)
      .put(離 , 震)
      .put(震 , 艮)
      .put(巽 , 坤)
      .put(坎 , 兌)
      .put(艮 , 乾)
      .put(坤 , 坎)
      .build();
  
  Symbol(char name, boolean[] yinYangs)
  {
    this.name = name;
    this.yinYangs = yinYangs;
  }
  
  /**
   * 取得八個卦
   */
  @NotNull
  public static Symbol[] getSymbols()
  {
    return symbolArray;
  }
  
  @NotNull
  public String toString()
  {
    return String.valueOf(name);
  }
  
  /**
   * 實作 SymbolIF
   * 取得八卦名
   */
  public char getName()
  {
    return name;
  }

  /**
   * 實作 SymbolIF
   * 取得一個卦的第幾爻
   * index 值為 1,2,或3
   */
  @Override
  public boolean getBooleanValue(int index) {
    return yinYangs[index-1];
  }


  /**
   * 「由下而上」 三個陰陽 , 查詢卦象為何
   */
  @NotNull
  private static Symbol getSymbol(IYinYang[] line)
  {
    for (Symbol aSymbolArray : symbolArray) {
      if ((line[0].getBooleanValue() == aSymbolArray.yinYangs[0]) &&
        (line[1].getBooleanValue() == aSymbolArray.yinYangs[1]) &&
        (line[2].getBooleanValue() == aSymbolArray.yinYangs[2])) {
        return aSymbolArray;
      }
    }
    throw new RuntimeException("Cannot find Symbol for " + line[0] + " , " + line[1] + " , " + line[2]);
  }
  
  /**
   * 「由下而上」 三個陰陽 , 查詢卦象為何
   */
  @NotNull
  public static Symbol getSymbol(boolean... lines)
  {
    for (Symbol aSymbolArray : symbolArray) {
      if ((lines[0] == aSymbolArray.yinYangs[0]) &&
        (lines[1] == aSymbolArray.yinYangs[1]) &&
        (lines[2] == aSymbolArray.yinYangs[2])) {
        return aSymbolArray;
      }
    }
    throw new RuntimeException("Cannot find Symbol for " + lines[0] + " , " + lines[1] + " , " + lines[2]);
  }

  /**
   * 由 三個陰陽 , 查詢卦象為何 , 比較標準的命名方式
   */
  @NotNull
  public static Symbol valueOf(IYinYang[] yinYangs)
  {
    return getSymbol(yinYangs);
  }
  
  /**
   * implements FiveElementIF 
   */
  @NotNull
  public FiveElement getFiveElement()
  {
    switch ( this )
    {
      case 乾 : case 兌 : return FiveElement.金 ;
      case 離 :          return FiveElement.火 ;
      case 震 : case 巽 : return FiveElement.木 ;
      case 坎 :          return FiveElement.水 ;
      case 艮 : case 坤 : return FiveElement.土 ;
      default   : throw new RuntimeException("impossible");
    }
  }
  /** 先天八卦 -> 後天八卦 */
  public Symbol toAcquired() {
    return con2AcqMap.get(this);
  }

  /** 後天八卦 -> 先天八卦 */
  public Symbol toCongential() {
    return con2AcqMap.inverse().get(this);
  }
}
