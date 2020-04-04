package com.teamalpha.bloodpals.presentation.utils;

import android.content.Context;

public class TextStringResource extends StringResource {

    private String text;

    public TextStringResource(String text) {
        this.text = text;
    }

    @Override
    public String format(Context context) {
        return text;
    }

}
