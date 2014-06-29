package com.example.ulikeitweather.app.activity;

import android.content.Intent;
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
import com.example.ulikeitweather.app.entity.DrawerItem;
import com.example.ulikeitweather.app.adapter.DrawerArrayAdapter;
import com.example.ulikeitweather.app.fragment.ForecastFragment;
import com.example.ulikeitweather.app.fragment.TodayFragment;
import com.example.ulikeitweather.app.fragment.WeatherParentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Main Activity holding two fragments for current weather and weather forecast
 * Handles navigation drawer and action bar buttons
 */
public class MainActivity extends ActionBarActivity {

    private List<DrawerItem> mForecastOptions; //options in drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private WeatherParentFragment currentFragment; //currently active fragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        renderViewNavigationDrawer();

        //run WeatherTodayFragment as the initial fragment
        if (savedInstanceState == null) {
            currentFragment = new TodayFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.lyt_main, currentFragment).commit();
            setTitle(getResources().getString(R.string.ac_today));
        }
    }

    /*
    renders the navigation drawer view and assigns items in it
     */
    private void renderViewNavigationDrawer() {
        mForecastOptions = new ArrayList<DrawerItem>();

        mForecastOptions.add(0, new DrawerItem(getResources().getString(R.string.ac_today), R.drawable.ic_action_go_to_today));
        mForecastOptions.add(1, new DrawerItem(getResources().getString(R.string.ac_forecast), R.drawable.ic_action_cloud));

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
                    currentFragment = new ForecastFragment();
                    break;
                default:
                    currentFragment = new TodayFragment();
                    break;
            }
            // Insert the fragment by replacing any existing fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.lyt_main, currentFragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerListView.setItemChecked(position, true);
            setTitle(mForecastOptions.get(position).getTitle());
            mDrawerLayout.closeDrawer(mDrawerListView);
        }
    }
}
