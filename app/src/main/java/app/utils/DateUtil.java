package app.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fadejimi on 7/17/18.
 */

public class DateUtil {
    static String old_pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static String new_pattern = "EEE, MMM d, ''yy";

    public static String getCurrentDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public static String formatter(String oldDate) {
        try {
            Date date = new SimpleDateFormat(old_pattern).parse(oldDate);
            return new SimpleDateFormat(new_pattern).format(date);
        }
        catch(ParseException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
