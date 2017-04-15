package hrv.band.app.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import hrv.band.app.R;
import hrv.band.app.view.presenter.ISettingsPresenter;
import hrv.band.app.view.presenter.SettingsPresenter;

/**
 * Copyright (c) 2017
 * Created by Julian on 24.01.2017.
 *
 * Handles the corresponding settings fragment that
 * contains the view for all settings of the HRV-Band app
 */

public class SettingsFragment extends PreferenceFragment implements ISettingsView {

    private ISettingsPresenter settingsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);

        settingsPresenter = new SettingsPresenter(this);

        addExportSetting();
        addSetRecordingLengthSetting();
        addCreateSampleSetting();
    }

    private void addExportSetting() {
        Preference exportPreference = getPreferenceManager().findPreference("settings_export");
        if (exportPreference != null) {
            exportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (settingsPresenter.hasFileWritePermission()) {
                        startExportFragment();
                        return true;
                    } else {
                        if (settingsPresenter.getFileWritePermission()) {
                            startExportFragment();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void addSetRecordingLengthSetting() {
        getPreferenceManager().findPreference("recording_length").setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        return settingsPresenter.isRecordLengthValid((String) o);
                    }
                }
        );
    }


    private void addCreateSampleSetting() {
        getPreferenceManager().findPreference("settings_sample").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SampleDataFragment.newInstance(false).show(getFragmentManager(), getResources().getString(R.string.common_import));
                return false;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        settingsPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void startExportFragment() {
        ExportFragment.newInstance().show(getFragmentManager(), getResources().getString(R.string.common_export));
    }

    @Override
    public Activity getSettingsActivity() {
        return getActivity();
    }

    @Override
    public void showSnackbar(int messageId, View.OnClickListener clickListener) {
        View view = getView();
        if (view == null) {
            return;
        }

        Snackbar.make(view, messageId,
                Snackbar.LENGTH_INDEFINITE).setAction(R.string.common_ok, clickListener).show();
    }

    @Override
    public void showAlertDialog(int titleId, int messageId) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(titleId);
        alert.setMessage(messageId);
        alert.setPositiveButton(android.R.string.ok, null);
        alert.show();
    }

}
