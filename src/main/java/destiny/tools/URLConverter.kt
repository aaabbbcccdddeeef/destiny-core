/**
 * @author smallufo
 * Created on 2007/2/16 at 上午 1:00:13
 */
package destiny.tools

import org.apache.commons.lang3.StringUtils
import java.io.Serializable
import java.util.*
import java.util.regex.Pattern

/**
 * TODO : 將 URL (http / ftp / email ... ) 轉換成 HTML 的 link ,
 * 目前 (2009/11/10) 只完成了 http , 而且無法處理中時電子報的怪網址 (有逗點)
 */
class URLConverter : Serializable {
  internal val pattern = Pattern.compile(
    //"(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?+%/.\\w]+)?",
    "(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(https://)?(http://)?[a-zA-Z_\\d\\-]+(\\.\\w[a-zA-Z_\\d\\-]+)+(/[#&\\n\\-=?+%/.\\w]+)?",
    Pattern.MULTILINE or Pattern.CASE_INSENSITIVE)

  fun convert(initialText: String): String {
    val result = StringBuffer(initialText.length)


    val m = pattern.matcher(initialText)
    while (m.find()) {
      val href = m.group()
      if (href.startsWith("@")) {
        continue
      }

      // ignore links that are already hyperlinks
      if (href.startsWith("href")) {
        continue
      }

      // TODO: add more top domains
      if (StringUtils.indexOfAny(href.lowercase(Locale.getDefault()), ".com", ".net", ".org", ".to", ".tw", ".ly", ".cc", ".us", ".pro", ".gl") != -1) {
        if (!StringUtils.startsWithIgnoreCase(href , "http://") && !StringUtils.startsWithIgnoreCase(href , "https://")) {
          m.appendReplacement(result, "<a href=\"http://$href\" target=\"_blank\">$href</a>")
        } else
          m.appendReplacement(result, "<a href=\"$href\" target=\"_blank\">$href</a>")
      }
    }
    m.appendTail(result)
    return result.toString()
  }
}
