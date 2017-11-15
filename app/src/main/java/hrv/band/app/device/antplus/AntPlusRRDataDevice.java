package hrv.band.app.device.antplus;

import android.app.Activity;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;

import hrv.band.app.device.HRVDeviceStatus;
import hrv.band.app.device.HRVRRIntervalDevice;
/**
 * Copyright (c) 2017
 * Created by Julian Martin on 29.12.2016.
 */
public class AntPlusRRDataDevice
        extends HRVRRIntervalDevice
        implements AntPluginPcc.IDeviceStateChangeReceiver,
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>,
        AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver {

    private final Activity activity;
    private AntPlusHeartRatePcc wgtplc;

    public AntPlusRRDataDevice(Activity activity) {
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
                notifyDeviceStatusChanged(HRVDeviceStatus.CONNECTED, requestAccessResult.toString());
                break;
            case OTHER_FAILURE:
                notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED, requestAccessResult.toString());
                break;
            default:
                notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED, requestAccessResult.toString());
        }
    }

    @Override
    public void onNewCalculatedRrInterval(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, AntPlusHeartRatePcc.RrFlag rrFlag) {

        //Data source cached and data source page 4 are valid sources for rr-data
        if(rrFlag == AntPlusHeartRatePcc.RrFlag.DATA_SOURCE_CACHED ||
                rrFlag == AntPlusHeartRatePcc.RrFlag.DATA_SOURCE_PAGE_4) {
            double rr = bigDecimal.longValue() / 1000.0;
            this.rrMeasurements.add(rr);
            notifyRRIntervalListeners(rr);
        }
    }


    @Override
    public void stopMeasuring() {
        if(wgtplc != null) {
            wgtplc.releaseAccess();
            notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED, "Disconnected");
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
        notifyDeviceStatusChanged(HRVDeviceStatus.CONNECTING, "Connecting");

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
                AntPlusHeartRatePcc.requestAccess(activity, activity.getApplicationContext(), helper, helper);
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
