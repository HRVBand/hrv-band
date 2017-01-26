package hrv.band.app.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hrv.band.app.view.fragment.SettingsFragment;

/**
 * Preferences are just one single fragment that contain all options grouped by
 * category
 *
 * Created by Julian on 24.01.2017.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Replace Activity with settings fragment content
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
