package com.example.ulikeitweather.app.client.parser;

import android.content.Context;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.client.response.Response;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.utility.Logcat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Asus on 26.6.2014.
 */
public class WeatherJsonParser extends Parser {

    private static final String JSON_MAIN_NODE_NAME = "data";
    private static final String JSON_CURRENT_CONDITION = "current_condition";
    private static final String JSON_REQUEST = "request";
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
    private static final String JSON_ERROR = "error";

    private static JsonParser parser;

    public static Response<List<Weather>> parse(InputStream stream, Context context) throws IOException, JsonParseException {
        Response<List<Weather>> response = null;
        List<Weather> weatherList = new ArrayList<Weather>();

        // init parser
        JsonFactory factory = new JsonFactory();
        parser = factory.createParser(stream);

        // parse JSON
        if (parser.nextToken() == JsonToken.START_OBJECT) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {

                // error
                if (parser.getCurrentName().equals(JSON_ERROR)) {
                    String type = null;
                    String message = null;

                    if (parser.nextToken() == JsonToken.START_OBJECT) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            if (parser.getCurrentName().equals("type")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                    type = parser.getText();
                            } else if (parser.getCurrentName().equals("message")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                    message = parser.getText();
                            }
                        }
                    }

                    response = new Response<List<Weather>>();
                    response.setError(true);
                    response.setErrorType(type);
                    response.setErrorMessage(message);
                }

                // response
                else if (parser.getCurrentName().equals(JSON_MAIN_NODE_NAME)) { //data node

                    Weather weather;

                    if (parser.nextToken() == JsonToken.START_OBJECT) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            weather = new Weather();
                            if (parser.getCurrentName().equals(JSON_CURRENT_CONDITION)) {   //current condition node
                                if (parser.nextToken() == JsonToken.START_ARRAY) {    //array within current condition node
                                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                                        if (parser.getCurrentToken() == JsonToken.START_OBJECT) {      //start of the first object in array 'current condition'
                                            while (parser.nextToken() != JsonToken.END_OBJECT) { //get values for current condition
                                                if (parser.getCurrentName().equals(JSON_TEMP_C)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setTempC(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_TEMP_F)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setTempF(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_WIND_MPH)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setWindSpeedMph(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_WIND_KPH)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setWindSpeedKmh(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_WIND_DIR)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setWindDir(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_PRECIPITATION)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setPrecip(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_HUMIDITY)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setHumidity(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_PRESSURE)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setPressure(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_IMG_URL)) {
                                                    weather.setImgUrl(getJsonValue());
                                                } else if (parser.getCurrentName().equals(JSON_DESCRIPTION)) {
                                                    weather.setDescription(getJsonValue());
                                                } else {
                                                    handleUnknownParameter(parser);
                                                }
                                            }
                                        }
                                    }
                                    weatherList.add(0, weather);
                                }

                            } else if(parser.getCurrentName().equals(JSON_REQUEST)) {
                                if (parser.nextToken() == JsonToken.START_ARRAY) {    //array within request node
                                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                                        if (parser.getCurrentToken() == JsonToken.START_OBJECT) {      //start of the first object in array 'request'
                                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                                //just to jump over tokens that are in request branch
                                            }
                                        }
                                    }
                                }
                            } else if (parser.getCurrentName().equals(JSON_WEATHER)) { //forecast node
                                if (parser.nextToken() == JsonToken.START_ARRAY) {    //array within current condition node
                                    int i = 0; //position in the list
                                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                                        if (parser.getCurrentToken() == JsonToken.START_OBJECT) {      //start of the first object in array 'current condition'
                                            weather = new Weather();
                                            while (parser.nextToken() != JsonToken.END_OBJECT) { //get values for current condition
                                                if (parser.getCurrentName().equals(JSON_TEMP_MAX_C)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setTempC(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_TEMP_MAX_F)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setTempF(parser.getText());
                                                } else if (parser.getCurrentName().equals(JSON_DATE)) {
                                                    if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                                        weather.setDayOfWeek(getDayOfWeek(parser.getText(), context));
                                                } else if (parser.getCurrentName().equals(JSON_IMG_URL)) {
                                                    weather.setImgUrl(getJsonValue());
                                                } else if (parser.getCurrentName().equals(JSON_DESCRIPTION)) {
                                                    weather.setDescription(getJsonValue());
                                                } else {
                                                    handleUnknownParameter(parser);
                                                }
                                            }

                                            weatherList.add(++i ,weather);

                                        }
                                    }

                                }

                            } else {
                                // unknown parameter
                                handleUnknownParameter(parser);
                            }
                        }
                    }
                }
            }
            response = new Response<List<Weather>>();
            response.setResponseObject(weatherList);
        }

        // close parser
        if (parser != null) parser.close();

        return response;
    }

    /*
    gets those values of json file that are not accessible directly but are in an array within a node
     returns the last obtained value from the array (normally there should be only one value)
     */
    private static String getJsonValue() throws IOException {
        String value = "";

        if (parser.nextToken() == JsonToken.START_ARRAY) {
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                if (parser.getCurrentToken() == JsonToken.START_OBJECT) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        if (parser.getCurrentName().equals(JSON_VALUE)) {
                            if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                value = parser.getText();
                        }
                    }
                }
            }
        }

        return value;
    }

    /*
    gives a name of the day of the week based on a date
    @params: date in a format YYYY-MM-DD
    @return: name of a day of the week
     */
    private static String getDayOfWeek(String date, Context context) {
        Calendar mCalender = Calendar.getInstance();
        String[] calStr = date.split("-");
        mCalender.set(Integer.parseInt(calStr[0]), Integer.parseInt(calStr[1]) - 1, Integer.parseInt(calStr[2]));
        int dayOfTheWeek = mCalender.get(Calendar.DAY_OF_WEEK);
        switch (dayOfTheWeek) {
            case Calendar.MONDAY:
                return context.getResources().getString(R.string.global_monday);
            case Calendar.TUESDAY:
                return context.getResources().getString(R.string.global_tuesday);
            case Calendar.WEDNESDAY:
                return context.getResources().getString(R.string.global_wednesday);
            case Calendar.THURSDAY:
                return context.getResources().getString(R.string.global_thursday);
            case Calendar.FRIDAY:
                return context.getResources().getString(R.string.global_friday);
            case Calendar.SATURDAY:
                return context.getResources().getString(R.string.global_saturday);
            case Calendar.SUNDAY:
                return context.getResources().getString(R.string.global_sunday);
            default:
                return "";
        }
    }
}

