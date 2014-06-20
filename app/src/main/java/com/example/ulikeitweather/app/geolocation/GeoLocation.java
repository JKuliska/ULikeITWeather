package com.example.ulikeitweather.app.geolocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.MyLocation;

/**
 * obtains the location if it is available
 */

public class GeoLocation implements LocationListener {

    public static boolean dontAskToTurnOnGPS = false; //flag so that the dialog prompting to turn on GPS does not pop up too often

    private Context mContext;
    private MyLocation myLocation = new MyLocation();

    private boolean canGetLocation = false;


    public GeoLocation(Context context) {
        mContext = context;
        getLoc();
    }


    public boolean canGetLocation() {
        return canGetLocation;
    }


    public MyLocation getMyLocation() {
        return myLocation;
    }

    /*
    creates a dialog to prompt a user to turn on GPS for the case the location is not available
     */

    private void geoLocDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                        getLoc();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.toast_weather_not_displayed), Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.dialog_geoloc_title))
                .setMessage(mContext.getResources().getString(R.string.dialog_geoloc_message))
                .setPositiveButton(mContext.getResources().getString(R.string.dialog_yes), dialogClickListener)
                .setNegativeButton(mContext.getResources().getString(R.string.dialog_no), dialogClickListener)
                .show();
    }

    /*
    obtains the location based on the best available provider
     */
    private void getLoc() {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String mProvider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(mProvider);

        // Initialize the location fields
        if (location != null) {
            canGetLocation = true;
            myLocation.setLongitude(location.getLongitude());
            myLocation.setLatitude(location.getLatitude());
        } else {
            canGetLocation = false;
            if(!dontAskToTurnOnGPS) {
                geoLocDialog();
                dontAskToTurnOnGPS = true;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) { }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }


    @Override
    public void onProviderEnabled(String provider) { }


    @Override
    public void onProviderDisabled(String provider) { }

}
