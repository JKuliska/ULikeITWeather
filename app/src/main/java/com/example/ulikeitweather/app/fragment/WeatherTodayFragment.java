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
import android.widget.TextView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.client.parser.WeatherParser;
import com.example.ulikeitweather.app.utility.MySharedPrefs;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.listener.OnCityLoadedListener;
import com.example.ulikeitweather.app.listener.OnClickListenerShare;
import com.example.ulikeitweather.app.task.DownloadIconTask;
import com.example.ulikeitweather.app.task.GetCityAsyncTask;

public class WeatherTodayFragment extends WeatherFragment implements OnCityLoadedListener {

    private Weather mWeather;
    private Bitmap mIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_today, container, false);
    }

    /*
    actions taken when the orientation is changed
     */
    public void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        inflater.inflate(R.layout.fragment_weather_today, viewGroup);
        loadViewSecondary();
        setupListener();
    }

    /*
    sets up share listener
     */
    private void setupListener() {
        LinearLayout mLayoutFull = (LinearLayout) getActivity().findViewById(R.id.lyt_today_full);
        mLayoutFull.setOnClickListener(new OnClickListenerShare(getFragmentManager(), mWeather, getActivity()));
    }

    /*
     loads the stored weather information, does not download it again
      */
    public void loadViewSecondary() {
        if(mWeather != null)
            loadViewDetails();
        if(myLocation != null)
            setLocationText(myLocation.getCity());
        if(mIcon != null) {
            iconFlyInAnimation();
        }
    }

    @Override
    public void OnCityLoaded(String cityName) {
        myLocation.setCity(cityName);
        setLocationText(cityName);
    }


    /*
    sets the text for city and runs an animation
     */
    public void setLocationText(String cityName) {
        TextView locationTxt = (TextView) getActivity().findViewById(R.id.txt_location);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        locationTxt.setText(cityName);
        locationTxt.setAnimation(alphaAnimation);
    }


    public void loadView() {
        makeIconInvisible();
        getLocationAndWeather();
        loadCity();
    }

    /*
     makes the icons invisible before refresh so that the animation is again visible when loaded
     */
    private void makeIconInvisible() {
        LinearLayout mLinLyt = (LinearLayout) getActivity().findViewById(R.id.lyt_weather_state_container);
        mLinLyt.setVisibility(View.VISIBLE);
    }

    /*
    runs task to load the name of the city
     */

    public void loadCity() {
        if(canGetLocation) {
            new GetCityAsyncTask(getActivity(), this).execute(myLocation);
        } else {
            myLocation.setCity(getResources().getString(R.string.global_unspecified_location));
            setLocationText(myLocation.getCity());
        }
    }

     /*
    loads all of the data into the views
     */

    private void loadViewDetails() {
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
        mImg[0].setImageResource(R.drawable.ic_humidity);
        mText[0].setText(mWeather.getHumidity() + getActivity().getResources().getString(R.string.global_percent_abbr));
        mImg[1].setImageResource(R.drawable.ic_precipitation);
        mText[1].setText(mWeather.getPrecip() + getActivity().getResources().getString(R.string.global_milimeter_abbr));
        mImg[2].setImageResource(R.drawable.ic_pressure);
        mText[2].setText(mWeather.getPressure() + getActivity().getResources().getString(R.string.global_hpa_abbr));
        mImg[3].setImageResource(R.drawable.ic_wind_speed);
        mText[3].setText(new MySharedPrefs(getActivity()).isKilometer() ? mWeather.getWindSpeedKmh() + getActivity().getResources().getString(R.string.global_kph_abbr)
                                                            : mWeather.getWindSpeedMph() + getActivity().getResources().getString(R.string.global_mph_abbr));
        mImg[4].setImageResource(R.drawable.ic_wind_direction);
        mText[4].setText(mWeather.getWindDir());

        TextView mDescriptionTxt = (TextView) getActivity().findViewById(R.id.txt_temperature);
        mDescriptionTxt.setText((new MySharedPrefs(getActivity()).isCelsius() ? mWeather.getTempC() + getActivity().getResources().getString(R.string.global_celsius_abbr)
                                                                    : mWeather.getTempF() + getActivity().getResources().getString(R.string.global_fahrenheit_abbr))
                                                                    + " | " + mWeather.getDescription());
    }

    /*
    called when the weather is finished downloading and starts downloading an icon
     */

    @Override
    public void OnWeatherDownloaded(String file) {
        if(file == null) {
            showInternetDialog();
        } else {
            WeatherParser mWeatherParser = new WeatherParser(getActivity(), file);
            mWeather = mWeatherParser.getWeatherToday();
            new DownloadIconTask(getActivity(), this, 4).execute(mWeather.getImgUrl());
            loadViewDetails();
            setupListener();
        }
    }

    /*
    called when an icon is downloaded
    @params: bitmap: obtained bitmap containing the picture
            pictureNum: index of the picture that was downloaded
     */

    @Override
    public void OnIconDownloaded(Bitmap bitmap, int pictureNum) {
        mIcon = bitmap;
        iconFlyInAnimation();
    }


    /*
    assigning an icon to an image view and running an animation
     */

    private void iconFlyInAnimation() {
        ImageView mImgIcon = (ImageView) getActivity().findViewById(R.id.img_weather_state);
        ImageView mImgCircle = (ImageView) getActivity().findViewById(R.id.img_weather_state_circle);
        mImgIcon.setImageBitmap(mIcon);
        mImgCircle.setImageResource(R.drawable.overlay_wsymbols);
        Animation mAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_top);
        LinearLayout mLinLyt = (LinearLayout) getActivity().findViewById(R.id.lyt_weather_state_container);
        mLinLyt.setVisibility(View.VISIBLE);
        mLinLyt.setAnimation(mAnim);
    }

}
