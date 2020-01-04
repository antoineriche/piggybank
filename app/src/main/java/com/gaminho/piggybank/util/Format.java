package com.gaminho.piggybank.util;

public enum Format {

    DATE_DD_MM_YYYY("dd/MM/yyyy");

    String format;

    Format(final String format) {
        this.format = format;
    }
}
