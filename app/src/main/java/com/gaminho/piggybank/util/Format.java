package com.gaminho.piggybank.util;

public enum Format {

    DATE_DD_MM_YYYY("dd/MM/yyyy"),
    DATE_YYYY_MM_DD("yyyy/MM/dd");

    final String format;

    Format(final String format) {
        this.format = format;
    }
}
