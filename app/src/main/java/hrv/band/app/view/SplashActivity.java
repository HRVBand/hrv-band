package hrv.band.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Thomas on 02.08.2016.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.content_splash);

        /*final TextView mSwitcher = (TextView) findViewById(R.id.splash_title);
        mSwitcher.setText("old text");

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);

        AnimationSet as = new AnimationSet(true);
        as.addAnimation(out);
        in.setStartOffset(3000);
        as.addAnimation(in);


        mSwitcher.setupAnimation(as);
        mSwitcher.setText("new text");
        mSwitcher.setupAnimation(in);*/

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
