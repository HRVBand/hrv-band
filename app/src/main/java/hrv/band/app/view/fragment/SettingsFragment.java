package hrv.band.app.view.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import hrv.band.app.R;

/**
 * Handles the corresponding settings fragment that
 * contains the view for all settings of the HRV-Band app
 * Created by Julian on 24.01.2017.
 */

public class SettingsFragment extends PreferenceFragment {

    private static final int EXPORT_DATABASE_REQUEST_ID = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);

        Preference exportPreference = getPreferenceManager().findPreference("settings_export");
        if (exportPreference != null) {
            exportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (hasFileWritePermission()) {
                        startExportFragment();
                        return true;
                    } else {
                        if(getFileWritePermission(EXPORT_DATABASE_REQUEST_ID)) {
                            startExportFragment();
                            return true;
                        }
                    }

                    return false;
                }
            });
        }

        //Adds the logic that checks whether the user input for
        //the measurement time is valid.
        getPreferenceManager().findPreference("recording_length").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        String newRecordLength = (String) o;
                        boolean integerParseResult = newRecordLength.matches("[0-9]+");

                        if (!integerParseResult) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle(R.string.invalid_input);
                            alert.setMessage(R.string.invalid_measurement_length_input);
                            alert.setPositiveButton(android.R.string.ok, null);
                            alert.show();
                        }

                        return integerParseResult;
                    }
                }
        );

        getPreferenceManager().findPreference("settings_sample").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SampleDataFragment.newInstance(false).show(getFragmentManager(), getResources().getString(R.string.common_import));
                return false;
            }
        });
    }

    private boolean canMakeSmores() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    /**
     * Tries to get the file write permission
     *
     * @param requestId Id specifies the usage that the permission is needed for.
     * @return Returns false if permission could not be granted, true otherwise
     */
    private boolean getFileWritePermission(final int requestId) {
        if (!canMakeSmores()) {
            return false;
        }

        if (hasFileWritePermission()) {
            return true;
        }

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            View view = getView();
            if (view == null)
                return false;

            Snackbar.make(view, R.string.settings_request_ext_write_message,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.common_ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestId);
                        }
                    }).show();


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestId);
        }

        return true;
    }


    private boolean hasFileWritePermission() {
        return ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == EXPORT_DATABASE_REQUEST_ID
                && (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            // permission was granted, yay! Export the Database
            startExportFragment();
        }
    }

    private void startExportFragment() {
        ExportFragment.newInstance().show(getFragmentManager(), getResources().getString(R.string.common_export));
    }

}
