package com.gaminho.piggybank.util;

import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

public final class ViewUtils {

    public static boolean isEditTextFilled(final EditText editText) {
        return StringUtils.isNotBlank(editText.getText().toString().trim());
    }
}
