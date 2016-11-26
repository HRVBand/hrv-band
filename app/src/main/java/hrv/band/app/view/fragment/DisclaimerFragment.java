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

public class DisclaimerFragment extends Fragment {

    private View rootView;

    public DisclaimerFragment() {
    }

    public static DisclaimerFragment newInstance() {
        DisclaimerFragment fragment = new DisclaimerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_imprint_disclaimer, container, false);
        return rootView;
    }
}
