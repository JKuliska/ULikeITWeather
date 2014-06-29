package com.example.ulikeitweather.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.Weather;
import com.example.ulikeitweather.app.listener.AnimateImageLoadingListener;
import com.example.ulikeitweather.app.utility.Logcat;
import com.example.ulikeitweather.app.utility.MySharedPrefs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ForecastArrayAdapter extends ArrayAdapter<Weather>{

    private List<Weather> mForecast;
    private Context mContext;
    private int mSelectedPosition = -1;
    private int mWindowHeight;
    private boolean mIsPortrait;
    private DisplayImageOptions mDisplayImageOptions;
    private ImageLoader mImageLoader;


    public ForecastArrayAdapter(Context context, List<Weather> objects, boolean isPortrait, DisplayImageOptions displayImageOptions, ImageLoader imageLoader) {
        super(context, R.layout.layout_forecast_day, objects);
        mForecast = objects;
        mContext = context;
        mIsPortrait = isPortrait;
        mDisplayImageOptions = displayImageOptions;
        mImageLoader = imageLoader;
        mWindowHeight = getItemPxHeight();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //view consists of two text fields
            view = inflater.inflate(R.layout.layout_forecast_day, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.dayOfWeekText = (TextView) view.findViewById(R.id.txt_day);
            holder.temperatureText = (TextView) view.findViewById(R.id.txt_temperature);
            holder.weatherDescText = (TextView) view.findViewById(R.id.txt_description);
            holder.iconImg = (ImageView) view.findViewById(R.id.img_weather_state);

            view.setTag(holder);
        }

        Weather forecastItem = mForecast.get(position);

        if(forecastItem != null) {

            // view holder
            ViewHolder holder = (ViewHolder) view.getTag();

            // content
            holder.dayOfWeekText.setText(forecastItem.getDayOfWeek());
            holder.temperatureText.setText(new MySharedPrefs(mContext).isCelsius()
                    ? forecastItem.getTempC() + mContext.getResources().getString(R.string.global_celsius_abbr)
                    : forecastItem.getTempF() + mContext.getResources().getString(R.string.global_fahrenheit_abbr));
            holder.weatherDescText.setText(forecastItem.getDescription());

            mImageLoader.displayImage(forecastItem.getImgUrl(), holder.iconImg, mDisplayImageOptions, new AnimateImageLoadingListener());

            // selected item
            if (mSelectedPosition == position) {
                view.setBackgroundResource(mContext.getResources().getColor(R.color.view_listview_item_bg_selected));
            } else {
                setBackgroundDiffApi(view, R.drawable.selector_view_listview_item_bg);
            }
        }

        view.setMinimumHeight(mWindowHeight);

        return view;
    }

    private int getItemPxHeight() {
        int actionBarHeight = 0;
        int notificationBarHeight = 0;
        int height;

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,mContext.getResources().getDisplayMetrics());
        }

        //Calculate Status bar height

        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            notificationBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
        }

        // Calculate full screen height
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        height = metrics.heightPixels - actionBarHeight - notificationBarHeight;
        return height / (mIsPortrait ? 4 : 2);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(16)
    private void setBackgroundDiffApi(View view, int resource) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            view.setBackground(mContext.getResources().getDrawable(resource));
        } else {
            view.setBackgroundDrawable(mContext.getResources().getDrawable(resource));
        }
    }

    static class ViewHolder {
        TextView dayOfWeekText;
        TextView temperatureText;
        TextView weatherDescText;
        ImageView iconImg;
    }


    public void stop()
    {
        mImageLoader.stop();
    }
}
