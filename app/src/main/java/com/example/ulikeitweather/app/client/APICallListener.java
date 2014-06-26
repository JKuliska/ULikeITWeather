package com.example.ulikeitweather.app.client;

import com.example.ulikeitweather.app.client.response.Response;

public interface APICallListener
{
    public void onAPICallRespond(APICallTask task, ResponseStatus status, Response<?> response);
    public void onAPICallFail(APICallTask task, ResponseStatus status, Exception exception);
}