package hrv.band.app.devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 03.01.2016.
 *
 * Collaborator Thomas Czogalik
 */
public abstract class HRVRRIntervalDevice implements HRVRRIntervalEventInitiator {

    protected List<Double> rrMeasurements = new ArrayList<>();

    private final List<HRVRRIntervalListener> listeners = new ArrayList<>();
    private final List<HRVRRDeviceListener> deviceListeners = new ArrayList<>();

    public abstract void connect();
    public abstract void tryStartRRIntervalMeasuring();
    public abstract void stopMeasuring();
    public abstract void destroy();
    public abstract void pauseMeasuring();

    public List<Double> getRRIntervals() {
        return rrMeasurements;
    }

    public void clearRRIntervals() {
        rrMeasurements = new ArrayList<>();
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

    public void notifyDeviceStatusChanged(HRVDeviceStatus status) {
        for(HRVRRDeviceListener listener : deviceListeners) {
            listener.deviceStatusChanged(status);
        }
    }

    public void notifyDeviceError(String error) {
        for (HRVRRDeviceListener listener: deviceListeners) {
            listener.deviceError(error);
        }
    }

    public void addDeviceListener(HRVRRDeviceListener toAdd) {deviceListeners.add(toAdd); }
}
