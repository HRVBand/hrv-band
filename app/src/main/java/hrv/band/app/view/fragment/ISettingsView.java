package hrv.band.app.view.fragment;

import android.app.Activity;
import android.view.View;

/**
 * Copyright (c) 2017
 * Created by Thomas on 15.04.2017.
 */

public interface ISettingsView {
    Activity getSettingsActivity();

    void showSnackbar(int messageId, View.OnClickListener clickListener);

    void showAlertDialog(int titleId, int messageId);

    void startExportFragment();
}
