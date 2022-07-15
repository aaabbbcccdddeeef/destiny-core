/**
 * Created by smallufo on 2022-07-13.
 */
package destiny.core.chinese.eightwords.hazards

import destiny.core.Gender
import destiny.core.calendar.eightwords.IEightWords
import destiny.core.chinese.Branch.*
import destiny.core.chinese.BranchTools
import destiny.core.chinese.FiveElement.*
import destiny.core.chinese.SimpleBranch
import destiny.core.chinese.Stem.*
import destiny.core.chinese.eightwords.hazards.Book.*
import destiny.core.chinese.eightwords.hazards.ChildHazard.*

private val 子午卯酉 = setOf(子, 午, 卯, 酉)
private val 寅申巳亥 = setOf(寅, 申, 巳, 亥)
private val 辰戌丑未 = setOf(辰, 戌, 丑, 未)

private val 寅卯辰 = setOf(寅, 卯, 辰)
private val 巳午未 = setOf(巳, 午, 未)
private val 申酉戌 = setOf(申, 酉, 戌)
private val 亥子丑 = setOf(亥, 子, 丑)

private val 巳酉丑 = setOf(巳, 酉, 丑)
private val 申子辰 = setOf(申, 子, 辰)
private val 亥卯未 = setOf(亥, 卯, 未)
private val 寅午戌 = setOf(寅, 午, 戌)


interface IHazardFactory {

  fun getBooks(): Set<Book> = emptySet()

  fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard?
}


/**
 * 寅申巳亥月：辰戌丑未時。
 * 子午卯酉月：寅申巳亥時。
 * 辰戌丑未月：子午卯酉時。
 * 俗忌百曰不出大門，房門不忌。夫百曰關者，專以十二生肖月忌各所內百犯之，童限月內百曰必有星辰難養。(象吉)
 *
 * 寅申巳亥月生辰戌丑未時是。
 * 子午卯酉月生寅申巳亥時是。
 * 辰戌丑未月生子午卯酉時是。
 * 俗忌百日不出門，止忌大門、房門不忌無妨。夫百日關者，專以十二生肖月忌各所值時犯之，童限月內百日必有星辰難養。又云：忌過週歲，又云：止忌百日、之外則無妨。(鰲頭)
 *
 * 寅申巳亥月忌辰戌丑未時。
 * 辰戌丑未月忌子午卯酉時，
 * 子午卯酉月忌寅申巳亥時。
 * 生孩百日內勿出大門前。(星平會海)
 *
 * 寅申巳亥月：辰戌丑未時。
 * 子午卯酉月：寅申巳亥時。
 * 辰戌丑未月：子午卯酉時。
 * 童限犯之初生百日內、勿出大門外。凡正月寅巳時生人，犯此百日內忌出入門前。(生育禮俗)
 *
 * 凡
 * 正、四、七、十月逢辰、戌、丑、未時，
 * 二、五、八、十一月逢寅、申、巳、亥時，
 * 三、六、九、十二月逢子、午、卯、酉時生人是。
 * 犯此百日內忌出入門前。(黃曆解秘)
 */
val p百日關 = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗, 黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅申巳亥.contains(eightWords.month.branch) && 辰戌丑未.contains(eightWords.hour.branch)) ||
      (子午卯酉.contains(eightWords.month.branch) && 寅申巳亥.contains(eightWords.hour.branch)) ||
      (辰戌丑未.contains(eightWords.month.branch) && 子午卯酉.contains(eightWords.hour.branch))
    ) {
      百日關
    } else {
      null
    }
  }
}

/**
 * 甲乙馬頭龍不住，
 * 丙丁雞猴奔山崗，
 * 戊己逢藏蛇在草，
 * 庚辛遇虎於林下，
 * 壬癸丑亥時須忌，
 * 孩兒直此有煩惱。
 * 夫千曰關者，如甲乙生人午時是也，余仿此。犯之主有驚風、吐乳之災，忌住難星。（象吉）
 *
 * 甲乙馬龍頭、
 * 丙丁猴雞山、
 * 庚辛虎林下、
 * 戊己蛇藏草、
 * 壬癸丑亥時。
 * 未滿千日勿往外媽厝，主驚風、吐乳，制化即安。凡午年寅巳亥時生人，犯上忌三歲上高落低之患。（生育禮俗）
 */
val p千日關A = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 乙).contains(eightWords.year.stem) && setOf(午).contains(eightWords.hour.branch)) ||
      (setOf(丙, 丁).contains(eightWords.year.stem) && setOf(酉, 申).contains(eightWords.hour.branch)) ||
      (setOf(戊, 己).contains(eightWords.year.stem) && setOf(巳).contains(eightWords.hour.branch)) ||
      (setOf(庚, 辛).contains(eightWords.year.stem) && setOf(寅).contains(eightWords.hour.branch)) ||
      (setOf(壬, 癸).contains(eightWords.year.stem) && setOf(丑, 亥).contains(eightWords.hour.branch))
    ) {
      千日關
    } else {
      null
    }
  }
}

/**
 * 甲乙馬頭龍不住、
 * 丙丁雞叫奔山崗、
 * 戊己逢蛇藏在草、
 * 庚辛遇虎竹林下、
 * 壬癸丑亥時須忌。
 * 孩兒值此有吁嗟。夫千日關者，且如甲乙生人忌午時是、餘仿此，犯之乃驚風、吐乳。（鰲頭）
 *
 * 甲乙馬頭籠不住、
 * 丙丁雞叫奔山岡、
 * 戊己逢蛇在草藏、
 * 庚辛遇虎於林下、
 * 壬癸丑亥時須忌。
 * 孩兒值此有嗟。過千日之外則不妨。（星平會海）
 */
val p千日關B = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(鰲頭通書, 星平會海)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 乙).contains(eightWords.year.stem) && setOf(午).contains(eightWords.hour.branch)) ||
      (setOf(丙, 丁).contains(eightWords.year.stem) && setOf(酉).contains(eightWords.hour.branch)) ||
      (setOf(戊, 己).contains(eightWords.year.stem) && setOf(巳).contains(eightWords.hour.branch)) ||
      (setOf(庚, 辛).contains(eightWords.year.stem) && setOf(寅).contains(eightWords.hour.branch)) ||
      (setOf(壬, 癸).contains(eightWords.year.stem) && setOf(丑, 亥).contains(eightWords.hour.branch))
    ) {
      千日關
    } else {
      null
    }
  }
}

/**
 * 凡午年寅、申、巳、亥時生人，犯此忌三歲上高落低之患。
 * 另一說法；三歲之前不宜到外婆家，或莫至外婆供奉祖先牌位處。（黃曆解秘）
 */
val p千日關C = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (eightWords.year.branch == 午 && 寅申巳亥.contains(eightWords.hour.branch)) {
      千日關
    } else {
      null
    }
  }
}


/**
 * 春忌牛羊水上波，夏逢辰戌見閻羅，秋逢子午君須避，冬時生人虎兔時，
 * 甲乙丙丁申子辰，戊己庚生亥卯未，辛兼壬癸寅午戌，生孩切慮不成人。日主旺不防，弱則難養。（象吉）
 *
 * 春季牛羊水上波，夏逢辰戌見閻羅，秋遇子午君須避，冬季生人虎兔嗟。
 * 甲乙丙丁申子辰，戊己庚辛亥卯未，辛兼壬癸寅午戌，生孩切慮不成人。日主生人不旺，弱則難養。（鰲頭）
 *
 * 春忌牛羊水上波，夏逢辰戌見閻羅，秋逢子午當須避，冬季生人虎兔磨。日主旺無妨。（星平會海）
 *
 * 春忌牛羊水上波，
 * 夏逢辰戌見閻王，
 * 秋怕子午當迴避，
 * 冬季生人虎兔磨。
 * 勿看功果做佛事，日主弱宜制化。凡七、八、九、十二月子午寅卯時生人，犯此帶天德、月德可解。（生育禮俗）
 */
val p閻王關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && setOf(丑, 未).contains(eightWords.hour.branch)) ||
      (巳午未.contains(eightWords.month.branch) && setOf(辰, 戌).contains(eightWords.hour.branch)) ||
      (申酉戌.contains(eightWords.month.branch) && setOf(子, 午).contains(eightWords.hour.branch)) ||
      (亥子丑.contains(eightWords.month.branch) && setOf(寅, 卯).contains(eightWords.hour.branch))
    ) {
      閻王關
    } else {
      null
    }
  }
}

/**
 * 凡七、八、九、十、十二月子、午、寅、卯時生人。
 * 另正、二、三月丑、未時，
 * 四、五、六月辰、戌時，
 * 七、八、九月子、午時，
 * 十、十一、十二月寅、卯時生人是，
 * 犯此小時應該避免看誦經作法或作功德場合，難養，帶天德、月德可解。（黃曆解秘）
 */
val p閻王關B = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(午, 未, 申, 酉, 亥).contains(eightWords.month.branch) && setOf(子, 午, 寅, 卯).contains(eightWords.hour.branch)) ||
      (寅卯辰.contains(eightWords.month.branch) && setOf(丑, 未).contains(eightWords.hour.branch)) ||
      (巳午未.contains(eightWords.month.branch) && setOf(辰, 戌).contains(eightWords.hour.branch)) ||
      (申酉戌.contains(eightWords.month.branch) && setOf(子, 午).contains(eightWords.hour.branch)) ||
      (亥子丑.contains(eightWords.month.branch) && setOf(寅, 卯).contains(eightWords.hour.branch))
    ) {
      閻王關
    } else {
      null
    }
  }
}

/**
 * 子丑寅生人、酉午未時真。
 * 卯辰巳生人、申戌亥為刑。
 * 午未申生人、莫犯丑寅卯。
 * 酉戌亥生人、子辰巳難乎。
 * 夫鬼門關者，以十二支生人、逢各所值時辰、論小兒時上、並童限逢之不可遠行。 (象吉)
 *
 * 子丑寅生人，巳午未時嗔，
 * 卯辰巳生人，申亥戌為刑，
 * 午未申生命，莫犯丑寅卯，
 * 酉戌亥生命，子巳辰時嗔。
 * 支生取時嗔，限忌遠行。（鰲頭）
 *
 */
val p鬼門關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(子, 丑, 寅).contains(eightWords.year.branch) && setOf(酉, 午, 未).contains(eightWords.hour.branch)) ||
      (setOf(卯, 辰, 巳).contains(eightWords.year.branch) && setOf(申, 戌, 亥).contains(eightWords.hour.branch)) ||
      (setOf(午, 未, 申).contains(eightWords.year.branch) && setOf(丑, 寅, 卯).contains(eightWords.hour.branch)) ||
      (setOf(酉, 戌, 亥).contains(eightWords.year.branch) && setOf(子, 辰, 巳).contains(eightWords.hour.branch))
    ) {
      鬼門關
    } else {
      null
    }
  }
}

/**
 * 子嫌酉上午嫌丑，
 * 寅未申卯不須安，
 * 亥怕辰宮戌怕巳，
 * 古賢立定鬼門關。
 * 人命生時莫犯此，
 * 六月炎天也遭寒。
 * 命限逢之、不可遠行。（星平會海）
 *
 * 子嫌酉年午嫌丑，
 * 寅未申卯不須安，
 * 亥怕辰兮戌怕巳，
 * 古賢立號鬼門關。
 * 不宜遠行、勿入陰廟宮寺。
 * 凡甲子丙子戊子生人，犯此忌夜出入門外。（生育禮俗）
 *
 *
 */
val p鬼門關B = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (setOf(
        Pair(子, 酉),
        Pair(午, 丑),
        Pair(亥, 辰),
        Pair(戌, 巳),
      ).contains(eightWords.year.branch to eightWords.hour.branch)
    ) {
      鬼門關
    } else {
      null
    }
  }
}

/**
 * 凡
 * 子年逢酉時生，
 * 丑年逢午時生，
 * 寅年逢未時生，
 * 卯年逢申時生，
 * 辰年逢亥時生，
 * 巳年逢戌時生，
 * 午年逢丑時生，
 * 未年逢寅時生，
 * 申年逢卯時生，
 * 酉年逢子時生，
 * 戌年逢巳時生，
 * 亥年逢辰時生是。
 * 一生不宜進陰廟、有應公、萬善祠、墳墓區及殯儀館等。宜燒地府錢給地府眾鬼神制化則吉。（黃曆解秘）
 */
val p鬼門關C = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (setOf(
        Pair(子, 酉),
        Pair(丑, 午),
        Pair(寅, 未),
        Pair(卯, 申),
        Pair(辰, 亥),
        Pair(巳, 戌),
        Pair(午, 丑),
        Pair(未, 寅),
        Pair(申, 卯),
        Pair(酉, 子),
        Pair(戌, 巳),
        Pair(亥, 辰),
      ).contains(eightWords.year.branch to eightWords.hour.branch)
    ) {
      鬼門關
    } else {
      null
    }
  }
}

/**
 * 甲己巳酉丑、孩兒難保守。庚辛亥卯未、父母哭斷腸。壬癸寅午戌、生下不見日。乙戊丙丁子、不過三朝死。此關童命犯之難養，童限遇之亦凶，以午明順。(鰲頭)
 * 甲己巳酉丑、孩兒難保守。庚辛亥卯年、爺娘哭斷腸。壬癸寅午戌、生下不見日。乙戊丙丁子、不過三朝死。(星平會海)
 */
val p雞飛關A = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(鰲頭通書, 星平會海)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 己).contains(eightWords.day.stem) && 巳酉丑.contains(eightWords.hour.branch)) ||
      (setOf(庚, 辛).contains(eightWords.day.stem) && 亥卯未.contains(eightWords.hour.branch)) ||
      (setOf(壬, 癸).contains(eightWords.day.stem) && 寅午戌.contains(eightWords.hour.branch)) ||
      (setOf(乙, 戊, 丙, 丁).contains(eightWords.day.stem) && eightWords.hour.branch == 子)
    ) {
      雞飛關
    } else {
      null
    }
  }
}

/**
 * 甲乙巳酉丑、孩兒難保守。庚辛亥卯未、父母哭斷腸。壬癸寅午戌、生下不見日。己戊丙丁子、不過三朝死。此關童命犯之難養，夜生不妨，限遇亦凶，以年干生人取用。(象吉)
 */
val p雞飛關B = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 乙).contains(eightWords.day.stem) && 巳酉丑.contains(eightWords.hour.branch)) ||
      (setOf(庚, 辛).contains(eightWords.day.stem) && 亥卯未.contains(eightWords.hour.branch)) ||
      (setOf(壬, 癸).contains(eightWords.day.stem) && 寅午戌.contains(eightWords.hour.branch)) ||
      (setOf(己, 戊, 丙, 丁).contains(eightWords.day.stem) && eightWords.hour.branch == 子)
    ) {
      雞飛關
    } else {
      null
    }
  }
}

/**
 * 甲己巳酉丑、庚辛亥卯未、壬庚午戌、乙戊丙丁子，不過三日關。勿看殺生，童限難養，夜生不妨。凡辰戌未時生人，犯此忌雞對面啼叫。(生育禮俗)
 */
val p雞飛關C = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 己).contains(eightWords.day.stem) && 巳酉丑.contains(eightWords.hour.branch)) ||
      (setOf(庚, 辛).contains(eightWords.day.stem) && 亥卯未.contains(eightWords.hour.branch)) ||
      (setOf(乙, 戊, 丙, 丁).contains(eightWords.day.stem) && eightWords.hour.branch == 子)
    ) {
      雞飛關
    } else {
      null
    }
  }
}

/**
 * 甲、己日逢巳、酉、丑時生，
 * 乙、丙、丁、戊日逢子時生，
 * 庚日逢亥、卯、未時生，
 * 辛、壬、癸日逢寅、午、戌時生是。
 * 避免孩子看殺雞、殺魚、殺鴨等行為，可燒牛頭馬面錢、或稱牛馬將軍錢，制化即吉。(黃曆解秘)
 */
val p雞飛關D = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(甲, 己).contains(eightWords.day.stem) && 巳酉丑.contains(eightWords.hour.branch)) ||
      (庚 == eightWords.day.stem && 亥卯未.contains(eightWords.hour.branch)) ||
      (setOf(辛, 壬, 癸).contains(eightWords.day.stem) && 寅午戌.contains(eightWords.hour.branch)) ||
      (setOf(乙, 丙, 丁, 戊).contains(eightWords.day.stem) && eightWords.hour.branch == 子)
    ) {
      雞飛關
    } else {
      null
    }
  }
}


/**
 * 金戌化成鐵，火向未申絕，木辰枝葉枯，水上丑寅滅。此關最凶，童命時上帶、童限更值之則難養。並忌見麻痘凶，壯命行限值之亦有災凶，重則倒壽、十有九驗，以命納音取用，如甲子生人戌時是。(象吉)
 * 金戌化成鐵，火向未申絕，木辰枝葉見，水土丑寅滅。此關最凶，童命時上帶、童限更值之則難養。並忌見麻痘凶，旺命行限值之亦有災，以命納音如甲子生戌時。(鰲頭)
 *
 * 金戌化為鐵，火向未申絕，木辰枝葉枯，水土丑寅滅。童限遇此最忌疹痘疾。此限老少皆忌。(星平會海)  !未考慮納音!
 * 金戌化為鐵，火向未申絕，木辰枝葉枯，水土丑寅滅。鐵蛇來浸害。出疹痘小心，出疹痘前宜制化。凡金木水火土生人，犯此忌痲痘之災。(生育禮俗) !未考慮納音!
 */
val p鐵蛇關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return eightWords.year.naYin.fiveElement.let { yearNaYinFiveElement ->
      when (yearNaYinFiveElement) {
        金    -> setOf(戌)
        火    -> setOf(未, 申)
        木    -> setOf(辰)
        水    -> setOf(丑, 寅)
        else -> emptySet()
      }.contains(eightWords.hour.branch).let {
        if (it) {
          鐵蛇關
        } else {
          null
        }
      }
    }
  }
}

/**
 * 生年屬金逢卯、戌時，
 * 生年屬木逢酉、辰時，
 * 生年屬火逢子、未、申時，
 * 生年屬水、土逢丑、寅、午時是。
 * 兒時出痲疹、疹痘須特別小心，也容易遭動物咬傷，若遭動物咬傷速就醫，回家用陰陽水燒化銅蛇鐵狗錢於其中則吉。(黃曆解秘)
 *
 * 生年屬金，逢卯、戌時；
 * 木年逢酉、辰時；
 * 火年逢子、申、未時；
 * 水年、土年逢午、丑、寅時生的人，才會犯到這個關煞。 (小兒關煞圖)
 * 這些人在兒童時期出疹痘、麻疹時要特別小心照料，否則身體上會留下許多像被狗或蛇咬過的疤痕，為了避免意外發生，最好還是找機會燒一些銅蛇鐵狗錢，制化那些不祥的蛇拘貓們。
 * 如果孩子已遭到這些動物咬傷，則表示這些動物已在償冤報，人們不得怨恨，追打那些動物，只得趁機燒一盆熱水，再倒入相當的冷水，成了冷（陰）熱（陽）調和的陰陽水，再燒化銅蛇鐵狗錢於其中，用這種陰陽水來洗傷口或塗抹傷口治療，很快就可以痊癒。
 */
val p鐵蛇關B = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘, 小兒關煞圖)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return SimpleBranch.getFiveElement(eightWords.year.branch).let { yearFiveElement ->
      when (yearFiveElement) {
        金 -> setOf(卯, 戌)
        木 -> setOf(酉, 辰)
        火 -> setOf(子, 未, 申)
        水 -> setOf(丑, 寅, 午)
        土 -> setOf(丑, 寅, 午)
      }.contains(eightWords.hour.branch).let {
        if (it) {
          鐵蛇關
        } else {
          null
        }
      }
    }
  }
}

/**
 * 正寅二兔三猴走，
 * 四月耕牛懶下田，
 * 五犬六雞門外立，
 * 七龍戲水八蛇纏，
 * 九馬十羊十一豬，
 * 冬季老鼠鬧喧喧。
 * 此關以月建遇十二時支論，乃指南中第一關，小兒生時帶著難養，
 * 壯命行限值之、加以刻度凶星到，必倒壽無有不驗。犯此忌過橋、汲水照影。宜用水官錢祭祀水官大帝。(象吉)、(鰲頭)
 *
 * 一虎二兔三猿子，
 * 四牛五犬六雞鬥，
 * 七龍八蛇九馬丁，
 * 十羊十一豬十二鼠。
 * 勿渡舟、過竹橋、遙籃，幼時難養、壯年亦忌。凡正二月寅卯時生人，犯此忌過橋汲水照影。(生育禮俗)
 */
val p斷橋關 = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return setOf(
      Pair(寅, 寅),
      Pair(卯, 卯),
      Pair(辰, 申),
      Pair(巳, 丑),
      Pair(午, 戌),
      Pair(未, 酉),
      Pair(申, 辰),
      Pair(酉, 巳),
      Pair(戌, 午),
      Pair(亥, 未),
      Pair(子, 亥),
      Pair(丑, 子),
    ).contains(eightWords.month.branch to eightWords.hour.branch).let {
      if (it) {
        斷橋關
      } else {
        null
      }
    }
  }
}

/**
 * 甲己見蛇傷，乙庚鼠內藏，丙辛猴覓果，丁壬犬吠汪，戊癸愁逢兔，孩兒有水殃。此關以年干取、如甲己生人巳時是，君流年浮沉合之少壯命皆有水災之厄，切宜防慎吉。(象吉)
 * 甲己見蛇傷，乙庚鼠內藏，丙辛猴見果，丁壬犬吠汪，戊癸愁逢兔，孩兒有水殃。如甲己生人巳時是，若流年浮沉合之災，旺命該有水厄之災，切宜防吉。(鰲頭)
 * 甲己見蛇傷，乙庚鼠內藏，丙辛猴覓果，丁壬犬吠汪，戊癸愁逢兔，孩兒有水殃。(星平會海)
 * 甲己見蛇傷，乙庚鼠內藏，丙辛猴見果，丁壬犬吠汪，戊癸愁逢兔，孩兒有水殃。勿近井水邊、渡舟、游泳有水厄之災。凡午巳卯申戌時生人，犯此忌見井泉、池塘、涌溪。(生育禮俗)
 *
 */
val p落井關A = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return setOf(
      Pair(土, 巳),
      Pair(金, 子),
      Pair(水, 申),
      Pair(木, 戌),
      Pair(火, 卯),
    ).contains(eightWords.year.stem.fiveElement to eightWords.hour.branch).let {
      if (it) {
        落井關
      } else {
        null
      }
    }
  }
}

/**
 * 凡甲己日逢巳時，乙庚日逢子時，丙辛日逢申時，丁壬日逢戌時，戊癸日逢卯時生人是。犯此須遠離井泉、池塘、河邊、溪流、海邊避免遭厄難。(黃曆解秘)
 */
val p落井關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return setOf(
      Pair(土, 巳),
      Pair(金, 子),
      Pair(水, 申),
      Pair(木, 戌),
      Pair(火, 卯),
    ).contains(eightWords.day.stem.fiveElement to eightWords.hour.branch).let {
      if (it) {
        落井關
      } else {
        null
      }
    }
  }
}

/**
 * 正七休生巳亥時，二八辰戌不堪推，三九卯酉生兒惡，四十寅申主哭悲。五十一月丑未死，六十二月子午啼。此關俗忌坐轎車、止忌生時，帶著童限不忌，大抵亦無甚凶。(象吉)
 * 正七休生巳亥時，二八辰戌不堪推，三九卯酉生兇惡，四十寅申主哭悲。五十一月丑未死，六十二月子午真。此關俗雲忌坐轎車、存忌，止忌生時，帶童限不忌。(鰲頭)
 * 正七休生巳亥時，二八辰戌不堪推，三九卯酉主人惡，四十寅申主交悲。五十一逢丑未死，六十二子午非宜。小兒若犯此關煞，父母不久主分離。(星平會海)
 * 正七巳亥時，二八辰戌時，三九卯酉時，四十寅申時，五十一丑未時，六十二子午時。與父母暫分離，俗稱忌座轎子而已。凡巳亥年正二月辰寅時生人，犯此忌坐桿竹椅太早。(生育禮俗)
 */
val p四柱關A = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (setOf(寅, 申).contains(eightWords.month.branch) && setOf(巳, 亥).contains(eightWords.hour.branch)) ||
      (setOf(卯, 酉).contains(eightWords.month.branch) && setOf(辰, 戌).contains(eightWords.hour.branch)) ||
      (setOf(辰, 戌).contains(eightWords.month.branch) && setOf(卯, 酉).contains(eightWords.hour.branch)) ||
      (setOf(巳, 亥).contains(eightWords.month.branch) && setOf(寅, 申).contains(eightWords.hour.branch)) ||
      (setOf(午, 子).contains(eightWords.month.branch) && setOf(丑, 未).contains(eightWords.hour.branch)) ||
      (setOf(未, 丑).contains(eightWords.month.branch) && setOf(子, 午).contains(eightWords.hour.branch))
    ) {
      四柱關
    } else {
      null
    }
  }
}

/**
 * 凡巳、亥年正、二月辰、巳時生人，犯此忌坐欄杆、竹椅太早。(黃曆解秘)
 */
val p四柱關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      setOf(巳, 亥).contains(eightWords.year.branch) &&
      setOf(寅, 卯).contains(eightWords.month.branch) &&
      setOf(辰, 巳).contains(eightWords.hour.branch)
    ) {
      四柱關
    } else {
      null
    }
  }
}

/**
 * 寅午戌龍當，巳酉丑虎亡，申子辰蛇上，亥卯未尋羊。此關生時上帶、主驚呼夜啼難養，如日干健則無事，日主弱凶。(象吉)
 * 寅午戌生龍位當，巳酉丑人在虎亡，申子辰生蛇頭立，亥卯未人去尋羊。此關生時上帶、主驚呼夜啼難養，如日干旺健則可無事矣。(鰲頭)
 * 寅午戌龍當，巳酉丑虎亡，申子辰蛇上，亥卯未尋羊。時上見主驚叫夜啼患。(星平會海)
 * 寅午戌龍當，巳酉丑虎郎，申子辰蛇上，亥卯未尋羊。時上見主驚叫夜啼。限內主小心看護，宜制化方可平安。凡子辰年巳時生人，犯此主多驚怖、夜啼之患。(生育禮俗)
 * 凡申、子、辰年逢巳時，巳、酉、丑年逢寅時，寅、午、戌年逢辰時，亥、卯、未年逢未時出生人是。犯此主多驚怖、夜啼之患。(黃曆解秘)
 */
val p短命關 = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗, 黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return when (BranchTools.trilogy(eightWords.year.branch)) {
      火    -> 辰
      金    -> 寅
      水    -> 巳
      木    -> 未
      else -> throw IllegalArgumentException("error")
    }.let { branch ->
      if (branch == eightWords.hour.branch) {
        短命關
      } else {
        null
      }
    }
  }
}

/**
 * 浴盆之煞最無良，春月忌龍夏忌羊，秋季犬兒須切忌，冬月逢牛定主傷。此生下地之時不可用腳盆，須用鐵鍋、火盆之類，洗之後無忌也，只忌月內一周之外不忌。(象吉)
 * 浴盆之煞最無良，春忌龍兮夏忌羊，秋忌犬兒須切忌，冬月逢丑定須防。出母勿用浴盆。(星平會海)
 * 浴盆之煞四季論，春忌龍兮夏忌羊，三秋忌犬冬忌丑，小兒初浴要提防。小兒初次洗浴時小心，解厄平安。正、二、三月申時生人，犯此忌沐浴太早。(生育禮俗)
 *
 * 浴盆之煞最無良，春忌龍兮夏忌羊，秋季人見切須忌，冬月逢午定主傷。此煞生子俗時不用瓦盆，宜用鐵銅大盆洗之後無忌。(鰲頭) (人 -> 犬? , 午 -> 牛? , 應該是抄錄錯誤, 不採用)
 */
val p浴盆關 = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && 辰 == eightWords.hour.branch) ||
      (巳午未.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (申酉戌.contains(eightWords.month.branch) && 戌 == eightWords.hour.branch) ||
      (亥子丑.contains(eightWords.month.branch) && 丑 == eightWords.hour.branch)
    ) {
      浴盆關
    } else {
      null
    }
  }
}


/**
 * 子午卯酉休逢馬，寅申巳亥虎驚人，辰戌丑未羊相觸，常防湯火厄相侵。此殺如子午卯酉生人忌午時，若小兒犯此招湯火之災，亦忌此限。蓋人生水火乃養生之要、何能免乎，知防慎則吉矣。(象吉)
 * 子午卯酉休遇馬，寅申巳亥虎驚人，辰戌丑未羊相撞，常防湯火厄相侵。其煞如子午卯酉生人忌午時，若小兒犯此招湯火之厄。(鰲頭)
 * 子午卯酉休騎馬，寅申巳亥虎驚人，辰戌丑未羊相觸，常防湯火厄相侵。（星平會海）
 * 子午卯酉莫逢馬，寅申巳亥怕見虎，辰戌丑未若遇羊，湯火傷身切須防。限內湯火油小心，宜制化保安寧。(生育禮俗)
 */
val p湯火關A = object : IHazardFactory {
  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 鰲頭通書, 星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (子午卯酉.contains(eightWords.year.branch) && eightWords.hour.branch == 午) ||
      (寅申巳亥.contains(eightWords.year.branch) && eightWords.hour.branch == 寅) ||
      (辰戌丑未.contains(eightWords.year.branch) && eightWords.hour.branch == 未)
    ) {
      湯火關
    } else {
      null
    }
  }
}

/**
 * 凡子、午、卯、酉年午時生人，犯此忌疵痲之患。另：丑、申、亥年逢未時，寅年逢巳時，卯年逢子時，午、酉年逢寅時出生人是。用火神錢制化、或到火神廟拜拜即吉。(黃曆解秘)
 */
val p湯火關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (子午卯酉.contains(eightWords.year.branch) && eightWords.hour.branch == 午) ||
      (setOf(丑, 申, 亥).contains(eightWords.year.branch) && eightWords.hour.branch == 未) ||
      (寅 == eightWords.year.branch && eightWords.hour.branch == 巳) ||
      (setOf(午, 酉).contains(eightWords.year.branch) && eightWords.hour.branch == 寅)
    ) {
      湯火關
    } else {
      null
    }
  }
}

/**
 * 春月生戌未，夏月見丑辰，秋月生丑戌，冬月見未辰。須防水火之災。(星平會海)
 * 春月生人見戌未，夏月生人見丑辰，秋月生人見丑戌，冬月生人見未辰。須防水厄、火湯油之災，解化保平安。凡正二三月未戌時生人，犯此主膿血、瘡疾太多及小心水火。(生育禮俗)
 */
val p水火關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && setOf(戌, 未).contains(eightWords.hour.branch)) ||
      (巳午未.contains(eightWords.month.branch) && setOf(丑, 辰).contains(eightWords.hour.branch)) ||
      (申酉戌.contains(eightWords.month.branch) && setOf(丑, 戌).contains(eightWords.hour.branch)) ||
      (亥子丑.contains(eightWords.month.branch) && setOf(未, 辰).contains(eightWords.hour.branch))
    ) {
      水火關
    } else {
      null
    }
  }
}

/**
 * 凡正、二、三月逢未、戌時生人，四、五、六月逢丑、辰時生人，七、八、九月逢酉時生人，十、十一、十二月逢丑時生人是。犯此主膿血、瘡疾太多及小心水火。民俗制化法：祭火神及水官大帝，用火神錢拜火神、用水官錢拜水官大帝。(黃曆解秘)
 */
val p水火關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && setOf(戌, 未).contains(eightWords.hour.branch)) ||
      (巳午未.contains(eightWords.month.branch) && setOf(丑, 辰).contains(eightWords.hour.branch)) ||
      (申酉戌.contains(eightWords.month.branch) && setOf(酉).contains(eightWords.hour.branch)) ||
      (亥子丑.contains(eightWords.month.branch) && setOf(丑).contains(eightWords.hour.branch))
    ) {
      水火關
    } else {
      null
    }
  }
}

/**
 * 春忌寅申夏忌羊，秋生雞嘴實堪傷，三冬切忌牛生角。十個孩兒九個亡。童限最忌痲痘災。(星平會海)
 * 春忌寅申時，夏怕未時當，秋季嫌酉時，冬天勿丑時。清明、七夕之日主不拜、多病多災。正二三月寅申時生人，犯此忌病疹災害。(生育禮俗)
 */
val p深水關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(星平會海, 生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && setOf(寅,申).contains(eightWords.hour.branch)) ||
      (巳午未.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (申酉戌.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) ||
      (亥子丑.contains(eightWords.month.branch) && 丑 == eightWords.hour.branch)
    ) {
      深水關
    } else {
      null
    }
  }
}

/**
 * 凡正、二、三月寅、申時生人，犯此忌病疹災害。俗云：犯此關之人與前世父母糾纏不清，應該避免在清明節、端午節、中秋節、除夕祭拜祖先，滿月及周歲皆要提前一天舉行。(黃曆解秘)
 */
val p深水關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && setOf(寅, 申).contains(eightWords.hour.branch))
    ) {
      深水關
    } else {
      null
    }
  }
}

/**
 * 子午卯酉單怕羊，寅申巳亥虎羊當，辰戌丑未雞常叫，連霄不睡到天光，春人怕馬夏逢雞，秋子冬卯不暫移，小兒若犯此關煞，定是三周半夜啼。此關利害難治，只是兩樣起例不同，因並錄之、云後一例有驗。(象吉)
 * 子午卯酉單怕羊，寅申巳亥虎羊當，辰戌丑未雞常叫，連霄不睡到天光，春馬夏雞秋怕子，冬兔三周半夜啼。(星平會海)
 *
 *
 */
val p夜啼關A = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(象吉通書, 星平會海)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (子午卯酉.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (寅申巳亥.contains(eightWords.month.branch) && setOf(寅,未).contains(eightWords.hour.branch)) ||
      (辰戌丑未.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) ||
      (寅卯辰.contains(eightWords.month.branch) && 午 == eightWords.hour.branch) ||
      (巳午未.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) ||
      (申酉戌.contains(eightWords.month.branch) && 子 == eightWords.hour.branch) ||
      (亥子丑.contains(eightWords.month.branch) && 卯 == eightWords.hour.branch)
    ) {
      夜啼關
    } else {
      null
    }
  }
}

/**
 * 子午卯酉單怕羊，寅申巳亥處牛當，辰戌丑未蟬常叫，連霄不睡到天光，春人怕馬夏逢雞，秋子冬卯不暫移，小兒若犯此關煞，定有三周半夜啼。此關利害難治，只是兩樣起例不同，因並錄之、云後則有驗。(鰲頭)
 */
val p夜啼關B = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(鰲頭通書)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (子午卯酉.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (寅申巳亥.contains(eightWords.month.branch) && 丑 == eightWords.hour.branch) ||
      // (辰戌丑未.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) || (蟬?)
      (寅卯辰.contains(eightWords.month.branch) && 午 == eightWords.hour.branch) ||
      (巳午未.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) ||
      (申酉戌.contains(eightWords.month.branch) && 子 == eightWords.hour.branch) ||
      (亥子丑.contains(eightWords.month.branch) && 卯 == eightWords.hour.branch)
    ) {
      夜啼關
    } else {
      null
    }
  }
}

/**
 * 辰戌丑未虎雞當，子午卯酉喜怕羊，寅申巳亥羊又忌，兒命犯之定有殃。限內夜間不要火，逢者制化自安。凡子午丑未時生人，犯此主夜間啾唧不寧。(生育禮俗)
 *
 */
val p夜啼關C = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(生育禮俗)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (子午卯酉.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (寅申巳亥.contains(eightWords.month.branch) && 未 == eightWords.hour.branch) ||
      (辰戌丑未.contains(eightWords.month.branch) && 寅 == eightWords.hour.branch)
    ) {
      夜啼關
    } else {
      null
    }
  }
}

/**
 * 凡正二三月逢午時生，四五六月逢酉時生，七八九月逢子時生，十十一十二月逢卯時生是。犯此主夜間啾唧不寧，制化保平安。(黃曆解秘)
 */
val p夜啼關D = object : IHazardFactory {

  override fun getBooks(): Set<Book> {
    return setOf(黃曆解秘)
  }

  override fun getHazard(eightWords: IEightWords, gender: Gender?): ChildHazard? {
    return if (
      (寅卯辰.contains(eightWords.month.branch) && 午 == eightWords.hour.branch) ||
      (巳午未.contains(eightWords.month.branch) && 酉 == eightWords.hour.branch) ||
      (申酉戌.contains(eightWords.month.branch) && 子 == eightWords.hour.branch) ||
      (亥子丑.contains(eightWords.month.branch) && 卯 == eightWords.hour.branch)
    ) {
      夜啼關
    } else {
      null
    }
  }
}

