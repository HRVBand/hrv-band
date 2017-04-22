package hrv.band.app.device;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 03.01.2016.
 */
public interface HRVRRDeviceListener {

    void deviceStartedMeasurement();
    void deviceError(String error);
    void deviceStatusChanged(HRVDeviceStatus status);
}
