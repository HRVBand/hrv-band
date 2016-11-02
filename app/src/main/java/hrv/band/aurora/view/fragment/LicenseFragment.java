package hrv.band.aurora.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import hrv.band.aurora.R;
import hrv.band.aurora.view.adapter.AbstractValueAdapter;
import hrv.band.aurora.view.adapter.LicenseAdapter;

/**
 * Created by star_ on 02.11.2016.
 */

public class LicenseFragment extends Fragment {

    public LicenseFragment() {
    }

    public static LicenseFragment newInstance() {
        LicenseFragment fragment = new LicenseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_imprint_license, container, false);

        ListView listview = (ListView) rootView.findViewById(R.id.licence_list);
        AbstractValueAdapter adapter = new LicenseAdapter(this.getActivity(),
                R.layout.license_item);
        listview.setAdapter(adapter);

        return rootView;
    }
}