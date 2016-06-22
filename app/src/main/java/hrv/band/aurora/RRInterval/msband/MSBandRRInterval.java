package inno.hacks.ms.band.RRInterval.msband;

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

import inno.hacks.ms.band.RRInterval.IRRInterval;

/**
 * Created by Thomas on 13.06.2016.
 */
public class MSBandRRInterval implements IRRInterval {
    private BandClient client;
    private Activity activity;
    private TextView statusTxt;
    private List<Double> rr;//stores the actual measurement-rrIntervals
    private WeakReference<Activity> reference;
    /**
     *Handels when a new RRInterval is incoming
     */
    private BandRRIntervalEventListener mRRIntervalEventListener;

    public MSBandRRInterval(Activity activity, TextView statusTxt) {
        this.activity = activity;
        this.statusTxt = statusTxt;
        reference = new WeakReference<Activity>(activity);
        rr = new ArrayList<>();

        mRRIntervalEventListener = new BandRRIntervalEventListener() {
            @Override
            public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
                if (event != null) {
                    double help = event.getInterval();
                    updateStatusText(String.format("RR Interval = %.3f s\n", help));
                    rr.add(help);//add the actual rrInterval

                }
            }
        };
    }
    @Override
    public void startRRIntervalMeasuring() {
        new MSBandRRIntervalSubscriptionTask(this).execute();
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
                updateStatusText("Band isn't paired with your phone.\n");
                return false;
            }
            client = BandClientManager.getInstance().create(activity.getApplicationContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        updateStatusText("Band is connecting...\n");
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

    /**
     * write data to UI-thread
     * @param string the text to write
     */
    public void updateStatusText(final String string) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusTxt.setText(string);
            }
        });
    }
}
