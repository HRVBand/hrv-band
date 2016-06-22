package inno.hacks.ms.band.RRInterval;

/**
 * Created by Thomas on 13.06.2016.
 */
public interface IRRInterval {

    void startRRIntervalMeasuring();

    void stopMeasuring();

    Double[] getRRIntervals();

    void pauseMeasuring();

    void destroy();

    void getDevicePermission();

}
