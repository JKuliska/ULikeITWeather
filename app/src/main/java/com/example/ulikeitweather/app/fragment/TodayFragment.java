package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ulikeitweather.app.R;

public class TodayFragment extends WeatherParentFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_today, container, false);
        return mRootView;
    }

    public void renderView()
    {
        // TODO
    }
}