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
public class Util {

    public static final SimpleDateFormat sdf_eng_ms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat sdf_eng_sec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdf_rus_ms  = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss .SSS");
    public static final SimpleDateFormat sdf_rus_sec = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    public static final SimpleDateFormat sdf_rus_day = new SimpleDateFormat("dd.MM.yyyy");

    public static final int msInSecond = 1000;     // number of  milliseconds in second
    public static final int msInMinute = msInSecond * 60;  // = 60 000  (number of  milliseconds in minute)
    public static final int msInHour = msInMinute * 60;  // = 3 600 000  (number of  milliseconds in hour)
    public static final int msInDay = msInHour * 24;    // = 86 400 000  (number of  milliseconds in day)


    /**
     * Convert (time interval as integer) to (time interval as string)
     *
     * @param interval - time interval as integer
     * @return - time interval as string
     */
    public static String timeIntervalToString(int interval) {
        StringBuffer sb = new StringBuffer("");
        int days = interval / msInDay;
        int rest = interval - (days * msInDay);
        int hours = rest / msInHour;
        rest = rest - (hours * msInHour);
        int minutes = rest / msInMinute;
        rest = rest - (minutes * msInMinute);
        int seconds = rest / msInSecond;
        int mseconds = rest - (seconds * msInSecond);
        if (days > 0) {
            sb.append(days).append(days == 1 ? " day " : " days ");
        }
        if (hours > 0) {
            sb.append(hours).append(hours == 1 ? " hour " : " hours ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(seconds == 1 ? " second " : " seconds ");
        }
        if (mseconds > 0) {
            sb.append(mseconds).append(mseconds == 1 ? " millisecond " : " milliseconds ");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    /**
     * Convert (time interval as string) to (time interval as integer)
     *
     * @param stInterval - time interval as string
     * @return - time interval as integer
     * @throws ParseException
     */
    public static int strToTimeInterval(String stInterval) throws ParseException {
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        try {
            String[] mapElements = stInterval.split(" ");
            for (int i = 0; i < mapElements.length; i = i + 2) {
                int intValueElem = Integer.parseInt(mapElements[i]);
                String strKeyElem = mapElements[i + 1];
                String lastChar = strKeyElem.substring(strKeyElem.length() - 1);
                if (!lastChar.equals("s")) {
                    strKeyElem += "s";
                }
                myMap.put(strKeyElem, intValueElem);
            }
            Integer days = myMap.get("days");
            Integer hours = myMap.get("hours");
            Integer minutes = myMap.get("minutes");
            Integer seconds = myMap.get("seconds");
            Integer milliseconds = myMap.get("milliseconds");
            if (days == null) {
                days = 0;
            }
            if (hours == null) {
                hours = 0;
            }
            if (minutes == null) {
                minutes = 0;
            }
            if (seconds == null) {
                seconds = 0;
            }
            if (milliseconds == null) {
                milliseconds = 0;
            }

            int result = (days * msInDay) +
                    (hours * msInHour) +
                    (minutes * msInMinute) +
                    (seconds * msInSecond) +
                    (milliseconds);
            return result;
        } catch (Exception e) {
            throw new ParseException("Can't convert time interval to integer.\n" + e.getMessage(), 0);
        }
    }


    /**
     * Convert int value to byte array
     * from   http://stackoverflow.com/questions/5399798/byte-array-and-int-conversion-in-java
     *
     * @param value - int value for converting
     * @return -  array of bytes (converted int value)
     */
    public static byte[] intToByteArray(int value) {
        byte[] byteArr = new byte[4];
        byteArr[3] = (byte) (value >> 24);
        byteArr[2] = (byte) (value >> 16);
        byteArr[1] = (byte) (value >> 8);
        byteArr[0] = (byte) value;
        return byteArr;
    }


    /**
     * Convert long value to byte array
     * from   https://www.daniweb.com/programming/software-development/code/216874/primitive-types-as-byte-arrays
     *
     * @param data - long value for converting
     * @return - array of bytes (converted long value)
     */
    public static byte[] longToByteArray(long data) {
        return new byte[]{
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }


    /**
     * Convert byte array to long value
     * from  https://www.daniweb.com/programming/software-development/code/216874/primitive-types-as-byte-arrays
     *
     * @param data - byte array for converting
     * @return - long value (converted byte array)
     */
    public static long byteArrayToLong(byte[] data) {
        if (data == null || data.length != 8) return 0x0;
        return (long) (
                // (Below) convert to longs before shift because digits
                //         are lost with ints beyond the 32-bit limit
                (long) (0xff & data[0]) << 56 |
                        (long) (0xff & data[1]) << 48 |
                        (long) (0xff & data[2]) << 40 |
                        (long) (0xff & data[3]) << 32 |
                        (long) (0xff & data[4]) << 24 |
                        (long) (0xff & data[5]) << 16 |
                        (long) (0xff & data[6]) << 8 |
                        (long) (0xff & data[7]) << 0
        );
    }


    /**
     * Convert byte array to int value
     * from   http://www.sql.ru/forum/419204/byte-to-int
     *
     * @param bArr - byte array for converting
     * @return - int value (converted byte array)
     */
    public static int byteArrayToInt(byte[] bArr) {
        int i = (bArr[3] << 24) | (bArr[2] << 16) | (bArr[1] << 8) | bArr[0];
        return i;
    }


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


    public static void showError(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Внимание",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showYesNoMessage(String message) {
        Object[] options = {"Да", "Нет!"};
        int n = JOptionPane.showOptionDialog(new JFrame(), message,
                "Подтверждение", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return (n == 0);
    }

}
