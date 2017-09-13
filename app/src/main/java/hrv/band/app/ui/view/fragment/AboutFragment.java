package hrv.band.app.ui.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.AboutPresenter;
import hrv.band.app.ui.presenter.IAboutPresenter;
import hrv.band.app.ui.view.activity.ImprintActivity;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * First Fragment in {@link ImprintActivity} showing imprint.
 */
public class AboutFragment extends Fragment {

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imprint_fragment_about, container, false);
        IAboutPresenter presenter = new AboutPresenter(getActivity());

        TextView version = rootView.findViewById(R.id.about_version);
        version.setText(presenter.getVersion());

        return rootView;
    }
}
