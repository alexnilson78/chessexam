package com.anilson.chesshealthexam.util;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class PreferencesUtil {

    private static final String FIRST_LAUNCH_KEY = "first_launch";

    SharedPreferences sharedPreferences;

    @Inject
    public PreferencesUtil(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
    }

    public void setFirstLaunch() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH_KEY, false).apply();
    }
}
