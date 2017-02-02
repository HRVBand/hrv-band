package hrv.band.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This Activity is shown if the app starts.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);

        animateLogo();

        final int splashTimeOut = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, splashTimeOut);
    }

    /**
     * Make sure that the logo fades in.
     */
    private void animateLogo() {
        ImageView imageView = (ImageView) findViewById(R.id.splash_logo);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        imageView.startAnimation(startAnimation);
    }
}