package com.example.ulikeitweather.app.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.ulikeitweather.app.entity.MyLocation;
import com.example.ulikeitweather.app.listener.OnWeatherDownloadedListener;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * downloading the weather from a specified url and saving it to internal storage
 */

public class DownloadWeather extends AsyncTask<MyLocation, Void, String> {

    public static final String DOWNLOADED_CSV_FILE = "weather.csv";

    private static final String KEY = "979fe88c1bedef59ad9bff0f8e3d7af90bf81f53";
    private static final String URL_PREFIX = "http://api.worldweatheronline.com/free/v1/weather.ashx?key=";
    private static final String QUERY_LOCATION = "&q=";
    private static final String QUERY_NUM_OF_DAYS = "&num_of_days=4";
    private static final String QUERY_FORMAT_CSV = "&format=csv";
    private Context mContext;
    private OnWeatherDownloadedListener mWeatherDownloadedListener = null;


    public DownloadWeather(Context context, OnWeatherDownloadedListener weatherDownloadedListener) {
        this.mContext = context;
        this.mWeatherDownloadedListener = weatherDownloadedListener;
    }


    @Override
    protected String doInBackground(MyLocation... params) {
        String mFile = null;
        if (isConnectedToInternet()) {
            mFile = downloadFile(params[0]);
        }
        return mFile;
    }


    @Override
    protected void onPostExecute(String file) {
        mWeatherDownloadedListener.OnWeatherDownloaded(file);
    }


    /*
     downloads file with weather information
     @params: location for which the weather should be downloaded - longitude, latitude
     @return: name of the file into which the weather information was written to
     */
    private String downloadFile(MyLocation location) {
        String mFileName;
        URL mUrl = null;

        mFileName = DOWNLOADED_CSV_FILE;
        try {
            mUrl = new URL(urlBuilder(location));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        try {
            // Open a connection to that URL.
            URLConnection ucon = null;
            ucon = mUrl.openConnection();


            //Define InputStreams to read from the URLConnection.
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            //Read bytes to the Buffer until there is nothing more to read(-1).
            ByteArrayBuffer baf = new ByteArrayBuffer(5000);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            //Convert the bytes to String
            FileOutputStream fos = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            fos.write(baf.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mFileName;
    }


    /*
    builds the url request based on the location (longitude, latitude)
    @params: location containing longitute and latitude
    @return: string representing url from which the weather can be downloaded
     */

    private String urlBuilder(MyLocation location) {
        return URL_PREFIX + KEY + QUERY_LOCATION + location.getLatitude() + "," + location.getLongitude() + QUERY_NUM_OF_DAYS + QUERY_FORMAT_CSV;
    }


    /*
    checks if a user is connected to the internet
    @return: true if user is connected and false is he is not
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
}
