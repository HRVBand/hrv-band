package hrv.band.app.ui.view.fragment.measuring.listener;

import android.view.View;

import hrv.band.app.R;
import hrv.band.app.device.ConnectionManager;
import hrv.band.app.ui.view.fragment.IMeasuringView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 *
 * Click listener handling the clicks in this fragment.
 */
public class ConnectionClickListener implements View.OnClickListener {

    private ConnectionManager connectionManager;
    private IMeasuringView measuringView;

    public ConnectionClickListener(ConnectionManager connectionManager, IMeasuringView measuringView) {
        this.connectionManager = connectionManager;
        this.measuringView = measuringView;
    }

    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.connect_antplus_float_button:
                measuringView.setHrvRrIntervalDevice(connectionManager.connectToAnt());
                measuringView.addDeviceListeners();
                break;
            case R.id.connect_band_float_button:
                measuringView.setHrvRrIntervalDevice(connectionManager.connectToMSBand());
                measuringView.addDeviceListeners();
                break;
            case R.id.disconnect_devices:
                measuringView.setHrvRrIntervalDevice(connectionManager.disconnectDevices(measuringView.getHrvRrIntervalDevice()));
                measuringView.makeToast(R.string.msg_disconnecting);
                break;
            default:
                break;
        }
        measuringView.toggleDeviceMenuButton(true);*/
    }
}
