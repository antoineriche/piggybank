package com.gaminho.piggybank.util;

public enum Format {

    DATE_DD_MM_YYYY("MM/dd/yyyy");

    String format;

    Format(final String format) {
        this.format = format;
    }
}
