package com.finalproject.finsera.finsera.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

@Component
public class DateFormatterIndonesia {
    public String dateFormatterIND(Date date) {
        String dateTimeString = String.valueOf(date);
        String dateTimeWithoutZone = dateTimeString.replace(" WIB", "");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeWithoutZone, inputFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter outputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM yyyy HH:mm")
                .toFormatter(new Locale("id", "ID"));

        return zonedDateTime.format(outputFormatter) + " WIB";
    }

    public String otherDateFormatterIND(Date date) {
        String dateTimeString = String.valueOf(date);
        String dateTimeWithoutZone = dateTimeString.replace(" WIB", "");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeWithoutZone, inputFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yy, h:mm a");
        return zonedDateTime.format(outputFormatter) + " WIB";
    }
}
