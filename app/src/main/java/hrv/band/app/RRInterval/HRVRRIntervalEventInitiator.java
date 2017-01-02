package hrv.band.app.RRInterval;

/**
 * Created by Julian on 02.01.2017.
 * Observer class, that notifies listeners, when a new heart beat arises
 */

public interface HRVRRIntervalEventInitiator {

    void notifyListeners(double rrValue);
    void addListener(HRVRRIntervalListener toAdd);
}
