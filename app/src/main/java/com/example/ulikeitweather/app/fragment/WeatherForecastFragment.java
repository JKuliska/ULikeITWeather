package com.example.ulikeitweather.app.fragment;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.ForecastArrayAdapter;
import com.example.ulikeitweather.app.client.parser.WeatherParser;
import com.example.ulikeitweather.app.dialog.ShareDialog;
import com.example.ulikeitweather.app.utility.MySharedPrefs;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.task.DownloadIconTask;

import java.util.List;

public class WeatherForecastFragment extends WeatherFragment {

    private List<Weather> mForecast;
    private Bitmap[] mIcon;
    private ArrayAdapter<Weather> mForecastArrayAdapter;
    private ListView mForecastListView;
    private GridView mForecastGridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    public void loadView() {
        getLocationAndWeather();
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
            loadListAdapter();
        }
    }

    private void loadListAdapter() {
        isPortrait = Configuration.ORIENTATION_PORTRAIT == getActivity().getResources().getConfiguration().orientation;
        mForecastArrayAdapter = new ForecastArrayAdapter(getActivity(), mForecast, isPortrait);
        if(isPortrait) {
            mForecastListView = (ListView) getActivity().findViewById(R.id.forecast_list);
            mForecastListView.setAdapter(mForecastArrayAdapter);
        } else {
            mForecastGridView = (GridView) getActivity().findViewById(R.id.forecast_grid);
            mForecastGridView.setAdapter(mForecastArrayAdapter);
        }
        (isPortrait ? mForecastListView : mForecastGridView).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShareDialog dialog = ShareDialog.newInstance(buildShareMessage(position));
                dialog.show(getFragmentManager(), "");
            }
        });
    }

    /*
    creates a message that is to be shared
     */
    private String buildShareMessage(int position) {
        Weather weather = mForecast.get(position);
        if(weather != null) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append((weather.getDayOfWeek() == null ? getActivity().getResources().getString(R.string.global_today) : weather.getDayOfWeek()) + ": ");
            strBuilder.append((new MySharedPrefs(getActivity()).isCelsius() ? weather.getTempC() + getActivity().getResources().getString(R.string.global_celsius_abbr)
                    : weather.getTempF() + getActivity().getResources().getString(R.string.global_fahrenheit_abbr)) + ", ");
            strBuilder.append(weather.getDescription());
            return strBuilder.toString();
        }
        return null;
    }

    /*
    runs task to download icons
     */

    private void downloadIcons() {
        for(int i =0; i < mForecast.size(); i++) {
            new DownloadIconTask(getActivity(), this, i).execute(mForecast.get(i).getImgUrl());
        }
    }

    /*
    called when an icon is downloaded
    @params: bitmap: obtained bitmap containing the picture
            pictureNum: index of the picture that was downloaded
     */

    @Override
    public void OnIconDownloaded(Bitmap bitmap, int pictureNum) {
        /*RelativeLayout[] mLayoutItem = getContainterLayouts();
        if(mIcon == null)
            mIcon = new Bitmap[mLayoutItem.length];
        for(int i=0; i<mForecast.size(); i++) {
            if(pictureNum == i) {
                mIcon[i] = bitmap;
                iconFlyInAnimation(i, mLayoutItem[i]);
            }
        }*/
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
