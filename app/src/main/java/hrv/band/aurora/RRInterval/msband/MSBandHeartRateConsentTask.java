package hrv.band.aurora.RRInterval.msband;
/**
 * Created by Thomas on 13.06.2016.
 */
import android.app.Activity;
import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.sensors.HeartRateConsentListener;

import java.lang.ref.WeakReference;

/**
 * Class that gets user-permission for measuring the heartrate (and rrIntervals)
 */
public class MSBandHeartRateConsentTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Activity> activityWeakReference;
    private MSBandRRInterval msBandRRInterval;

    public MSBandHeartRateConsentTask(WeakReference<Activity> activityWeakReference, MSBandRRInterval msBandRRInterval) {
        this.activityWeakReference = activityWeakReference;
        this.msBandRRInterval = msBandRRInterval;
    }
    //register eventhandler, so we can recieve wether the user has accepted the measurement
    @Override
    protected Void doInBackground(Void... params) {
        BandClient client = msBandRRInterval.getClient();
        try {
            if (msBandRRInterval.getConnectedBandClient()) {

                if (activityWeakReference != null) {
                    client.getSensorManager().requestHeartRateConsent(activityWeakReference.get(), new HeartRateConsentListener() {
                        @Override
                        public void userAccepted(boolean consentGiven) {
                        }
                    });
                }
            } else {
                msBandRRInterval.updateStatusText("Band isn't connected. Please make sure bluetooth is on and the band is in range.\n");
            }
        } catch (BandException e) {
            String exceptionMessage="";
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
            msBandRRInterval.updateStatusText(exceptionMessage);

        } catch (Exception e) {
            msBandRRInterval.updateStatusText(e.getMessage());
        }
        return null;
    }
}
