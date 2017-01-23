package hrv.band.app.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import hrv.band.app.R;
import hrv.band.app.view.adapter.LicenseAdapter;


/**
 * Copyright (c) 2017
 * Created by Julian Martin on 19.01.2017
 *
 * Fragment holding the licenses of the used libaries.
 */
public class LicenseFragment extends Fragment {

    /**
     * Returns a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static LicenseFragment newInstance() {
        return new LicenseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imprint_fragment_license, container, false);

        ListView listview = (ListView) rootView.findViewById(R.id.licence_list);
        LicenseAdapter adapter = new LicenseAdapter(this.getActivity());
        listview.setAdapter(adapter);

        return rootView;
    }
}