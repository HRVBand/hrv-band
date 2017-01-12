package hrv.band.app.view.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.R;

/**
 * Created by star_ on 02.11.2016.
 */

public class AboutFragment extends Fragment {

    private View rootView;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_imprint_about, container, false);
        try {
            setVersion();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void setVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(
                getActivity().getPackageName(), 0);
        TextView version = (TextView) rootView.findViewById(R.id.about_version);
        version.setText("Version: " + pInfo.versionName + " (" + pInfo.versionCode + ")");
    }
}
