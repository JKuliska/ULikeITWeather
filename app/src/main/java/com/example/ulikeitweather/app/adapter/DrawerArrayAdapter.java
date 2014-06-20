package com.example.ulikeitweather.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;

import java.util.List;

/**
 * Adapter for filling the list in navigation drawer
 */

public class DrawerArrayAdapter extends ArrayAdapter<String>{

    private List<String> mForecastOptions;
    private Context mContext;


    public DrawerArrayAdapter(Context context, List<String> objects) {
        super(context, R.layout.drawer_list_item, objects);
        mForecastOptions = objects;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //list item consisting of an image and a text
        View rowView = mInflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView txt = (TextView) rowView.findViewById(R.id.txt_list_item);
        ImageView img = (ImageView) rowView.findViewById(R.id.ic_list_item);

        txt.setText(mForecastOptions.get(position));

        //list item is assigned an image, image resources are stored in an array
        TypedArray mIconArray = mContext.getResources().obtainTypedArray(R.array.drawer_icons);
        Drawable drawable = mContext.getResources().getDrawable(mIconArray.getResourceId(position, -1));
        img.setImageDrawable(drawable);
        mIconArray.recycle();

        return rowView;
    }
}
