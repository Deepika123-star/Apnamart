package com.smartwebarts.apnamart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartwebarts.apnamart.dashboard.DashboardActivity;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2500l;
    private ImageView mAppLogoView;
    private TextView description;
    private String user;

    Animation topAnim, bottomAnim;

    private Handler mDelayHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());

            user = preferences.getLoginDetails();
            if (user == null || user.isEmpty()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                finish();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove Title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        init();

        mDelayHandler.postDelayed(runnable, SPLASH_DELAY);

        startAnimation();
    }

    private void init() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animator);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animator);
        mAppLogoView = findViewById(R.id.logo);
        description = findViewById(R.id.description);
    }

    private void startAnimation() {
        mAppLogoView.startAnimation(topAnim);
        description.startAnimation(bottomAnim);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelayHandler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}