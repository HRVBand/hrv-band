package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hrv.band.app.R;

/**
 * Created by star_ on 02.11.2016.
 */

public class PrivacyFragment extends Fragment {

    public PrivacyFragment() {
    }

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imprint_privacy, container, false);
    }
}
