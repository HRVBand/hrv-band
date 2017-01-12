package hrv.band.app.RRInterval.msband;
/**
 * Created by Thomas on 13.06.2016.
 */
import android.app.Activity;
import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.sensors.HeartRateConsentListener;

import java.lang.ref.WeakReference;

import hrv.band.app.R;
import hrv.band.app.view.UiHandlingUtil;

/**
 * Class that gets user-permission for measuring the heartrate (and rrIntervals)
 */
class MSBandHeartRateConsentTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Activity> activityWeakReference;
    private final MSBandRRIntervalDevice msBandRRIntervalDevice;

    public MSBandHeartRateConsentTask(WeakReference<Activity> activityWeakReference, MSBandRRIntervalDevice msBandRRIntervalDevice) {
        this.activityWeakReference = activityWeakReference;
        this.msBandRRIntervalDevice = msBandRRIntervalDevice;
    }
    //register eventhandler, so we can recieve wether the user has accepted the measurement
    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (msBandRRIntervalDevice.getConnectedBandClient()) {
                BandClient client = msBandRRIntervalDevice.getClient();

                if (activityWeakReference != null) {
                    client.getSensorManager().requestHeartRateConsent(activityWeakReference.get(), new HeartRateConsentListener() {
                        @Override
                        public void userAccepted(boolean consentGiven) {
                        }
                    });
                }
            } else {
                String msg = activityWeakReference.get().getResources().getString(R.string.error_device_not_connected_help);
                UiHandlingUtil.showSnackbar(msg);
            }
        } catch (BandException e) {
            String exceptionMessage;
            switch (e.getErrorType()) {
                case UNSUPPORTED_SDK_VERSION_ERROR:
                    exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                    break;
                case SERVICE_ERROR:
                    exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                    break;
                default:
                    exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                    break;
            }
            UiHandlingUtil.showSnackbar(exceptionMessage);

        } catch (Exception e) {
            UiHandlingUtil.showSnackbar(e.getMessage());
        }
        return null;
    }
}
