package com.example.ulikeitweather.app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.client.parser.WeatherParser;
import com.example.ulikeitweather.app.utility.MySharedPrefs;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.listener.OnClickListenerShare;
import com.example.ulikeitweather.app.task.DownloadIconTask;

public class WeatherForecastFragment extends WeatherFragment {

    private Weather[] mForecast;
    private Bitmap[] mIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_forecast, container, false);
    }


    /*
    actions taken when the orientation is changed
     */
    public void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        inflater.inflate(R.layout.fragment_weather_forecast, viewGroup);
        loadViewSecondary();
        setupListener();
    }


    /*
    sets up share listeners for all days
     */
    private void setupListener() {
        RelativeLayout[] mLayoutItem = new RelativeLayout[4];
        mLayoutItem[0] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day1);
        mLayoutItem[1] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day2);
        mLayoutItem[2] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day3);
        mLayoutItem[3] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day4);
        for(int i = 0; i<mLayoutItem.length; i++) {
            LinearLayout mLinLyt = (LinearLayout) mLayoutItem[i].findViewById(R.id.lyt_day_full);
            mLinLyt.setOnClickListener(new OnClickListenerShare(getFragmentManager(), mForecast[i], getActivity()));
        }
    }


    public void loadView() {
        makeIconsInvisible();
        getLocationAndWeather();
    }

    /*
     makes the icons invisible before refresh so that the animation is again visible when loaded
     */
    private void makeIconsInvisible() {
        RelativeLayout[] mRelLyt = getContainterLayouts();
        for (RelativeLayout aMRelLyt : mRelLyt) {
            LinearLayout mLinLyt = (LinearLayout) aMRelLyt.findViewById(R.id.lyt_weather_state_container);
            mLinLyt.setVisibility(View.INVISIBLE);
        }
    }

    /*
    loads the stored weather information, does not download it again
     */
    public void loadViewSecondary() {
        if(mForecast != null)
            loadViewDetails();
        if(mIcon != null) {
            RelativeLayout mLayoutItem[] = getContainterLayouts();
            for(int i = 0; i < mLayoutItem.length; i++) {
                iconFlyInAnimation(i, mLayoutItem[i]);
            }
        }

    }


    /*
    called when the weather is downloaded and tries to parse it and download icons
     */
    @Override
    public void OnWeatherDownloaded(String file) {
        if(file == null) {
            showInternetDialog();
        } else {
            WeatherParser mWeatherParser = new WeatherParser(getActivity(), file);
            mForecast = mWeatherParser.getWeatherForecast();
            downloadIcons();
            loadViewDetails();
            setupListener();
        }
    }

    /*
    runs task to download icons
     */

    private void downloadIcons() {
        for(int i =0; i < mForecast.length; i++) {
            new DownloadIconTask(getActivity(), this, i).execute(mForecast[i].getImgUrl());
        }
    }

    /*
    gets subviews that contain the view to be filled with data
     */

    private RelativeLayout[] getContainterLayouts() {
        RelativeLayout[] mLayoutItem = new RelativeLayout[4];
        mLayoutItem[0] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day1);
        mLayoutItem[1] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day2);
        mLayoutItem[2] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day3);
        mLayoutItem[3] = (RelativeLayout) getActivity().findViewById(R.id.lyt_day4);
        return mLayoutItem;
    }

    /*
    loads all of the data into the views
     */

    private void loadViewDetails() {
        RelativeLayout[] mLayoutItem = getContainterLayouts();
        for(int i =0; i < mLayoutItem.length; i++) {
            TextView dayOfWeekTxt = (TextView) mLayoutItem[i].findViewById(R.id.txt_day);
            dayOfWeekTxt.setText(mForecast[i].getDayOfWeek());
            TextView tempTxt = (TextView) mLayoutItem[i].findViewById(R.id.txt_temperature);
            tempTxt.setText(new MySharedPrefs(getActivity()).isCelsius() ? mForecast[i].getTempC() + getActivity().getResources().getString(R.string.global_celsius_abbr)
                                                                : mForecast[i].getTempF() + getActivity().getResources().getString(R.string.global_fahrenheit_abbr));
            TextView descTxt = (TextView) mLayoutItem[i].findViewById(R.id.txt_description);
            descTxt.setText(mForecast[i].getDescription());
            AlphaAnimation mAnim = new AlphaAnimation(0.0f, 1.0f);
            dayOfWeekTxt.setAnimation(mAnim);
            tempTxt.setAnimation(mAnim);
            descTxt.setAnimation(mAnim);
        }
    }

    /*
    called when an icon is downloaded
    @params: bitmap: obtained bitmap containing the picture
            pictureNum: index of the picture that was downloaded
     */

    @Override
    public void OnIconDownloaded(Bitmap bitmap, int pictureNum) {
        RelativeLayout[] mLayoutItem = getContainterLayouts();
        if(mIcon == null)
            mIcon = new Bitmap[mLayoutItem.length];
        for(int i=0; i<mForecast.length; i++) {
            if(pictureNum == i) {
                mIcon[i] = bitmap;
                iconFlyInAnimation(i, mLayoutItem[i]);
            }
        }
    }

    /*
    assigning an icon to an image view and running an animation
     */

    private void iconFlyInAnimation(int iconIndex, RelativeLayout rLyt) {
        ImageView mImgIcon = (ImageView) rLyt.findViewById(R.id.img_weather_state);
        mImgIcon.setImageBitmap(mIcon[iconIndex]);
        ImageView mImgCircle = (ImageView) rLyt.findViewById(R.id.img_weather_state_circle);
        mImgCircle.setImageResource(R.drawable.overlay_wsymbols);
        Animation mAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_left);
        LinearLayout mLinLyt = (LinearLayout) rLyt.findViewById(R.id.lyt_weather_state_container);
        mLinLyt.setVisibility(View.VISIBLE);
        mLinLyt.setAnimation(mAnim);
    }
}
