package hrv.band.aurora.RRInterval.msband;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import hrv.band.aurora.R;
import hrv.band.aurora.RRInterval.IRRInterval;
import hrv.band.aurora.view.UiHandlingUtil;

/**
 * Created by Thomas on 13.06.2016.
 */
public class MSBandRRInterval implements IRRInterval {
    private BandClient client;
    private Activity activity;
    private TextView statusTxt;
    private List<Double> rr;//stores the actual measurement-rrIntervals
    private WeakReference<Activity> reference;
    private ObjectAnimator animation;

    /**
     *Handels when a new RRInterval is incoming
     */
    private BandRRIntervalEventListener mRRIntervalEventListener;

    public MSBandRRInterval(final Activity activity, TextView statusTxt, final TextView rrStatus) {
        this.statusTxt = statusTxt;
        this.activity = activity;
        reference = new WeakReference<>(activity);
        rr = new ArrayList<>();

        mRRIntervalEventListener = new BandRRIntervalEventListener() {
            @Override
            public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
                if (event != null) {
                    double help = event.getInterval();
                    UiHandlingUtil.updateTextView(activity, rrStatus, String.format("%.2f", help));
                    rr.add(help);//add the actual rrInterval
                }
            }
        };
    }
    @Override
    public void startRRIntervalMeasuring(ObjectAnimator animation) {
        this.animation = animation;
        new MSBandRRIntervalSubscriptionTask(this).execute();
    }

    @Override
    public void startAnimation() {
        UiHandlingUtil.updateTextView(activity, statusTxt, activity.getResources().getString(R.string.msg_hold_still));
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (animation != null) {
                    animation.start();
                }
            }
        });

    }

    @Override
    public void stopMeasuring() {
        try {
            client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
        } catch (BandIOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pauseMeasuring() {
        if (client != null) {
            stopMeasuring();
        }
    }

    @Override
    public void destroy() {
        if (client != null) {
            try {
                client.disconnect().await();
            } catch (InterruptedException e) {
                // Do nothing as this is happening during destroy
            } catch (BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
    }

    @Override
    public void getDevicePermission(){
        new MSBandHeartRateConsentTask(reference, this).execute();
    }

    @Override
    public boolean isDeviceConnected() {
        boolean connected = false;
        try {
            connected = getConnectedBandClient();
        } catch (InterruptedException inter) {

        }catch (BandException band) {

        }
        return connected;
    }

    /**
     * get the Microsoft band client, that handles all the connections and does the actual measurement
     * @return wether the band is connected
     * @throws InterruptedException	connection has dropped e.g.
     * @throws BandException ohter stuff that should not happen
     */
    public boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                UiHandlingUtil.updateTextView(activity, statusTxt, activity.getResources().getString(R.string.error_band_not_paired));
                return false;
            }
            client = BandClientManager.getInstance().create(activity.getApplicationContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        UiHandlingUtil.updateTextView(activity, statusTxt, activity.getResources().getString(R.string.msg_band_connecting));
        return ConnectionState.CONNECTED == client.connect().await();
    }

    public BandClient getClient() {
        return client;
    }

    public BandRRIntervalEventListener getRRIntervalEventListener() {
        return mRRIntervalEventListener;
    }

    public Double[] getRRIntervals() {
        return (Double[]) rr.toArray(new Double[rr.size()]);
    }

}
