/**
 * Created by smallufo on 2015-05-27.
 */
package destiny.core.chinese

import destiny.core.Descriptive
import destiny.core.calendar.ILocation
import destiny.core.calendar.Location
import destiny.core.chinese.Branch.*

import java.time.chrono.ChronoLocalDateTime

/**
 * 取「月將」 (不是月令干支！)
 * 一般而言就是太陽星座（過中氣）
 * 但是有些人堅持用「月支相合」（過節）
 * 因此會產生兩種實作
 */
interface IMonthMaster : Descriptive {

  /** 取得「月將」的方法  */
  fun getBranch(lmt: ChronoLocalDateTime<*>, location: ILocation): Branch

  companion object {

    /** 取得「月將」的中文稱謂
     *
     * @see [十二月將](http://zh.wikipedia.org/wiki/十二月將)
     *
     */
    fun getName(branch: Branch): String {
      return when (branch) {
        子 -> "神後" // 神後者，古之媒氏也。本齊國人，姓賈，字仲獄，欺詐取財而死於十二月子日，除為神後。知人婚姻陰私事。天乙臨，主貴人接引之喜。騰蛇婦女悲泣，朱雀凶喪信息，六合主成合交易，勾陳絕嗣鬥訟。青龍望貴求財喜。天後婚姻成，百事吉。太陰同，玄武文狀怪現。太常失財，先凶後吉。白虎望遠信到家喜。天空主田土凶，多虛少實。
        丑 -> "大吉" // 大吉者，古之牛圈也。本鄭國人，姓鄭，字季賢，病死十一月丑日，除為大吉。知人年命憂喜、六畜、田宅、口舌事。天乙臨求貴，祿直至，百事吉。騰蛇臨之百事喜，朱雀亦然，六合爭訟凶。勾陳男盜女奸，凶。青龍求晉官及財喜。天后陰病暗昧凶，太陰陰權財帛進人口。玄武陰謀鬥訟凶。太常陰財喜，白虎盜賊失財及四足。天空臨之，主四季相會，主殺害之凶，亦主鬥訟。
        寅 -> "功曹" // 功曹者，古之太史。宋國人，姓孟，字仲賢，欺客印死於十月寅日，除為功曹。知人官事、口舌、文字、信息。天乙加臨，主印信之喜。騰蛇主驚憂，後喜生女也，朱雀遠信、火光，六合婚姻不成，勾陳婦女爭訟，青龍本位大吉，天后婦女婚姻，太陰同天後，玄武財喜不失，太常陰財破，白虎入家凶，天空鬥訟虛詐。
        卯 -> "太沖" // 太衝者，古之盜人也。本秦國人，姓姜，字漢陽，撓擾村邑，為盜門戶死九月卯日，除為太衝。知人本命盜賊，門戶分張事。天乙臨門，貴人得財吉。騰蛇火光文字、官事，朱雀同騰蛇，六合主成就婚姻，勾陳陰訟田宅，青龍臨門立有喜至。天後主婚姻，百事成。太陰同天後，玄武盜財，發動得財之喜。太常外得陰財，須主孝順。白虎傷人凶，外出失財及訟也。天空求事不成，外事勾連。
        辰 -> "天罡" // 酉月以辰為月將，堅剛肅殺之意，天罡是土神，其形為面圓而須多，其色黃，古之獄師也。味甘，其數五。
        巳 -> "太乙" // 申月以巳為將，萬寶成熟之意，故名太乙，是火神，外形為額髙口大，面有斑點，古之鍛人也。味苦，其數四。
        午 -> "勝光" // 未月以午為月將，焰火不息之像故名勝光，是火神，眼雖小，但顏面大，色紅，味苦，其數九，古之御馬人。
        未 -> "小吉" // 午月以未為月將，萬物小成之意，故稱小吉，是土神，外形光澤，色黃，味甘，其數為八，古之藥師。
        申 -> "傳送" // 巳月以申為月將，傳陰送陽之意，傳送是金神，古形為項短，目僻，色黑白，古之行人也。味辛，其數四，古人之形。
        酉 -> "從魁" // 從魁，古之亡徒也。本燕國人，姓孟，字仲任。逃亡客死三月酉日，除為從魁。知人年命、陰私、囚死之事。天乙臨，因人得貴立至喜。騰蛇悲泣臨門凶，朱雀遠信臨門凶，六合婚姻成就，勾陳娼婦臨門凶，青龍財帛臨門喜。天后臨，婦人生產，陰人主家。太陰同天後。玄武盜賊失財，男盜女奸，徒刑為兵。太常陰人常處財喜。白虎凶喪臨門立至。天空臨門為骸骨神，主大葬。
        戌 -> "河魁" // 河魁者，古之亡奴也。本晉國人，姓郭字太宅，病死二月戌日，除為河魁。知人田宅骸骨事也。天乙加臨犯煞凶，騰蛇朱雀陰訟為盜凶。六合爭骸骨，或爭墳墓。勾陳奴僕殺害，亦主訟。青龍貴人帶犬入家凶，天後主悲泣，太陰陰葬喜，玄武盜賊軍兵訟凶，常陰財凶。白虎刀兵斬殺重喪凶，天空鬥訟凶。
        亥 -> "登明" // 登明者，古之獄吏也。本魯國人，姓韓字燕，七坐賊獄，死正月亥日，除為登明。知人縣官、田宅、徵召事。天乙臨，貴人田宅訟。騰蛇信息患病事，朱雀同騰蛇。六合交易喜，及婚姻求就吉。勾陳陰人爭訟田土。青龍得財，望貴人。天后陰權婚成，百事大吉。太陰同天後。玄武鬼怪、盜賊不害。太常悲泣，白虎道路、病符皆無害。天空牢獄凶。
      }
    }
  }
}
