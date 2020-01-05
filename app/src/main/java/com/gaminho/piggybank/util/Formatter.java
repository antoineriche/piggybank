package com.gaminho.piggybank.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class Formatter {

    public static String dateToString(final LocalDate localDate, final Format format){
        return dateToString(localDate.atStartOfDay(), format);
    }

    public static String dateToString(final Date date, final Format format){
        return dateToString(DateUtils.toLocalDate(date), format);
    }

    private static String dateToString(final LocalDateTime localDateTime, final Format format){
        return localDateTime.format(DateTimeFormatter.ofPattern(format.format));
    }

    public static String formatDouble(final double valueToFormat){
        return String.format(Locale.FRANCE, "%.02f", valueToFormat);
    }

    public static String formatAmount(final double valueToFormat){
        return formatDouble(valueToFormat).concat(" €");
    }
}
