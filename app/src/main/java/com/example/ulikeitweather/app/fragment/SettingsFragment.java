package com.example.ulikeitweather.app.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.adapter.SettingsListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * deals with settings - kilometers/miles, celsius/fahrenheit
 */

public class SettingsFragment extends Fragment {

    public static final String SETTINGS_PREFS = "settingsPrefs"; //location where the preferences are stored
    public static final String SETTING_LENGTH_UNIT = "lengthUnit"; //true if kmh, false if mph
    public static final String SETTING_TEMP_UNIT = "tempUnit"; //true if celsius, false if fahrenheit

    public static boolean useCelsius = true;
    public static boolean useKilometer = true;

    private List<String> mCurrentSettings;
    private SettingsListAdapter mSettingsAdapter;


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

        mCurrentSettings = getCurrentSettingsFromSharedPreferences();

        mSettingsAdapter = new SettingsListAdapter(getActivity(), android.R.layout.simple_list_item_2, mCurrentSettings);

        mListViewSettings.setAdapter(mSettingsAdapter);

        mListViewSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        refreshSettingsItem(SETTING_LENGTH_UNIT);
                        break;
                    case 1:
                        refreshSettingsItem(SETTING_TEMP_UNIT);
                        break;
                }
            }
        });
    }


    /*
    changes the value of an settings item
    @params: takes the key of the item under which it is stored in SharedPreferences
     */
    private void refreshSettingsItem(String settingKey) {
        changeCurrentSettings(settingKey);
        mCurrentSettings = getCurrentSettingsFromSharedPreferences();
        mSettingsAdapter.swapItems(mCurrentSettings);
    }


    /*
    obtains the current settings from SharedPreferences
    @return: a list containing the current settings values
     */
    private List<String> getCurrentSettingsFromSharedPreferences() {
        SharedPreferences mPrefs = getActivity().getSharedPreferences(SETTINGS_PREFS, 0);
        List<String> mCurrSettings = new ArrayList<String>();

        mCurrSettings.add(0, mPrefs.getBoolean(SETTING_LENGTH_UNIT, true)
                ? getActivity().getResources().getString(R.string.global_kph) : getActivity().getResources().getString(R.string.global_mph));
        mCurrSettings.add(1, mPrefs.getBoolean(SETTING_TEMP_UNIT, true)
                ? getActivity().getResources().getString(R.string.global_celsius) : getActivity().getResources().getString(R.string.global_fahrenheit));
        return mCurrSettings;
    }


    /*
    changes a given settings value based on a key for storing in SharedPreferences
    @params: a key of the item that is to be changed
     */
    private void changeCurrentSettings(String settingKey) {
        SharedPreferences mPrefs = getActivity().getSharedPreferences(SETTINGS_PREFS, 0);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
        mPrefsEditor.putBoolean(settingKey, !mPrefs.getBoolean(settingKey, true));
        mPrefsEditor.commit();

        if(settingKey.equals(SETTING_LENGTH_UNIT)) {
            useKilometer = mPrefs.getBoolean(SETTING_LENGTH_UNIT, true);
        } else if (settingKey.equals(SETTING_TEMP_UNIT)) {
            useCelsius = mPrefs.getBoolean(SETTING_TEMP_UNIT, true);
        }
    }
}
