package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hrv.band.app.R;
import hrv.band.app.view.ImprintActivity;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Fragment showing the disclaimer in the {@link ImprintActivity}.
 */
public class DisclaimerFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static DisclaimerFragment newInstance() {
        return new DisclaimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_imprint_disclaimer, container, false);
    }
}
