package com.example.ulikeitweather.app;

import android.os.Environment;

import java.io.File;

public class ULikeITWeatherConfig {

    public static final boolean LOGS = true;
    public static final boolean DEV_API = true;

    public static final String API_ENDPOINT_PRODUCTION = "http://example.com/api/";
    public static final String API_ENDPOINT_DEVELOPMENT = "http://dev.example.com/api/";

    public static final String GCM_REGISTER_URL = "http://example.com/register";
    public static final String GCM_UNREGISTER_URL = "http://example.com/unregister";
    public static final String GCM_SENDER_ID = "0123456789";

}
