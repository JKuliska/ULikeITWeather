package com.example.ulikeitweather.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.geolocation.GeoLocation;
import com.example.ulikeitweather.app.entity.MyLocation;
import com.example.ulikeitweather.app.listener.OnIconDownloadedListener;
import com.example.ulikeitweather.app.listener.OnWeatherDownloadedListener;
import com.example.ulikeitweather.app.task.DownloadWeather;
import com.example.ulikeitweather.app.utility.Logcat;

/**
 * parent class for both Weather fragments implementing listeners for finished download events
 */

public abstract class WeatherFragment extends Fragment implements OnWeatherDownloadedListener, OnIconDownloadedListener {

    protected MyLocation myLocation = new MyLocation();
    protected boolean canGetLocation = false;
    protected boolean isPortrait = true;

    public abstract void loadView();

    /*
    if it is possible, it gets the location current location of the user and downloads the according weather
     */

    public void getLocationAndWeather() {
        GeoLocation mGeoLoc = new GeoLocation(getActivity());
        if(mGeoLoc.canGetLocation()) {
            canGetLocation = true;
            myLocation = mGeoLoc.getMyLocation();
            DownloadWeather mDownloadedWeather = new DownloadWeather(getActivity(), this);
            mDownloadedWeather.execute(myLocation);
        } else {
            canGetLocation = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadView();
    }

    /*
    shows dialog asking user if they want to turn on the Internet so that the weather could be downloaded
     */
    protected void showInternetDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(getActivity().getResources().getString(R.string.dialog_no_internet));
        alertBuilder.setMessage(getActivity().getResources().getString(R.string.dialog_no_internet_message));
        alertBuilder.setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                getActivity().startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
