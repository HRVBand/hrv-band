package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hrv.band.app.R;

/**
 * Created by Julian on 24.01.2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_fragment);
    }
}
