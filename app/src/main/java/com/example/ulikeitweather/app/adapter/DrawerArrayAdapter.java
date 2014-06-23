package com.example.ulikeitweather.app.adapter;

import android.annotation.TargetApi;
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
import com.example.ulikeitweather.app.entity.DrawerItem;
import com.example.ulikeitweather.app.entity.SettingsItem;

import java.util.List;

/**
 * Adapter for filling the list in navigation drawer
 */

public class DrawerArrayAdapter extends ArrayAdapter<DrawerItem>{

    private List<DrawerItem> mForecastOptions;
    private Context mContext;
    private int mSelectedPosition = -1;


    public DrawerArrayAdapter(Context context, List<DrawerItem> objects) {
        super(context, R.layout.drawer_list_item, objects);
        mForecastOptions = objects;
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //view consists of two text fields
            view = inflater.inflate(R.layout.drawer_list_item, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.titleText = (TextView) view.findViewById(R.id.txt_list_item);
            holder.iconImg = (ImageView) view.findViewById(R.id.ic_list_item);

            view.setTag(holder);
        }

        DrawerItem drawerItem = mForecastOptions.get(position);

        if(drawerItem != null) {

            // view holder
            ViewHolder holder = (ViewHolder) view.getTag();

            // content
            holder.titleText.setText(drawerItem.getTitle());
            holder.iconImg.setImageResource(drawerItem.getIconResource());

            // selected item
            if (mSelectedPosition == position) {
                view.setBackgroundResource(mContext.getResources().getColor(R.color.view_listview_item_bg_selected));
            } else {
                setBackgroundDiffApi(view, R.drawable.selector_view_listview_item_bg);
            }
        }

        return view;
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
        ImageView iconImg;
    }
}
