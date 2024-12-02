package com.example.sandy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView img=(ImageView)findViewById(R.id.splashLogo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        // Start animations
        img.startAnimation(fadeIn);
        img.startAnimation(scaleUp);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After the timer ends, launch the main activity
                Intent intent = new Intent(SplashActivity.this, MainActivity2.class);
                startActivity(intent);

                // Close the splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}