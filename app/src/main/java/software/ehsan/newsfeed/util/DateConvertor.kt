package software.ehsan.newsfeed.util

import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class DateConvertor {
    companion object {
        fun formatDate(date: String): String {
            val sourceDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            sourceDateFormat.timeZone = TimeZone.getTimeZone("GMT");

            val convertedDate = sourceDateFormat.parse(date)
            val prettyTime = PrettyTime(Locale.ENGLISH)
            return prettyTime.format(convertedDate)
        }
    }

}
