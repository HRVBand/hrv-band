package hrv.band.app.devices;

/**
 * Created by Julian on 02.01.2017.
 * Observer class, that notifies listeners, when a new heart beat arises
 */

interface HRVRRIntervalEventInitiator {

    void notifyRRIntervalListeners(double rrValue);
    void addRRIntervalListener(HRVRRIntervalListener toAdd);
}
