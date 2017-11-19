package hrv.band.app.ui.view.fragment;

import android.app.Activity;

import hrv.band.app.device.HRVRRIntervalDevice;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 */

public interface IMeasuringView {
    Activity getParentActivity();

    void addDeviceListeners();

    HRVRRIntervalDevice getHrvRrIntervalDevice();

    void setHrvRrIntervalDevice(HRVRRIntervalDevice hrvRrIntervalDevice);

    void showCancelDialog();

    boolean isAnimationRunning();

    void startMeasurementActivity();

    void makeToast(int messageId);

    void showSnackbar(String s);

    void resetProgress();

    void setConnectionButtonClickable(boolean clickable);

    void stopMeasuring();
}
