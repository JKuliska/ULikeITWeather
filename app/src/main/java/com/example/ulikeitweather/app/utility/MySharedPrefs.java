package com.example.ulikeitweather.app.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ulikeitweather.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 20.6.2014.
 */
public class MySharedPrefs {

    public static final String SETTINGS_PREFS = "settingsPrefs"; //location where the preferences are stored
    public static final String SETTING_LENGTH_UNIT = "lengthUnit"; //true if kmh, false if mph
    public static final String SETTING_TEMP_UNIT = "tempUnit"; //true if celsius, false if fahrenheit

    private Context mContext;

    public MySharedPrefs(Context context) {
        mContext = context;
    }

    public boolean isKilometer() {
        return getCurrentSetting(SETTING_LENGTH_UNIT);
    }

    public void setKilometer() {
        changeCurrentSettings(SETTING_LENGTH_UNIT);
    }

    public boolean isCelsius() {
        return getCurrentSetting(SETTING_TEMP_UNIT);
    }

    public void setCelsius() {
        changeCurrentSettings(SETTING_TEMP_UNIT);
    }


    /*
    changes a given settings value based on a key for storing in SharedPreferences
    @params: a key of the item that is to be changed
     */
    private void changeCurrentSettings(String settingKey) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(SETTINGS_PREFS, 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean(settingKey, !mPrefs.getBoolean(settingKey, true));
        mPrefsEditor.commit();
    }


    /*
    obtains the current settings from SharedPreferences
    @return: a list containing the current settings values
     */
    private boolean getCurrentSetting(String which) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(SETTINGS_PREFS, 0);
        return mPrefs.getBoolean(which, true);
    }

}
