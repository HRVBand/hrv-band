package hrv.band.app.devices.msband;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.UserConsent;
import com.microsoft.band.sensors.BandSensorManager;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik 13.06.2016.
 *
 * Collaborator Julian Martin
 *
 * Class that measures the RRInterval (get results via Eventhandler).
 */
class MSBandRRIntervalSubscriptionTask extends AsyncTask<Void, Void, Void> {

    private final MSBandRRIntervalDevice msBandRRIntervalDevice;
    MSBandRRIntervalSubscriptionTask(MSBandRRIntervalDevice msBandRRIntervalDevice) {
        this.msBandRRIntervalDevice = msBandRRIntervalDevice;
    }

    //register eventhandler, so we can recieve the rrIntervals
    @Override
    protected Void doInBackground(Void... params) {
       try {
           if (!msBandRRIntervalDevice.getConnectedBandClient()) {
               msBandRRIntervalDevice.notifyDeviceError("Device isn't connected. Is bluetooth on and the device in range?\n");
               return null;
           }
           BandClient client = msBandRRIntervalDevice.getClient();
           int hardwareVersion = Integer.parseInt(client.getHardwareVersion().await());
           if (hardwareVersion < 20) {
               msBandRRIntervalDevice.notifyDeviceError("The RR Interval sensor is only supported with MS Band 2.\n");
               return null;
           }
           BandSensorManager sensorManager = client.getSensorManager();
           if (sensorManager.getCurrentHeartRateConsent() != UserConsent.GRANTED) {
               msBandRRIntervalDevice.connect();
               return null;
           }
           sensorManager.registerRRIntervalEventListener(msBandRRIntervalDevice.getRRIntervalEventListener());
           msBandRRIntervalDevice.notifyDeviceStartedMeasurement();
        } catch (BandException e) {
           Log.e(e.getClass().getName(), "BandException", e);
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
           msBandRRIntervalDevice.notifyDeviceError(exceptionMessage);

        } catch (Exception e) {
           Log.e(e.getClass().getName(), "Exception", e);
           msBandRRIntervalDevice.notifyDeviceError(e.getMessage());
        }
        return null;
    }

}
