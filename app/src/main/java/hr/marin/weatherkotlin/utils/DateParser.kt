package hr.marin.weatherkotlin.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateParser {
    fun formatDate(oldString:String, newFormat:String):String {
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"
        val sdf = SimpleDateFormat(oldFormat, Locale.US)
        var d: Date? = null
        try
        {
            d = sdf.parse(oldString)
        }
        catch (e:ParseException) {
            e.printStackTrace()
        }
        sdf.applyPattern(newFormat)
        return sdf.format(d)
    }
    fun formatWeekday(dateString:String):String {
        val oldFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(oldFormat, Locale.US)
        var d: Date? = null
        try
        {
            d = sdf.parse(dateString)
        }
        catch (e:ParseException) {
            e.printStackTrace()
        }
        sdf.applyPattern("EEEE")
        return sdf.format(d)
    }
}