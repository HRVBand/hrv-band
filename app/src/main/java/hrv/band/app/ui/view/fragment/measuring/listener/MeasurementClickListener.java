package hrv.band.app.ui.view.fragment.measuring.listener;

import android.view.View;

import hrv.band.app.R;
import hrv.band.app.device.HRVRRIntervalDevice;
import hrv.band.app.ui.view.fragment.IMeasuringView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 *
 * Click listener handling the clicks in this fragment.
 */
public class MeasurementClickListener implements View.OnClickListener {

    private IMeasuringView measuringView;

    public MeasurementClickListener(IMeasuringView measuringView) {
        this.measuringView = measuringView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.progressBar) {
            startMeasurement();
        }
    }
    private void startMeasurement() {
        if (measuringView.isAnimationRunning()) {
            measuringView.showCancelDialog();
        } else {
            final HRVRRIntervalDevice hrvRrIntervalDevice = measuringView.getHrvRrIntervalDevice();
            if (hrvRrIntervalDevice != null) {
                hrvRrIntervalDevice.tryStartRRIntervalMeasuring();
            } else {
                measuringView.makeToast(R.string.msg_select_device);
                measuringView.toggleDeviceMenuButton(true);
            }
        }
    }
}
