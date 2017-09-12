package com.possible.rssreader.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.possible.rssreader.fragment.FeedFragment;
import com.possible.rssreader.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            generateFragments();
        }
    }

    private void generateFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FeedFragment feedFragment = new FeedFragment();
        fragmentTransaction.add(R.id.lyt_feed_container, feedFragment);
        fragmentTransaction.commit();
    }
}
