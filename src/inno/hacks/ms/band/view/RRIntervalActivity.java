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
import com.microsoft.band.BandIOException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.HeartRateConsentListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import inno.hacks.ms.band.RRInterval.Interval;
import inno.hacks.ms.band.rrintervalExample.R;
import inno.hacks.ms.band.RRInterval.RRIntervalBand;

public class RRIntervalActivity extends Activity {

    private TextView rrTextView;
    private RRIntervalBand band;
    private List rr;
    private Interval ival;
    private RRIntervalBand.RRIntervalSubscriptionTask task;
    static int duration = 10000;
    final WeakReference<Activity> reference = new WeakReference<Activity>(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rrinterval);

        rrTextView = (TextView) findViewById(R.id.rrTextView);
        band = new RRIntervalBand();
        ival = new Interval();

    }


    public void startMeasure(View view) {
        rr = new ArrayList<Double>();
        ival.SetStartTime(new Date());

        //txtStatus.setText("");
        task = band.startMeasure();
        task.execute();

        CountDownTimer timer = new CountDownTimer(duration,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished/1000+1;
                appendToUI(sec+" seconds remain\n");
            }

            @Override
            public void onFinish() {
                ival = band.stopMeasure();        //hier ist messung fertig, und das ergebnis kann geholt werden.

                String s = ival.printRR();
                appendToUI(ival.printRR());

            }
        }.start();

    }

    public void getPermission(View view) {
        band.getPermission(reference);
    }

    private void appendToUI(final String string) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rrTextView.setText(string);
            }
        });
    }

}
