package hrv.band.app.devices;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 03.01.2016.
 *
 * Observer class, that notifies listeners, when a new heart beat arises.
 */
interface HRVRRIntervalEventInitiator {

    void notifyRRIntervalListeners(double rrValue);
    void addRRIntervalListener(HRVRRIntervalListener toAdd);
}
