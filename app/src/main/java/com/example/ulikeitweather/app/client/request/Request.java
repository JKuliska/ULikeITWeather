package com.example.ulikeitweather.app.client.request;

import android.os.Bundle;

import com.example.ulikeitweather.app.ULikeITWeatherConfig;
import com.example.ulikeitweather.app.client.response.Response;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.IOException;
import java.io.InputStream;


public abstract class Request
{
    public static final String API_ENDPOINT = ULikeITWeatherConfig.DEV_API ? ULikeITWeatherConfig.API_ENDPOINT_DEVELOPMENT : ULikeITWeatherConfig.API_ENDPOINT_PRODUCTION;
    public static final String CHARSET = "UTF-8";
    public static final String BOUNDARY = "0xKhTmLbOuNdArY";

    private Bundle mMetaData = null;

    public abstract String getRequestMethod();
    public abstract String getAddress();
    public abstract Response<?> parseResponse(InputStream stream) throws IOException, JsonParseException;


    public byte[] getContent()
    {
        return null;
    }


    public String getBasicAuthUsername()
    {
        return null;
    }


    public String getBasicAuthPassword()
    {
        return null;
    }


    public boolean isMultipart()
    {
        return false;
    }


    public Bundle getMetaData()
    {
        return mMetaData;
    }


    public void setMetaData(Bundle metaData)
    {
        mMetaData = metaData;
    }
}
