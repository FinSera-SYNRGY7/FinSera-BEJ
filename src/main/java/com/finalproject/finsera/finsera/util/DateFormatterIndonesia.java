package com.finalproject.finsera.finsera.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

@Component
@Slf4j
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
        LocalDateTime localDateTime = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTimeFormatter outputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("d MMMM yyyy HH:mm")
                .toFormatter(new Locale("id", "ID"));
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Jakarta"));
        return zonedDateTime.format(outputFormatter) + " WIB";
    }
    public static String formatCurrency(int amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormatter.format(amount);
    }
}
