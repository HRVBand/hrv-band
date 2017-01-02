package hrv.band.app.RRInterval;

import android.animation.ObjectAnimator;

/**
 * Created by Thomas on 13.06.2016.
 */
public interface IRRIntervalDevice {

    void startRRIntervalMeasuring(ObjectAnimator animation);

    void stopMeasuring();

    Double[] getRRIntervals();

    void pauseMeasuring();

    void destroy();

    void connect();

    void startAnimation();
}
