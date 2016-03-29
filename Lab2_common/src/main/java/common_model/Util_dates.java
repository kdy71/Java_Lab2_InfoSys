package common_model;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilites and constants for Lab 1 (Task Manager)
 * Created by Dmitry Khoruzhenko on 08.01.2016.
 */
public class Util_dates {

    public static final SimpleDateFormat sdf_rus_ms  = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss .SSS");
    public static final SimpleDateFormat sdf_rus_day = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Convert date to string in format "dd.MM.yyyy HH:mm:ss"  or String "null" if date is null
     *
     * @param dat1 - Date for formatting
     * @return date as String in format "dd.MM.yyyy HH:mm:ss"  or String "null" if date is null
     */
    public static String dat2Str(Date dat1) {
        if (dat1 == null)
            return "null";
        else
            return sdf_rus_day.format(dat1);
    }

    /**
     * Возвращает текущее время в строковом виде
     * @return
     */
    public static String now2Str() {
        return sdf_rus_ms.format(new Date());
    }


    /**
     * Convert string (dd.MM.yyyy) to date
     *
     * @param stDate
     * @return Date from String
     * @throws ParseException
     */
    public static Date str2Date(String stDate) throws ParseException {
        return sdf_rus_day.parse(stDate);
    }

}
