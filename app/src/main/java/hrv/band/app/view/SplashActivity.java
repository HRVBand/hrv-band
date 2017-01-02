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
 * Created by Thomas on 02.08.2016.
 */
public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_splash);

        animateLogo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void animateLogo() {
        ImageView imageView = (ImageView) findViewById(R.id.splash_logo);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        imageView.startAnimation(startAnimation);
    }
}