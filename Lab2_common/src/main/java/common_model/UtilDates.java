package common_model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilites and constants for Lab 1 (Task Manager)
 * Created by Dmitry Khoruzhenko on 08.01.2016.
 */
public class UtilDates {

    public static final SimpleDateFormat DATE_FORMAT_RUS_MILLIS = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss .SSS");
    public static final SimpleDateFormat DATE_FORMAT_RUS_DAY = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Convert date to string in format "dd.MM.yyyy HH:mm:ss"  or String "null" if date is null
     *
     * @param dat1 - Date for formatting
     * @return date as String in format "dd.MM.yyyy HH:mm:ss"  or String "null" if date is null
     */
    public static String dateToString(Date dat1) {
        if (dat1 == null)
            return "null";
        else
            return DATE_FORMAT_RUS_DAY.format(dat1);
    }

    /**
     * Возвращает текущее время в строковом виде
     *
     * @return
     */
    public static String currentDateToString() {
        return DATE_FORMAT_RUS_MILLIS.format(new Date());
    }


    /**
     * Convert string (dd.MM.yyyy) to date
     *
     * @param stDate
     * @return Date from String
     * @throws ParseException
     */
    public static Date stringToDate(String stDate) throws ParseException {
        return DATE_FORMAT_RUS_DAY.parse(stDate);
    }

}
