package com.teamalpha.bloodpals.presentation.utils;

import android.content.Context;

public class IdStringResource extends StringResource {

    private int id;

    public IdStringResource(int id) {
        this.id = id;
    }

    @Override
    public String format(Context context) {
        return context.getString(id);
    }
}
