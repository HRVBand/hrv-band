package hrv.band.app.RRInterval;

/**
 * Created by Julian on 03.01.2017.
 */

public interface HRVRRDeviceListener {

    void deviceStartedMeasurement();
    void deviceError(String error);
    void deviceStatusChanged(HRVDeviceStatus status);
}
