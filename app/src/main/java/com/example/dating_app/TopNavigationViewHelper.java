package com.example.dating_app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.dating_app.match.MatchesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TopNavigationViewHelper {
    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationView bottomNavigationViewEx) {
        Log.d(TAG, "setupTopNavigationView: Setting up navigation view");
        // Perform any setup for your BottomNavigationView here
    }

    public static void enableNavigation(final Context context, BottomNavigationView view){
        view.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the item ID

                if (itemId == R.id.ic_profile) {
                    Intent i = new Intent(context, SettingActivity.class);
                    context.startActivity(i);
                } else if (itemId == R.id.ic_matched) {
                    Intent i1 = new Intent(context, MatchesActivity.class);
                    context.startActivity(i1);
                }
            }
        });
    }

}
