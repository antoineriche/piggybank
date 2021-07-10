package com.gaminho.piggybank.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public final class Formatter {

    private static final DecimalFormat DECIMAL_FORMAT = buildDecimalFormat();

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
        return DECIMAL_FORMAT.format(valueToFormat);
    }

    public static String formatAmount(final double valueToFormat){
        return formatDouble(valueToFormat).concat(" €");
    }

    private static DecimalFormat buildDecimalFormat() {
        final DecimalFormat nf = new DecimalFormat("###########0.00");
        final DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator('.');
        customSymbol.setGroupingSeparator(' ');
        nf.setGroupingSize(3);
        nf.setDecimalFormatSymbols(customSymbol);
        nf.setGroupingUsed(true);
        return nf;
    }
}
