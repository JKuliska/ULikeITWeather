package com.example.ulikeitweather.app.client.parser;

import android.content.Context;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.utility.Logcat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

/**
 * parses the raw downloaded text file containing weather information into entities
 */

public class WeatherParser {

    private static final int FORECAST_DAYS = 4;
    private static final String JSON_MAIN_NODE_NAME = "data";
    private static final String JSON_CURRENT_CONDITION = "current_condition";
    private static final String JSON_TEMP_C = "temp_C";
    private static final String JSON_TEMP_F = "temp_F";
    private static final String JSON_TEMP_MAX_C = "tempMaxC";
    private static final String JSON_TEMP_MAX_F = "tempMaxF";
    private static final String JSON_IMG_URL = "weatherIconUrl";
    private static final String JSON_DESCRIPTION = "weatherDesc";
    private static final String JSON_WIND_KPH = "windspeedKmph";
    private static final String JSON_WIND_MPH = "windspeedMiles";
    private static final String JSON_WIND_DIR = "winddir16Point";
    private static final String JSON_PRECIPITATION = "precipMM";
    private static final String JSON_HUMIDITY = "humidity";
    private static final String JSON_PRESSURE = "pressure";
    private static final String JSON_VALUE = "value";
    private static final String JSON_DATE = "date";
    private static final String JSON_WEATHER = "weather";

    private Context mContext;
    private Weather[] mWeatherForecast; //4 days of forecast
    private Weather mWeatherToday; //one current day


    public Weather getWeatherToday() {
        return mWeatherToday;
    }


    public Weather[] getWeatherForecast() {
        return mWeatherForecast;
    }


    public WeatherParser(Context context, String file) {
        mContext = context;
        JSONObject jsonObject = getJsonFromFile(file);
        mWeatherToday = getToday(jsonObject);
        mWeatherForecast = getFourDayForecast(jsonObject);
    }


    /*
    Extracts day weather information from the downloaded csv file
    @params: file is the whole string downloaded from website
     @return: Json object containing the weather information or null if the parsing was not successful due to JsonException
     */
    private JSONObject getJsonFromFile(String file) {
        StringBuilder mStrBuilder = new StringBuilder();
        try {
            FileInputStream fIn = mContext.openFileInput(file);
            int ch;
            while ((ch = fIn.read()) != -1) {
                mStrBuilder.append((char) ch);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(mStrBuilder.toString());
            return jsonResponse;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    gives a name of the day of the week based on a date
    @params: date in a format YYYY-MM-DD
    @return: name of a day of the week
     */
    private String getDayOfWeek(String date) {
        Calendar mCalender = Calendar.getInstance();
        String[] calStr = date.split("-");
        mCalender.set(Integer.parseInt(calStr[0]), Integer.parseInt(calStr[1]) - 1, Integer.parseInt(calStr[2]));
        int dayOfTheWeek = mCalender.get(Calendar.DAY_OF_WEEK);
        switch (dayOfTheWeek) {
            case Calendar.MONDAY:
                return mContext.getResources().getString(R.string.global_monday);
            case Calendar.TUESDAY:
                return mContext.getResources().getString(R.string.global_tuesday);
            case Calendar.WEDNESDAY:
                return mContext.getResources().getString(R.string.global_wednesday);
            case Calendar.THURSDAY:
                return mContext.getResources().getString(R.string.global_thursday);
            case Calendar.FRIDAY:
                return mContext.getResources().getString(R.string.global_friday);
            case Calendar.SATURDAY:
                return mContext.getResources().getString(R.string.global_saturday);
            case Calendar.SUNDAY:
                return mContext.getResources().getString(R.string.global_sunday);
            default:
                return "";
        }
    }

    /*
    gets the forecast for the next 4 days and stores it in a mWeatherForecast array
     */
    private Weather[] getFourDayForecast(JSONObject jsonObject) {
        Weather[] weatherForecast = new Weather[FORECAST_DAYS];

        JSONObject jsonMainNode = jsonObject.optJSONObject(JSON_MAIN_NODE_NAME);
        try {
            JSONArray jsonArray = jsonMainNode.getJSONArray(JSON_WEATHER);

            int lengthJsonArray = jsonArray.length();

            for(int i = 0; i < lengthJsonArray; i++) {

                JSONObject jsonChildNode = jsonArray.getJSONObject(i);

                weatherForecast[i] = new Weather();
                weatherForecast[i].setTempC(jsonChildNode.optString(JSON_TEMP_MAX_C));
                weatherForecast[i].setTempF(jsonChildNode.optString(JSON_TEMP_MAX_F));
                weatherForecast[i].setDayOfWeek(getDayOfWeek(jsonChildNode.optString(JSON_DATE)));


                JSONArray jsonChildArray = jsonChildNode.getJSONArray(JSON_DESCRIPTION);
                weatherForecast[i].setDescription(jsonChildArray.getJSONObject(0).optString(JSON_VALUE));
                jsonChildArray = jsonChildNode.getJSONArray(JSON_IMG_URL);
                weatherForecast[i].setImgUrl(jsonChildArray.getJSONObject(0).optString(JSON_VALUE));
            }

            return weatherForecast;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     gets the current weather for today and stores it in a variable mWeatherToday
     */
    private Weather getToday(JSONObject jsonObject) {
        Weather weatherToday = new Weather();
        if(jsonObject != null) {
            JSONObject jsonMainNode = jsonObject.optJSONObject(JSON_MAIN_NODE_NAME);
            try {
                JSONArray jsonArray = jsonMainNode.getJSONArray(JSON_CURRENT_CONDITION);
                JSONObject jsonChildNode = jsonArray.getJSONObject(0);

                weatherToday.setTempC(jsonChildNode.optString(JSON_TEMP_C));
                weatherToday.setTempF(jsonChildNode.optString(JSON_TEMP_F));
                weatherToday.setWindSpeedMph(jsonChildNode.optString(JSON_WIND_MPH));
                weatherToday.setWindSpeedKmh(jsonChildNode.optString(JSON_WIND_KPH));
                weatherToday.setWindDir(jsonChildNode.optString(JSON_WIND_DIR));
                weatherToday.setPrecip(jsonChildNode.optString(JSON_PRECIPITATION));
                weatherToday.setHumidity(jsonChildNode.optString(JSON_HUMIDITY));
                weatherToday.setPressure(jsonChildNode.optString(JSON_PRESSURE));

                JSONArray jsonChildArray = jsonChildNode.getJSONArray(JSON_IMG_URL);
                weatherToday.setImgUrl(jsonChildArray.getJSONObject(0).optString(JSON_VALUE));
                jsonChildArray = jsonChildNode.getJSONArray(JSON_DESCRIPTION);
                weatherToday.setDescription(jsonChildArray.getJSONObject(0).optString(JSON_VALUE));

                return weatherToday;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
