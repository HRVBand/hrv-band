package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import hrv.band.app.R;

/**
 * Handles the corresponding settings fragment that
 * contains the view for all settings of the HRV-Band app
 * Created by Julian on 24.01.2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_fragment);

        Preference exportPreference = getPreferenceManager().findPreference("settings_export");
        if (exportPreference != null) {
            exportPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ExportFragment.newInstance().show(getFragmentManager(), getResources().getString(R.string.common_export));
                    return true;
                }
            });
        }

        Preference importPreference = getPreferenceManager().findPreference("settings_import");
        if (importPreference != null) {
            importPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    ImportFragment.newInstance().show(getFragmentManager(), getResources().getString(R.string.common_import));
                    return true;
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
    }
}
