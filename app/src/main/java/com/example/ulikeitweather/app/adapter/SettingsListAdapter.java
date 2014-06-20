package com.example.ulikeitweather.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;

import java.util.List;

/**
 * adapter for filling the items in settings list
 */

public class SettingsListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mCurrentSettings;


    public SettingsListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mCurrentSettings = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //view consists of two text fields
        View rowView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
        //get titles of the items from resource
        String[] mTitleArray = mContext.getResources().getStringArray(R.array.view_list_settings);

        TextView mTitleTxt = (TextView) rowView.findViewById(android.R.id.text1);
        TextView mCurrentOptionTxt = (TextView) rowView.findViewById(android.R.id.text2);

        mTitleTxt.setText(mTitleArray[position]);
        mCurrentOptionTxt.setText(mCurrentSettings.get(position));

        return rowView;
    }

    /*
    if an item is clicked the list is refreshed
     */
    public void swapItems(List<String> currentOptions) {
        this.mCurrentSettings = currentOptions;
        notifyDataSetChanged();
    }
}
