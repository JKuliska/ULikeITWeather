package com.example.ulikeitweather.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.SettingsArrayAdapter;
import com.example.ulikeitweather.app.entity.SettingsItem;
import com.example.ulikeitweather.app.utility.MySharedPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * deals with settings - kilometers/miles, celsius/fahrenheit
 */

public class SettingsFragment extends Fragment {

    private List<SettingsItem> mCurrentSettings;
    private SettingsArrayAdapter mSettingsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        renderView();
    }

    private void renderView() {
        final ListView mListViewSettings = (ListView) getActivity().findViewById(R.id.list_settings);

        mCurrentSettings = getCurrentSettingsList();

        mSettingsAdapter = new SettingsArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, mCurrentSettings);

        mListViewSettings.setAdapter(mSettingsAdapter);

        mListViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        refreshSettingsItem(true);
                        break;
                    case 1:
                        refreshSettingsItem(false);
                        break;
                }
            }
        });
    }


    /*
    changes the value of an settings item
    @params: flag to change either distance (true) or temperature (false)
     */
    private void refreshSettingsItem(boolean which) {
        MySharedPrefs mPrefs = new MySharedPrefs(getActivity());
        if(which)
            mPrefs.setKilometer();
        else
            mPrefs.setCelsius();
        mCurrentSettings = getCurrentSettingsList();
        mSettingsAdapter.swapItems(mCurrentSettings);
    }


    private List<SettingsItem> getCurrentSettingsList() {
        List<SettingsItem> mCurrSettings = new ArrayList<SettingsItem>();
        MySharedPrefs mPrefs = new MySharedPrefs(getActivity());

        mCurrSettings.add(0, new SettingsItem(getResources().getString(R.string.list_item_length),
                mPrefs.isKilometer() ? getActivity().getResources().getString(R.string.global_kph) : getActivity().getResources().getString(R.string.global_mph)));

        mCurrSettings.add(1, new SettingsItem(getResources().getString(R.string.list_item_temperature),
                mPrefs.isCelsius() ? getActivity().getResources().getString(R.string.global_celsius) : getActivity().getResources().getString(R.string.global_fahrenheit)));
        return mCurrSettings;
    }



}
