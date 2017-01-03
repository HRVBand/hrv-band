package hrv.band.app.RRInterval.antplus;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import hrv.band.app.RRInterval.HRVRRIntervalEvent;
import hrv.band.app.RRInterval.HRVRRIntervalEventInitiator;
import hrv.band.app.RRInterval.HRVRRIntervalListener;
import hrv.band.app.RRInterval.IRRIntervalDevice;

/**
 * Created by Julian on 29.12.2016.
 */

public class AntPlusRRDataDevice
        implements IRRIntervalDevice, HRVRRIntervalEventInitiator,
        AntPluginPcc.IDeviceStateChangeReceiver,
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>,
        AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver {

    private AntPlusHeartRatePcc wgtplc;
    private List<HRVRRIntervalListener> listeners = new ArrayList<>();
    private List<Double> rr = new ArrayList<>();//stores the actual measurement-rrIntervals

    private ObjectAnimator animation;

    final Context context;
    final Activity activity;

    public AntPlusRRDataDevice(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onDeviceStateChange(DeviceState deviceState) {
        if(deviceState == DeviceState.DEAD)
        {
            wgtplc = null;

        }
    }

    @Override
    public void onResultReceived(AntPlusHeartRatePcc antPlusHeartRatePcc, RequestAccessResult requestAccessResult, DeviceState deviceState) {

        switch(requestAccessResult) {
            case SUCCESS:
                wgtplc = antPlusHeartRatePcc;
                break;

        }
    }

    @Override
    public void onNewCalculatedRrInterval(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, AntPlusHeartRatePcc.RrFlag rrFlag) {
        long rr = bigDecimal.longValue();
        this.rr.add((double) rr);
        notifyListeners(rr);
    }

    @Override
    public void notifyListeners(double rrValue) {
        for (HRVRRIntervalListener listener: listeners) {

            HRVRRIntervalEvent event = new HRVRRIntervalEvent();
            event.setRr(rrValue);

            listener.newRRIntervall(event);
        }
    }

    @Override
    public void addListener(HRVRRIntervalListener toAdd) {
        listeners.add(toAdd);
    }

    @Override
    public void startRRIntervalMeasuring(ObjectAnimator animation) {
        boolean submitted = wgtplc.subscribeCalculatedRrIntervalEvent(this);
    }

    @Override
    public void stopMeasuring() {
        if(wgtplc != null)
            wgtplc.releaseAccess();
    }

    @Override
    public Double[] getRRIntervals() {
        return new Double[0];
    }

    @Override
    public void pauseMeasuring() {
        stopMeasuring();
    }

    @Override
    public void destroy() {
        stopMeasuring();
    }

    @Override
    public void connect() {
        //Release the old access
        if(wgtplc != null) {
            wgtplc.releaseAccess();
            wgtplc = null;
        }

        final AntPlusRRDataDevice helper = this;

        //Make access request
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AntPlusHeartRatePcc.requestAccess(activity, context, helper, helper);
            }
        });
    }

    @Override
    public void startAnimation() {

    }
}
