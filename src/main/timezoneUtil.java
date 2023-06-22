package main;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;

public class timezoneUtil {

    /**
     * Converts time in current time zone to UTC to be stored in DB
     * @param dateTime Represents a date and time string that needs to be converted to UTC format.
     * @return
     */
    public static String convertDateTimetoUTC(String dateTime) {
        Timestamp currentTimeStamp = Timestamp.valueOf(dateTime);
        LocalDateTime localDT = currentTimeStamp.toLocalDateTime();
        ZonedDateTime zoneDT = localDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime utcDT = zoneDT.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localOut = utcDT.toLocalDateTime();
        String UTCOut = localOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return UTCOut;
    }

}