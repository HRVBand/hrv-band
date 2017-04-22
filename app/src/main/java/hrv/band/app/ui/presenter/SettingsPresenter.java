package hrv.band.app.ui.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import hrv.band.app.R;
import hrv.band.app.ui.view.fragment.ISettingsView;

/**
 * Copyright (c) 2017
 * Created by Thomas on 15.04.2017.
 */

public class SettingsPresenter implements ISettingsPresenter {
    private static final int EXPORT_DATABASE_REQUEST_ID = 200;

    private ISettingsView settingsView;


    public SettingsPresenter(ISettingsView settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public boolean hasFileWritePermission() {
        return ContextCompat.checkSelfPermission(settingsView.getSettingsActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * Tries to get the file write permission
     *
     * @return Returns false if permission could not be granted, true otherwise
     */
    @Override
    public boolean getFileWritePermission() {
        if (!canMakeSmores()) {
            return false;
        }

        if (hasFileWritePermission()) {
            return true;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(settingsView.getSettingsActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            settingsView.showSnackbar(R.string.settings_request_ext_write_message,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestWritePermission(EXPORT_DATABASE_REQUEST_ID);
                        }
                    });
        } else {
            requestWritePermission(EXPORT_DATABASE_REQUEST_ID);
        }

        return true;
    }

    private void requestWritePermission(int requestId) {
        ActivityCompat.requestPermissions(settingsView.getSettingsActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == EXPORT_DATABASE_REQUEST_ID
                && (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            settingsView.startExportFragment();
        }
    }

    @Override
    public boolean isRecordLengthValid(String recordLength) {
        boolean matchesNumber = recordLength.matches("[0-9]+");
        boolean matchesLength = recordLength.matches("[0-9]{2,6}+");

        if (!matchesNumber) {
            settingsView.showAlertDialog(R.string.invalid_input, R.string.invalid_measurement_length_character);
            return false;
        } else if(!matchesLength) {
            settingsView.showAlertDialog(R.string.invalid_input, R.string.settings_invalid_measurement_length);
            return false;
        }
        return true;
    }

    private boolean canMakeSmores() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }
}
