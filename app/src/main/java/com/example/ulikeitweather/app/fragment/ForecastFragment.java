package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.ForecastArrayAdapter;
import com.example.ulikeitweather.app.dialog.ShareDialog;
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
            forecastListView.setOnItemClickListener(new OnItemClickShareListener());
        } else {
            GridView forecastGridView = (GridView) getActivity().findViewById(R.id.forecast_grid);
            forecastGridView.setAdapter(mForecastAdapter);
            forecastGridView.setOnItemClickListener(new OnItemClickShareListener());
        }
    }

    private class OnItemClickShareListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShareDialog dialog = ShareDialog.newInstance(buildShareMessage(mForecastList.get(position)));
            dialog.show(getFragmentManager(), "");
        }
    }
}
