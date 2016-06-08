package com.serggggg.movieguide.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.serggggg.movieguide.app.R;
import com.serggggg.movieguide.app.utils.WebUtils;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setupImagesSize();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment(), MainFragment.FRAGMENT_TAG)
                    .commit();
        }
    }

    /**
     * Getting image size preference for building image URL. If there is no such preference
     * (first launch), preferred image size will be selected from the set of constants,
     * provided by Movie Database API, according to the screen size.
     * Preferred image size will be saved in shared preferences for future use
     */
    private void setupImagesSize() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefKey = getString(R.string.prefs_image_size_prefix_key);
        String imgSizePrefix = preferences.getString(prefKey, null);

        if (imgSizePrefix == null) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int halfWidth = size.x / 2;

            int[] posterSizes = {185, 342, 500};
            int minDelta = Math.abs(halfWidth - posterSizes[0]);
            int sizeIndex = 0;
            for (int i = 1; i < posterSizes.length; i++) {
                int delta = Math.abs(halfWidth - posterSizes[i]);
                if (delta < minDelta) {
                    minDelta = delta;
                    sizeIndex = i;
                }
            }
            imgSizePrefix = "w" + posterSizes[sizeIndex];
            preferences.edit().putString(prefKey, imgSizePrefix).apply();
        }
        WebUtils.IMAGE_SIZE_PREFIX = imgSizePrefix;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
