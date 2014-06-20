package com.example.ulikeitweather.app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.ulikeitweather.app.R;
import com.example.ulikeitweather.app.fragment.SettingsFragment;

/**
 * Holds the settings fragment
 */

public class SettingsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.lyt_main, new SettingsFragment()).commit();
            setTitle(getResources().getString(R.string.ac_today));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.ac_settings));
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
