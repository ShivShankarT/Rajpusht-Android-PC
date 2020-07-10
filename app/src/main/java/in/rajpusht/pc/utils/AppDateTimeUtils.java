package in.rajpusht.pc.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppDateTimeUtils {

    private static DateFormat serverDf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static DateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    private static DateFormat localDf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    private static DateFormat localDateTimeDf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

    public static String convertLocalDate(Date date) {
        if (date != null)
            return localDf.format(date);
        else
            return null;
    }

    public static String convertLocalDateTime(Date date) {
        if (date != null)
            return localDateTimeDf.format(date);
        else
            return null;
    }

    public static String convertServerDate(Date date) {
        if (date != null)
            return serverDf.format(date);
        else
            return null;
    }

    public static Date convertDateFromServer(String date) {

        if (date != null) {
            try {
                return serverDf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String convertServerTimeStampDate(Date date) {
        if (date != null)
            return timestamp.format(date);
        else
            return null;
    }

    public static Date convertServerTimeStampDate(String date) {
        if (date != null) {
            try {
                return timestamp.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
