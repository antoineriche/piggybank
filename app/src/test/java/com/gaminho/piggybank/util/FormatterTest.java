package com.gaminho.piggybank.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bonnie on 2021-07-10
 */
public class FormatterTest {

    @Test
    public void test_formatAmount() {
        assertEquals("0.00 €", Formatter.formatAmount(0D));
    }
}