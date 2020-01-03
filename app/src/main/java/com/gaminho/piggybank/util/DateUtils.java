package com.gaminho.piggybank.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

    public static Date toDate(LocalDate localDate){
        return toDate(localDate.atStartOfDay());
    }

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date){
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date){
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
