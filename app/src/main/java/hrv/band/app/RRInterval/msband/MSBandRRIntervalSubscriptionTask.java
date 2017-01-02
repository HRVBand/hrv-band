package hrv.band.app.RRInterval.msband;

/**
 * Created by Thomas on 13.06.2016.
 */

import android.os.AsyncTask;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandSensorManager;

import hrv.band.app.view.UiHandlingUtil;

/**
 * Class that meassures the RRInterval (get results via Eventhandler...)
 */
public class MSBandRRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {

    private MSBandRRIntervalDevice msBandRRIntervalDevice;
    public MSBandRRIntervalSubscriptionTask(MSBandRRIntervalDevice msBandRRIntervalDevice) {
        this.msBandRRIntervalDevice = msBandRRIntervalDevice;
    }

    //register eventhandler, so we can recieve the rrIntervals
    @Override
    protected Void doInBackground(Void... params) {
       try {
            if (msBandRRIntervalDevice.getConnectedBandClient()) {
                BandClient client = msBandRRIntervalDevice.getClient();
                int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
                if (hardwareVersion >= 20) {
                    BandSensorManager sensorManager = client.getSensorManager();
                    if (sensorManager.getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        sensorManager.registerRRIntervalEventListener(msBandRRIntervalDevice.getRRIntervalEventListener());
                        msBandRRIntervalDevice.startAnimation();
                    } else {
                        //msBandRRIntervalDevice.showConsentSnackbar("Please give consent to access heart rate data");
                        msBandRRIntervalDevice.connect();
                    }
                } else {
                    UiHandlingUtil.showSnackbar("The RR Interval sensor is only supported with MS Band 2.\n");
                }
            } else {

                UiHandlingUtil.showSnackbar("Device isn't connected. Is bluetooth on and the device in range?\n");
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
           UiHandlingUtil.showSnackbar(exceptionMessage);

        } catch (Exception e) {
           UiHandlingUtil.showSnackbar(e.getMessage());
        }
        return null;
    }

}
