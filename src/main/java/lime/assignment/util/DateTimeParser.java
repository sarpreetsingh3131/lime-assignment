package lime.assignment.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {

    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("M/d/y h:m:s a"));
    }

    public static LocalDateTime updateTimeZone(String timeZone, LocalDateTime localDateTime) {
        return localDateTime
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of(timeZone))
                .toLocalDateTime();
    }
}
