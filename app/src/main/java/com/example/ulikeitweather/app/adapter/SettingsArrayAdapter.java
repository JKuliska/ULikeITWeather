package com.example.ulikeitweather.app.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.entity.SettingsItem;

import java.util.List;

/**
 * adapter for filling the items in settings list
 */

public class SettingsArrayAdapter extends ArrayAdapter<SettingsItem> {

    private Context mContext;
    private List<SettingsItem> mCurrentSettings;
    private int mSelectedPosition = -1;


    public SettingsArrayAdapter(Context context, int resource, List<SettingsItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mCurrentSettings = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //view consists of two text fields
            view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.titleText = (TextView) view.findViewById(android.R.id.text1);
            holder.valueText = (TextView) view.findViewById(android.R.id.text2);

            view.setTag(holder);
        }

        SettingsItem settItem = mCurrentSettings.get(position);

        if(settItem != null) {

            // view holder
            ViewHolder holder = (ViewHolder) view.getTag();

            // content
            holder.titleText.setText(settItem.getTitleText());
            holder.valueText.setText(settItem.getValueText());

            // selected item
            if(mSelectedPosition == position)
            {
                view.setBackgroundResource(mContext.getResources().getColor(R.color.view_listview_item_bg_selected));
            }
            else
            {
                setBackgroundDiffApi(view, R.drawable.selector_view_listview_item_bg);
            }
        }

        return view;
    }

    /*
    if an item is clicked the list is refreshed
     */
    public void swapItems(List<SettingsItem> currentOptions) {
        this.mCurrentSettings = currentOptions;
        notifyDataSetChanged();
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
        TextView titleText;
        TextView valueText;
    }
}
