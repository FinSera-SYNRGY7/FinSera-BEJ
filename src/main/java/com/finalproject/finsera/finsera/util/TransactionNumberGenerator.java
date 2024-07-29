package com.finalproject.finsera.finsera.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionNumberGenerator {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final int MAX_SEQUENCE = 9999;

    public static String generateTransactionNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());

        int sequence = counter.incrementAndGet();
        if (sequence > MAX_SEQUENCE) {
            counter.set(0);
            sequence = counter.incrementAndGet();
        }

        return String.format("%s%04d", timestamp, sequence);
    }
}