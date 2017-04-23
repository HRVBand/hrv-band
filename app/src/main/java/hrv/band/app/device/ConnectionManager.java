package hrv.band.app.device;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import hrv.band.app.device.antplus.AntPlusRRDataDevice;
import hrv.band.app.device.msband.MSBandRRIntervalDevice;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 23.04.2017
 */
public class ConnectionManager {
    private Activity activity;
    private SharedPreferences sharedPreferences;

    private static final String SELECTED_DEVICE_ID = "selected_device_id";

    public ConnectionManager(Activity activity) {
        this.activity = activity;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    /**
     * Connects to ms band.
     */
    public HRVRRIntervalDevice connectToMSBand() {
        setDevice(Device.MSBAND);
        return connectToIntervalDevice();
    }

    @NonNull
    private HRVRRIntervalDevice connectToIntervalDevice() {
        HRVRRIntervalDevice hrvRRIntervalDevice = getDevice();
        hrvRRIntervalDevice.connect();
        return hrvRRIntervalDevice;
    }

    /**
     * Connects to ant+ device.
     */
    public HRVRRIntervalDevice connectToAnt() {
        setDevice(Device.ANT);
        return connectToIntervalDevice();
    }

    /**
     * Disconnects connected device.
     */
    public HRVRRIntervalDevice disconnectDevices(HRVRRIntervalDevice hrvRRIntervalDevice) {
        setDevice(Device.NONE);
        if (hrvRRIntervalDevice != null) {
            hrvRRIntervalDevice.notifyDeviceStatusChanged(HRVDeviceStatus.DISCONNECTED);
        }
        return null;
    }

    public HRVRRIntervalDevice getDevice() {
        Device device = Device.values()[sharedPreferences.getInt(SELECTED_DEVICE_ID, 0)];
        switch (device) {
            case MSBAND:
                return new MSBandRRIntervalDevice(activity);
            case ANT:
                return new AntPlusRRDataDevice(activity);
            default:
                return null;
        }
    }

    private void setDevice(Device device) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(SELECTED_DEVICE_ID, device.ordinal());
        prefsEditor.apply();
    }
}
