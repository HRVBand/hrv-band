package hrv.band.app.devices.msband;

import android.app.Activity;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;

import java.lang.ref.WeakReference;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.devices.HRVDeviceStatus;
import hrv.band.app.devices.HRVRRIntervalDevice;

/**
 * Created by Thomas on 13.06.2016.
 */
public class MSBandRRIntervalDevice extends HRVRRIntervalDevice {
    private BandClient client;
    private final Activity activity;
    private final WeakReference<Activity> reference;
    /**
     *Handels when a new RRInterval is incoming
     */
    private final BandRRIntervalEventListener mRRIntervalEventListener;

    public MSBandRRIntervalDevice(final Activity activity) {
        this.activity = activity;
        reference = new WeakReference<>(activity);

        mRRIntervalEventListener = new BandRRIntervalEventListener() {
            @Override
            public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
                if (event != null) {
                    double help = event.getInterval();
                    notifyRRIntervalListeners(help);
                    rrMeasurements.add(help);//add the actual rrInterval
                }
            }
        };
    }
    @Override
    public void tryStartRRIntervalMeasuring() {
        new MSBandRRIntervalSubscriptionTask(this).execute();
    }

    @Override
    public void stopMeasuring() {
        try {
            if (client != null) {
                client.getSensorManager().unregisterRRIntervalEventListener(mRRIntervalEventListener);
            }
            /*if (animation != null) {
                animation.cancel();
            }*/
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
            } catch (InterruptedException | BandException e) {
                // Do nothing as this is happening during destroy
            }
        }
    }



    @Override
    public void connect(){
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
                notifyDeviceError(activity.getResources().getString(R.string.error_band_not_paired));
                return false;
            }
            client = BandClientManager.getInstance().create(activity.getApplicationContext(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }

        notifyDeviceStatusChanged(HRVDeviceStatus.Connecting);
        return ConnectionState.CONNECTED == client.connect().await();
    }

    BandClient getClient() {
        return client;
    }

    BandRRIntervalEventListener getRRIntervalEventListener() {
        return mRRIntervalEventListener;
    }

    public List<Double> getRRIntervals() {
        return rrMeasurements;
    }
}
