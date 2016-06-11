package inno.hacks.ms.band.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.band.BandException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.HeartRateConsentListener;

import java.lang.ref.WeakReference;

import inno.hacks.ms.band.rrintervalExample.R;
import inno.hacks.ms.band.RRInterval.RRIntervalBand;

public class RRIntervalActivity extends Activity {

    private TextView rrTextView;
    private RRIntervalBand band;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrinterval);

        rrTextView = (TextView) findViewById(R.id.rrTextView);
        band = new RRIntervalBand(getApplicationContext(), rrTextView);


    }


    public void startMeasure(View view) {
        rrTextView.setText("HELLO");
        try {
            band.GetRRInterval(rrTextView);
        }
        catch (Exception e){
            rrTextView.setText("Timeout");
        }
    }

    final WeakReference<Activity> reference = new WeakReference<Activity>(this);

    public void getPermission(View view) {
        band.getPermission(reference);
    }



}
