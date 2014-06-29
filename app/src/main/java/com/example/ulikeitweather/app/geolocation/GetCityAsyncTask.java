package com.example.ulikeitweather.app.geolocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.MyLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * gets the name of the city based on the location (longitude, latitude)
 */

public class GetCityAsyncTask extends AsyncTask<MyLocation, Void, String> {

    private Context mContext;
    private OnCityLoadedListener mCityLoadedListener;

    public GetCityAsyncTask(Context context, OnCityLoadedListener listener) {
        super();
        mContext = context;
        mCityLoadedListener = listener;
    }

    /**
     * Get a Geocoder instance, get the latitude and longitude
     * look up the address, and return it
     *
     * @return A string containing the address of the current
     * location, or an empty string if no address can be found,
     * or an error message
     * @params params One or more MyLocation objects
     */
    @Override
    protected String doInBackground(MyLocation... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        // Get the current location from the input parameter list
        MyLocation loc = params[0];
        // Create a list to contain the result address
        List<Address> addresses;
        try {
            // Return 1 address.
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e1) {
            Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
            e1.printStackTrace();
            return mContext.getResources().getString(R.string.global_unspecified_location);
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments " + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) + " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            e2.printStackTrace();
            return mContext.getResources().getString(R.string.global_unspecified_location);
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);

            //format and return the city and country text
            return String.format("%s, %s",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName()
            );
        } else {
            return mContext.getResources().getString(R.string.global_unspecified_location);
        }
    }


    @Override
    protected void onPostExecute(String city) {
        mCityLoadedListener.OnCityLoaded(city);
    }
}

