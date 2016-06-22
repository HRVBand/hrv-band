package hrv.band.aurora.RRInterval;

/**
 * Created by Thomas on 13.06.2016.
 */
public interface IRRInterval {

    boolean isDeviceConnected();

    void startRRIntervalMeasuring();

    void stopMeasuring();

    Double[] getRRIntervals();

    void pauseMeasuring();

    void destroy();

    void getDevicePermission();

}
