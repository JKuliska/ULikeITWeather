package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.ForecastArrayAdapter;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.utility.Logcat;

import java.util.ArrayList;
import java.util.List;

public class ForecastFragment extends WeatherParentFragment {

    private List<Weather> mForecastList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        return mRootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.ac_forecast));
        if(mForecastAdapter != null) {
            mForecastAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void renderCityTextView() { }

    public void renderView()
    {
        mForecastList = new ArrayList<Weather>();
        for(int i=1; i<mWeatherList.size(); i++) {
            mForecastList.add(i-1, mWeatherList.get(i));
        }

        mForecastAdapter = new ForecastArrayAdapter(getActivity(), mForecastList, mOrientationIsPortrait, mDisplayImageOptions, mImageLoader);
        if(mOrientationIsPortrait) {
            ListView forecastListView = (ListView) getActivity().findViewById(R.id.forecast_list);
            forecastListView.setAdapter(mForecastAdapter);
        } else {
            GridView forecastListView = (GridView) getActivity().findViewById(R.id.forecast_grid);
            forecastListView.setAdapter(mForecastAdapter);
        }

    }


}
