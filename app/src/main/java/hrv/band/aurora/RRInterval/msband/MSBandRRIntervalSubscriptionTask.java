package hrv.band.aurora.RRInterval.msband;

/**
 * Created by Thomas on 13.06.2016.
 */

import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.UserConsent;

/**
 * Class that meassures the RRInterval (get results via Eventhandler...)
 */
public class MSBandRRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {

    private MSBandRRInterval msBandRRInterval;
    public MSBandRRIntervalSubscriptionTask( MSBandRRInterval msBandRRInterval) {
        this.msBandRRInterval = msBandRRInterval;
    }

    //register eventhandler, so we can recieve the rrIntervals
    @Override
    protected Void doInBackground(Void... params) {
       try {
            if (msBandRRInterval.getConnectedBandClient()) {
                BandClient client = msBandRRInterval.getClient();
                msBandRRInterval.updateStatusText("");
                int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
                if (hardwareVersion >= 20) {
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        client.getSensorManager().registerRRIntervalEventListener(msBandRRInterval.getRRIntervalEventListener());
                    } else {
                        msBandRRInterval.updateStatusText("You have not given this application consent to access heart rate data yet."
                                + " Please press the Heart Rate Consent button.\n");
                    }
                } else {
                    msBandRRInterval.updateStatusText("The RR Interval sensor is not supported with your Band version. Microsoft Band 2 is required.\n");
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
