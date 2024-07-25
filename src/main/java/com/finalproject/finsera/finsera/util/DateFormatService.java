package com.finalproject.finsera.finsera.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFormatService {

    private static final String INPUT_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";
    private static final String OUTPUT_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat(INPUT_FORMAT);
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat(OUTPUT_FORMAT);

    public Date formatDate(Date inputDateStr) {
        try {
            String outputDate = outputDateFormat.format(inputDateFormat);
            return outputDateFormat.parse(outputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}