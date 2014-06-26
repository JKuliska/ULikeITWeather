package com.example.ulikeitweather.app.client.parser;

import android.content.Context;

import com.example.ulikeitweather.app.client.response.Response;
import com.example.ulikeitweather.app.entity.Weather;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 26.6.2014.
 */
public class WeatherJsonParser extends Parser {

    public static Response<List<Weather>> parse(InputStream stream) throws IOException, JsonParseException {
        Response<List<Weather>> response = null;


        // init parser
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        parser = factory.createJsonParser(stream);


        // parse JSON
        if (parser.nextToken() == JsonToken.START_OBJECT)
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                // error
                if (parser.getCurrentName().equals("error")) {
                    String type = null;
                    String message = null;

                    if (parser.nextToken() == JsonToken.START_OBJECT)
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            if (parser.getCurrentName().equals("type")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                    type = parser.getText();
                            } else if (parser.getCurrentName().equals("message")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                    message = parser.getText();
                            }
                        }

                    response = new Response<List<Weather>>();
                    response.setError(true);
                    response.setErrorType(type);
                    response.setErrorMessage(message);
                }

                // response
                else if (parser.getCurrentName().equals("product")) {
                    long id = -1l;
                    String name = null;

                    if (parser.nextToken() == JsonToken.START_OBJECT)
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            if (parser.getCurrentName().equals("id")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_NUMBER_INT)
                                    id = parser.getLongValue();
                            } else if (parser.getCurrentName().equals("name")) {
                                if (parser.getCurrentToken() == JsonToken.VALUE_STRING)
                                    name = parser.getText();
                            } else {
                                // unknown parameter
                                handleUnknownParameter(parser);
                            }
                        }

                    Weather weather = new Weather();
                    weather.setId(id);
                    weather.setName(name);

                    List<Weather> weatherList = new ArrayList<Weather>();
                    weatherList.add(weather);

                    response = new Response<List<Weather>>();
                    response.setResponseObject(weatherList);
                }
            }


        // close parser
        if (parser != null) parser.close();
        return response;
    }
}

