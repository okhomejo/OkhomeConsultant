package id.co.okhome.consultant.lib.app;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Locale;

/**
 * Created by jo on 2018-04-13.
 */

public class OkhomeDateTimeFormatUtil {

    public final static String printFullDateTime(String yyyyMMddHHmmss){
        return printOkhomeType(yyyyMMddHHmmss, "E d MMM yy, hh:mm a");
    }

    public final static String printFullDateTimeWithoutDayName(String yyyyMMddHHmmss){
        return printOkhomeType(yyyyMMddHHmmss, "d MMM yy, hh:mm a");
    }

    public final static String printFullDate(String yyyyMMddHHmmss){
        return printOkhomeType(yyyyMMddHHmmss, "E d MMM yy");
    }

    public final static String printOkhomeType(String yyyyMMddHHmmss, String targetFormat){
        return printOkhomeType(yyyyMMddHHmmss, "yyyy-MM-dd HH:mm:ss", targetFormat);
    }

    public final static String printOkhomeType(String fromDateTime, String fromFormat, String targetFormat){
        DateTime dateTime = DateTimeFormat.forPattern(fromFormat).parseDateTime(fromDateTime);
        Locale locale = new Locale("id");
        return DateTimeFormat.forPattern(targetFormat).withLocale(locale).print(dateTime);
    }


}
