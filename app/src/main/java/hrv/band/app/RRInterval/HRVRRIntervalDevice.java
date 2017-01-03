package hrv.band.app.RRInterval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 13.06.2016.
 */
public abstract class HRVRRIntervalDevice implements HRVRRIntervalEventInitiator {

    protected List<Double> rrMeasurements = new ArrayList<>();

    public abstract void connect();
    public abstract void tryStartRRIntervalMeasuring();
    public abstract void stopMeasuring();
    public abstract void destroy();
    public abstract void pauseMeasuring();

    private List<HRVRRIntervalListener> listeners = new ArrayList<>();
    private List<HRVRRDeviceListener> deviceListeners = new ArrayList<>();

    public List<Double> getRRIntervals() {
        return rrMeasurements;
    }

    @Override
    public void notifyRRIntervalListeners(double rrValue) {
        for (HRVRRIntervalListener listener: listeners) {

            HRVRRIntervalEvent event = new HRVRRIntervalEvent();
            event.setRr(rrValue);

            listener.newRRInterval(event);
        }
    }

    @Override
    public void addRRIntervalListener(HRVRRIntervalListener toAdd) {
        listeners.add(toAdd);
    }

    public void notifyDeviceStartedMeasurement() {
        for (HRVRRDeviceListener listener: deviceListeners) {
            listener.deviceStartedMeasurement();
        }
    }

    protected void notifyDeviceStatusChanged(HRVDeviceStatus status) {
        for(HRVRRDeviceListener listener : deviceListeners) {
            listener.deviceStatusChanged(status);
        }
    }

    protected void notifyDeviceError(String error) {
        for (HRVRRDeviceListener listener: deviceListeners) {
            listener.deviceError(error);
        }
    }

    public void addDeviceListener(HRVRRDeviceListener toAdd) {deviceListeners.add(toAdd); }
}
