package com.example.ulikeitweather.app.client.parser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public class Parser
{
    protected static void handleUnknownParameter(JsonParser parser) throws IOException, JsonParseException
    {
        if(parser.getCurrentToken() == JsonToken.START_OBJECT)
            while(parser.nextToken() != JsonToken.END_OBJECT)
            {
                handleUnknownParameter(parser);
            }

        if(parser.getCurrentToken() == JsonToken.START_ARRAY)
            while(parser.nextToken() != JsonToken.END_ARRAY)
            {
                handleUnknownParameter(parser);
            }
    }
}