package hrv.band.aurora.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hrv.band.aurora.R;

/**
 * Created by star_ on 02.11.2016.
 */

public class PrivacyFragment extends Fragment {

    private View rootView;

    public PrivacyFragment() {
    }

    public static PrivacyFragment newInstance() {
        PrivacyFragment fragment = new PrivacyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_imprint_privacy, container, false);
        return rootView;
    }
}
