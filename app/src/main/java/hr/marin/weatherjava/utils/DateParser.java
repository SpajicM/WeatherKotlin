package hr.marin.weatherjava.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser {
    public static String formatDate(String oldString, String newFormat) {
        final String OLD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
        Date d = null;
        try {
            d = sdf.parse(oldString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(newFormat);
        return sdf.format(d);
    }


    public static String formatWeekday(String dateString) {
        final String OLD_FORMAT = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.US);
        Date d = null;
        try {
            d = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("EEEE");
        return sdf.format(d);
    }
}
