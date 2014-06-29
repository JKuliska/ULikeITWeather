package com.example.ulikeitweather.app.client.request;
import android.content.Context;

import com.example.ulikeitweather.app.client.parser.WeatherJsonParser;
import com.example.ulikeitweather.app.client.response.Response;
import com.example.ulikeitweather.app.entity.MyLocation;
import com.example.ulikeitweather.app.entity.Weather;
import com.fasterxml.jackson.core.JsonParseException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;


public class WeatherRequest extends Request
{
    private static final String REQUEST_METHOD = "POST";
    private static final String KEY = "979fe88c1bedef59ad9bff0f8e3d7af90bf81f53";
    private static final String URL_PREFIX = "http://api.worldweatheronline.com/free/v1/weather.ashx?key=";
    private static final String QUERY_LOCATION = "&q=";
    private static final String QUERY_NUM_OF_DAYS = "&num_of_days=4";
    private static final String QUERY_FORMAT_CSV = "&format=json";

    private MyLocation mLocation;
    private Context mContext;


    public WeatherRequest(MyLocation location, Context context)
    {
        mLocation = location;
        mContext = context;
    }


    @Override
    public String getRequestMethod()
    {
        return REQUEST_METHOD;
    }


    @Override
    public String getAddress()
    {
        return URL_PREFIX + KEY + QUERY_LOCATION + mLocation.getLatitude() + "," + mLocation.getLongitude() + QUERY_NUM_OF_DAYS + QUERY_FORMAT_CSV;
    }


    @Override
    public Response<List<Weather>> parseResponse(InputStream stream) throws IOException, JsonParseException
    {
        return WeatherJsonParser.parse(stream, mContext);
    }


    @Override
    public byte[] getContent()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("content");

        try
        {
            return builder.toString().getBytes(CHARSET);
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String getBasicAuthUsername()
    {
        return "myusername";
    }


    @Override
    public String getBasicAuthPassword()
    {
        return "mypassword";
    }
}
