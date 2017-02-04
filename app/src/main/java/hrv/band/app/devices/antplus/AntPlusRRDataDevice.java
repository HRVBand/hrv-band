package hrv.band.app.devices.antplus;

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

import hrv.band.app.devices.HRVDeviceStatus;
import hrv.band.app.devices.HRVRRIntervalDevice;
/**
 * Copyright (c) 2017
 * Created by Julian Martin on 29.12.2016.
 */
public class AntPlusRRDataDevice
        extends HRVRRIntervalDevice
        implements AntPluginPcc.IDeviceStateChangeReceiver,
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>,
        AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver {

    private AntPlusHeartRatePcc wgtplc;

    private final Context context;
    private final Activity activity;

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
                notifyDeviceStatusChanged(HRVDeviceStatus.CONNECTED);
                break;
            case USER_CANCELLED:
            case SEARCH_TIMEOUT:
            case OTHER_FAILURE:
                notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED);
                break;
            default:
                notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED);
        }
    }

    @Override
    public void onNewCalculatedRrInterval(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, AntPlusHeartRatePcc.RrFlag rrFlag) {
        double rr = bigDecimal.longValue() / 1000.0;
        this.rrMeasurements.add(rr);
        notifyRRIntervalListeners(rr);
    }


    @Override
    public void stopMeasuring() {
        if(wgtplc != null) {
            wgtplc.releaseAccess();
            notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED);
        }
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
        notifyDeviceStatusChanged(HRVDeviceStatus.CONNECTING);

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
        if(wgtplc == null)
        {
            connect();
        }
        else {
            boolean submitted = wgtplc.subscribeCalculatedRrIntervalEvent(this);

            if(submitted) {
                notifyDeviceStartedMeasurement();
            }
        }
    }
}
