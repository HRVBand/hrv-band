package hrv.band.app.view.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.view.ImprintActivity;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * First Fragment in {@link ImprintActivity} showing imprint.
 */
public class AboutFragment extends Fragment {

    /** The root view of this activity. **/
    private View rootView;

    /**
     * Creates a new instance of this fragment.
     * @return a new instance of this fragment.
     */
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.imprint_fragment_about, container, false);
        try {
            setVersion();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e.getClass().getName(), "NameNotFoundException", e);
        }

        return rootView;
    }

    /**
     * Sets the actual version of the app into a text view.
     */
    private void setVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(
                getActivity().getPackageName(), 0);
        TextView version = (TextView) rootView.findViewById(R.id.about_version);
        version.setText("Version: " + pInfo.versionName + " (" + pInfo.versionCode + ")");
    }
}
