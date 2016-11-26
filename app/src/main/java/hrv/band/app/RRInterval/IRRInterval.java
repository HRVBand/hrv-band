package hrv.band.app.RRInterval;

import android.animation.ObjectAnimator;

/**
 * Created by Thomas on 13.06.2016.
 */
public interface IRRInterval {

    boolean isDeviceConnected();

    void startRRIntervalMeasuring(ObjectAnimator animation);

    void stopMeasuring();

    Double[] getRRIntervals();

    void pauseMeasuring();

    void destroy();

    void getDevicePermission();

    void startAnimation();

}
