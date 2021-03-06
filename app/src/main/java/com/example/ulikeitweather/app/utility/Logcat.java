package com.example.ulikeitweather.app.utility;

import com.example.ulikeitweather.app.ULikeITWeatherConfig;

public class Logcat {
    public static final String TAG = "EXAMPLE";


    public static void d(String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.d(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.d(tag, msg);
    }

    public static void e(String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.e(tag, msg);
    }

    public static void e(String msg, Throwable tr) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.e(TAG, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.e(tag, msg, tr);
    }

    public static void i(String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.i(tag, msg);
    }

    public static void v(String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.v(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.v(tag, msg);
    }

    public static void w(String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.w(TAG, msg);
    }

    public static void w(String tag, String msg) {
        if (ULikeITWeatherConfig.LOGS) android.util.Log.w(tag, msg);
    }
}

