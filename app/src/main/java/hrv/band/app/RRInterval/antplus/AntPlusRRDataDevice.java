package hrv.band.app.RRInterval.antplus;

import android.app.Activity;
import android.content.Context;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import hrv.band.app.RRInterval.HRVRRIntervalDevice;

/**
 * Created by Julian on 29.12.2016.
 */

public class AntPlusRRDataDevice
        extends HRVRRIntervalDevice
        implements AntPluginPcc.IDeviceStateChangeReceiver,
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>,
        AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver {

    private AntPlusHeartRatePcc wgtplc;

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
        this.rrMeasurements.add((double) rr);
        notifyRRIntervalListeners(rr);
    }


    @Override
    public void stopMeasuring() {
        if(wgtplc != null)
            wgtplc.releaseAccess();
    }

    @Override
    public List<Double> getRRIntervals() {
        return rrMeasurements;
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
    public void tryStartRRIntervalMeasuring() {
        boolean submitted = wgtplc.subscribeCalculatedRrIntervalEvent(this);

        if(submitted) {
            notifyDeviceStartedMeasurement();
        }
    }
}
