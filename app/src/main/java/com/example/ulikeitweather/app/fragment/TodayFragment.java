package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.listener.AnimateImageLoadingListener;
import com.example.ulikeitweather.app.utility.Logcat;
import com.example.ulikeitweather.app.utility.MySharedPrefs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TodayFragment extends WeatherParentFragment {

    private Weather mWeather = null;
    private ImageLoadingListener mImageLoadingListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_today, container, false);
        return mRootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getResources().getString(R.string.ac_today));
        TextView mDescriptionTxt = (TextView) getActivity().findViewById(R.id.txt_temperature);
        if(mDescriptionTxt != null && mWeather != null) {
            mDescriptionTxt.setText(buildDescriptionText(new MySharedPrefs(getActivity())));
        }
    }

    @Override
    protected void renderCityTextView() {
        TextView mCityTxt = (TextView) getActivity().findViewById(R.id.txt_location);
        Logcat.i("renderCityTextView", "1");
        if(mCityTxt != null) {
            Logcat.i("renderCityTextView", "2");
            mCityTxt.setText(mLocation.getCity());
        }
    }

    public void renderView()
    {
        mWeather = mWeatherList.get(0);
        LinearLayout[] mLyt = new LinearLayout[5];
        mLyt[0] = (LinearLayout) getActivity().findViewById(R.id.lyt_humidity);
        mLyt[1] = (LinearLayout) getActivity().findViewById(R.id.lyt_precipitation);
        mLyt[2] = (LinearLayout) getActivity().findViewById(R.id.lyt_pressure);
        mLyt[3] = (LinearLayout) getActivity().findViewById(R.id.lyt_wind_speed);
        mLyt[4] = (LinearLayout) getActivity().findViewById(R.id.lyt_wind_direction);
        ImageView[] mImg = new ImageView[5];
        TextView[] mText = new TextView[5];
        for(int i = 0; i < mLyt.length; i++) {
            mImg[i] = (ImageView) mLyt[i].findViewById(R.id.img_icon);
            mText[i] = (TextView) mLyt[i].findViewById(R.id.txt_icon);
        }
        MySharedPrefs prefs = new MySharedPrefs(getActivity());
        mImg[0].setImageResource(R.drawable.ic_humidity);
        mText[0].setText(mWeather.getHumidity() + getActivity().getResources().getString(R.string.global_percent_abbr));
        mImg[1].setImageResource(R.drawable.ic_precipitation);
        mText[1].setText(mWeather.getPrecip() + getActivity().getResources().getString(R.string.global_milimeter_abbr));
        mImg[2].setImageResource(R.drawable.ic_pressure);
        mText[2].setText(mWeather.getPressure() + getActivity().getResources().getString(R.string.global_hpa_abbr));
        mImg[3].setImageResource(R.drawable.ic_wind_speed);
        mText[3].setText(prefs.isKilometer() ? mWeather.getWindSpeedKmh() + getActivity().getResources().getString(R.string.global_kph_abbr)
                : mWeather.getWindSpeedMph() + getActivity().getResources().getString(R.string.global_mph_abbr));
        mImg[4].setImageResource(R.drawable.ic_wind_direction);
        mText[4].setText(mWeather.getWindDir());

        TextView mDescriptionTxt = (TextView) getActivity().findViewById(R.id.txt_temperature);
        mDescriptionTxt.setText(buildDescriptionText(prefs));


        // reference
        ImageView photoImageView = (ImageView) mRootView.findViewById(R.id.img_weather_state);

        // image caching
        mImageLoader.displayImage(mWeather.getImgUrl(), photoImageView, mDisplayImageOptions, mImageLoadingListener);
    }

    private String buildDescriptionText(MySharedPrefs prefs) {
        return (prefs.isCelsius() ? mWeather.getTempC() + getActivity().getResources().getString(R.string.global_celsius_abbr)
                : mWeather.getTempF() + getActivity().getResources().getString(R.string.global_fahrenheit_abbr))
                + " | " + mWeather.getDescription();
    }


}
