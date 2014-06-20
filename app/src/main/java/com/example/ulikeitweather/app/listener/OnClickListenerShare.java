package com.example.ulikeitweather.app.listener;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.dialog.ShareDialog;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.fragment.SettingsFragment;
/*
listens for screen click to show the share dialog
 */

public class OnClickListenerShare implements View.OnClickListener {

    private FragmentManager mFragManager;
    private Weather mWeather;
    private Context mContext;

    public OnClickListenerShare(FragmentManager fragManager, Weather weather, Context context) {
        mFragManager = fragManager;
        mWeather = weather;
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        ShareDialog dialog = ShareDialog.newInstance(buildShareMessage());
        dialog.show(mFragManager, "");
    }


    /*
    creates a message that is to be shared
     */
    private String buildShareMessage() {
        StringBuilder mStrBuilder = new StringBuilder();
        mStrBuilder.append((mWeather.getmDayOfWeek() == null ? mContext.getResources().getString(R.string.global_today) : mWeather.getmDayOfWeek()) + ": ");
        mStrBuilder.append((SettingsFragment.useCelsius ? mWeather.getmTempC() + mContext.getResources().getString(R.string.global_celsius_abbr)
                                                        : mWeather.getmTempF() + mContext.getResources().getString(R.string.global_celsius_abbr) + ", "));
        mStrBuilder.append(mWeather.getmDescription());
        return mStrBuilder.toString();
    }
}