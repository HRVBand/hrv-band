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

    public static DisclaimerFragment newInstance() {
        return new DisclaimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imprint_disclaimer, container, false);
        return rootView;
    }
}
