package hr.marin.weatherkotlin.utils

import java.text.SimpleDateFormat
import java.util.*

object DateParser {
    fun formatDate(oldString: String, newFormat: String): String? {
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"

        return format(oldString, oldFormat, newFormat)
    }

    fun formatWeekday(dateString: String):String {
        val oldFormat = "yyyy-MM-dd"
        val newFormat = "EEEE"

        return format(dateString, oldFormat, newFormat);
    }

    private fun format(dateString: String, oldFormat: String, newFormat: String): String {
        val parser = SimpleDateFormat(oldFormat, Locale.US)
        val formatter = SimpleDateFormat(newFormat, Locale.US)
        val date = parser.parse(dateString)
        return if(date != null) {
            formatter.format(date);
        } else {
            "";
        }
    }
}