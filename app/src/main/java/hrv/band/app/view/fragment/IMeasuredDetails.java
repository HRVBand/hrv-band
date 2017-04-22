package hrv.band.app.view.fragment;

import android.support.v4.app.Fragment;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */

public interface IMeasuredDetails {
    float getRating();
    MeasurementCategoryAdapter.MeasureCategory getCategory();
    String getNote();
    Fragment getFragment();
}
