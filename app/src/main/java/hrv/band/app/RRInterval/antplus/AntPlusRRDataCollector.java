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

/**
 * Created by Julian on 29.12.2016.
 */

public class AntPlusRRDataCollector
        implements AntPluginPcc.IDeviceStateChangeReceiver,
        AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>,
        AntPlusHeartRatePcc.ICalculatedRrIntervalReceiver {

    private AntPlusHeartRatePcc wgtplc;

    public void ConnectToHeartRateMonitor(final Context context, final Activity activity) {

        //Release the old access
        if(wgtplc != null) {
            wgtplc.releaseAccess();
            wgtplc = null;
        }

        final AntPlusRRDataCollector helper = this;

        //Make access request
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AntPlusHeartRatePcc.requestAccess(activity, context, helper, helper);
            }
        });

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
                requestMesurement();
                break;

        }
    }

    private void requestMesurement(){
        boolean submitted = wgtplc.subscribeCalculatedRrIntervalEvent(this);
    }

    @Override
    public void onNewCalculatedRrInterval(long l, EnumSet<EventFlag> enumSet, BigDecimal bigDecimal, AntPlusHeartRatePcc.RrFlag rrFlag) {
        long a = bigDecimal.longValue();
        long b = a;
    }
}
