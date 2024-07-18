package com.example.dating_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BtnDislikeActivity extends AppCompatActivity {

    private static final String TAG = "BtnDislikeActivity";
    private static final int ACTIVITY_NUM =1;
    private final Context mContext = BtnDislikeActivity.this;
    private ImageView dislike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_btn_dislike);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupTopNavigationView();
        dislike = findViewById(R.id.dislike);

        Intent intent = getIntent();
        String profileUrl = intent.getStringExtra("url");

        assert profileUrl != null;

        if (profileUrl.equals("default")) {
            Glide.with(mContext).load(R.drawable.person_24px).into(dislike);
        } else {
            Glide.with(mContext).load(profileUrl).into(dislike);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent mainIntent = new Intent(BtnDislikeActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        }).start();
    }

    public void LikeBtn(View view) {
    }

    private void setupTopNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(bottomNavigationViewEx);
        TopNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}