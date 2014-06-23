package com.example.ulikeitweather.app.client.parser;

import android.content.Context;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.Weather;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * parses the raw downloaded text file containing weather information into entities
 */

public class WeatherParser {

    private static final int FORECAST_DAYS = 4;

    private Context mContext;
    private Weather[] mWeatherForecast; //4 days of forecast
    private Weather mWeatherToday; //one current day
    private String[] mCsvString; //string array containing all 5 days in CSV format


    public Weather getWeatherToday() {
        return mWeatherToday;
    }


    public Weather[] getWeatherForecast() {
        return mWeatherForecast;
    }


    public WeatherParser(Context context, String file) {
        mContext = context;
        mCsvString = getStringFromFile(file);
        getToday();
        getFourDayForecast();
    }


    /*
    Extracts day weather information from the downloaded csv file
    @params: file is the whole string downloaded from website
     @return: 5-item array, each item containing weather information about one day
     */
    private String[] getStringFromFile(String file) {
        StringBuilder mStrBuilder = new StringBuilder();
        try {
            FileInputStream fIn = mContext.openFileInput(file);
            int ch;
            while((ch = fIn.read()) != -1) {
                mStrBuilder.append((char)ch);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] mSplitByHash = mStrBuilder.toString().split("#");
        String[] mSplitByNewline = mSplitByHash[mSplitByHash.length - 1].split("\n");
        String[] mRemovedFirstEmptyLine = new String[mSplitByNewline.length - 1];
        for(int i = 1; i < mSplitByNewline.length ; i++) {
            mRemovedFirstEmptyLine[i-1] = mSplitByNewline[i];
        }
        return mRemovedFirstEmptyLine;
    }

    /*
    gives a name of the day of the week based on a date
    @params: date in a format YYYY-MM-DD
    @return: name of a day of the week
     */
    private String getDayOfWeek(String date) {
        SimpleDateFormat mSdf = new SimpleDateFormat("EEEE");
        Calendar mCalender = Calendar.getInstance();
        String[] calStr = date.split("-");
        mCalender.set(Integer.parseInt(calStr[0]), Integer.parseInt(calStr[1])-1, Integer.parseInt(calStr[2]));
        int dayOfTheWeek = mCalender.get(Calendar.DAY_OF_WEEK);
        switch (dayOfTheWeek) {
            case Calendar.MONDAY: return mContext.getResources().getString(R.string.global_monday);
            case Calendar.TUESDAY: return mContext.getResources().getString(R.string.global_tuesday);
            case Calendar.WEDNESDAY: return mContext.getResources().getString(R.string.global_wednesday);
            case Calendar.THURSDAY: return mContext.getResources().getString(R.string.global_thursday);
            case Calendar.FRIDAY: return mContext.getResources().getString(R.string.global_friday);
            case Calendar.SATURDAY: return mContext.getResources().getString(R.string.global_saturday);
            case Calendar.SUNDAY: return mContext.getResources().getString(R.string.global_sunday);
            default: return "";
        }
    }

    /*
    gets the forecast for the next 4 days and stores it in a mWeatherForecast array
     */
    private void getFourDayForecast() {
        mWeatherForecast = new Weather[FORECAST_DAYS];
        for(int i = 0; i < FORECAST_DAYS; i++) {
            String[] strOneDayArray = mCsvString[i+1].split(",");
            mWeatherForecast[i] = new Weather();
            mWeatherForecast[i].setTempC(strOneDayArray[1]);
            mWeatherForecast[i].setTempF(strOneDayArray[2]);
            mWeatherForecast[i].setImgUrl(strOneDayArray[10]);
            mWeatherForecast[i].setDescription(strOneDayArray[11]);
            mWeatherForecast[i].setDayOfWeek(getDayOfWeek(strOneDayArray[0]));
        }
    }

    /*
     gets the current weather for today and stores it in a variable mWeatherToday
     */
    private void getToday() {
        mWeatherToday = new Weather();
        String[] strTodayArray = mCsvString[0].split(",");
        mWeatherToday.setTempC(strTodayArray[1]);
        mWeatherToday.setTempF(convertCelsiusToFahrenheit(Integer.parseInt(strTodayArray[1])) + "");
        mWeatherToday.setImgUrl(strTodayArray[3]);
        mWeatherToday.setDescription(strTodayArray[4]);
        mWeatherToday.setWindSpeedMph(strTodayArray[5]);
        mWeatherToday.setWindSpeedKmh(strTodayArray[6]);
        mWeatherToday.setWindDir(strTodayArray[8]);
        mWeatherToday.setPrecip(strTodayArray[9]);
        mWeatherToday.setHumidity(strTodayArray[10]);
        mWeatherToday.setPressure(strTodayArray[12]);
    }


    /*
    converts celsius to fahrenheit
    @params: integer value of celsius
    @return: according value of temperature in fahrenheit
     */
    private int convertCelsiusToFahrenheit(int celsius) {
        return ((celsius * 9) / 5) + 32;
    }
}
