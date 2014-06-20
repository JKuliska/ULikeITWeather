package com.example.ulikeitweather.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.fragment.SettingsFragment;
import com.example.ulikeitweather.app.fragment.WeatherForecastFragment;
import com.example.ulikeitweather.app.fragment.WeatherFragment;
import com.example.ulikeitweather.app.fragment.WeatherTodayFragment;
import com.example.ulikeitweather.app.adapter.DrawerArrayAdapter;
import com.example.ulikeitweather.app.geolocation.GeoLocation;

import java.util.Arrays;
import java.util.List;

/**
 * Main Activity holding two fragments for current weather and weather forecast
 * Handles navigation drawer and action bar buttons
 */
public class MainActivity extends ActionBarActivity {

    private List<String> mForecastOptions; //options in drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private WeatherFragment currentFragment; //currently active fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        renderViewNavigationDrawer();
        setConfigVariablesDistTemp();
        //run WeatherTodayFragment
        if (savedInstanceState == null) {
            currentFragment = new WeatherTodayFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.lyt_main, currentFragment).commit();
            setTitle(getResources().getString(R.string.ac_today));
        }
    }



    /*
    loads the units (from SharedPreferences) which should be used Kilometers/Miles, Celsius/Fahrenheit
     */
    private void setConfigVariablesDistTemp() {
        SharedPreferences mPrefs = this.getSharedPreferences(SettingsFragment.SETTINGS_PREFS, 0);
        SettingsFragment.useKilometer = mPrefs.getBoolean(SettingsFragment.SETTING_LENGTH_UNIT, true);
        SettingsFragment.useCelsius = mPrefs.getBoolean(SettingsFragment.SETTING_TEMP_UNIT, true);
    }


    /*
    renders the navigation drawer view and assigns items in it
     */
    private void renderViewNavigationDrawer() {
        mForecastOptions = Arrays.asList(getResources().getStringArray(R.array.drawer_items));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.drawer_left);

        // Set the adapter for the list view
        mDrawerListView.setAdapter(new DrawerArrayAdapter(getApplication(), mForecastOptions));

        mDrawerListView.setOnItemClickListener(new OnDrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerListView.setItemChecked(0, true);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //handle other ActionBar items
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                GeoLocation.dontAskToTurnOnGPS = false;
                currentFragment.loadView();
                break;
            case R.id.menu_item_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
    handles the clicks on the drawer items
     */
    private class OnDrawerItemClickListener  implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            drawerSelectItem(position);
        }

        private void drawerSelectItem(int position) {
            switch (position) {
                case 1:
                    currentFragment = new WeatherForecastFragment();
                    break;
                default:
                    currentFragment = new WeatherTodayFragment();
                    break;
            }
            // Insert the fragment by replacing any existing fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.lyt_main, currentFragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerListView.setItemChecked(position, true);
            setTitle(mForecastOptions.get(position));
            mDrawerLayout.closeDrawer(mDrawerListView);
        }
    }
}
