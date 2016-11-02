package hrv.band.aurora.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.view.MainActivity;
import hrv.band.aurora.view.ValueDescriptionActivity;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.ValueAdapter;

/**
 * Created by star_ on 02.11.2016.
 */

public class AboutFragment extends Fragment {

    public AboutFragment() {
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imprint_about, container, false);

        return rootView;
    }
}
