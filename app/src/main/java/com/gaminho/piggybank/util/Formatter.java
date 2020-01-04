package com.gaminho.piggybank.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class Formatter {

    public static String dateToString(final LocalDate localDate, final Format format){
        return dateToString(localDate.atStartOfDay(), format);
    }

    public static String dateToString(final Date date, final Format format){
        return dateToString(DateUtils.toLocalDate(date), format);
    }

    public static String dateToString(final LocalDateTime localDateTime, final Format format){
        return localDateTime.format(DateTimeFormatter.ofPattern(format.format));
    }
}
