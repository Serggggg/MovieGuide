package com.serggggg.movieguide.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.serggggg.movieguide.app.R;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_MOVIE_ID = "MOVIE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            if (extras.containsKey(KEY_MOVIE_ID)) {
                DetailFragment fragment = new DetailFragment();
                fragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction().add(R.id.container_detail, fragment).commit();
            }
        }
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        if (intent != null)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}
