package com.finalproject.finsera.finsera.util;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

@Component
public class DateFormatterIndonesia {
    public static String dateFormatterIND(Date date) {
        String dateTimeString = String.valueOf(date);
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("EEE MMM dd HH:mm:ss z yyyy")
                .toFormatter();
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, inputFormatter.withZone(ZoneId.of("GMT")));
        ZonedDateTime jakartaTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Jakarta"));

        DateTimeFormatter outputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM yyyy HH:mm")
                .toFormatter(new Locale("id", "ID"));

        return jakartaTime.format(outputFormatter) + " WIB";
    }

    public static String otherDateFormatterIND(Date date) {
        String dateTimeString = String.valueOf(date);
        String dateTimeWithoutZone = dateTimeString.replace(" WIB", "");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy", java.util.Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeWithoutZone, inputFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Jakarta"));
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yy, h:mm a");
        return zonedDateTime.format(outputFormatter) + " WIB";
    }
    public static String formatCurrency(int amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(amount);
    }
}
