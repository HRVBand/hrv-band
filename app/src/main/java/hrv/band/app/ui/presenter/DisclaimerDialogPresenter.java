package hrv.band.app.ui.presenter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Copyright (c) 2017
 * Created by Thomas on 29.04.2017.
 */

public class DisclaimerDialogPresenter implements IDisclaimerDialogPresenter {
    static final String DISCLAIMER_AGREEMENT = "DisclaimerAgreementAccepted";
    private Activity activity;

    public DisclaimerDialogPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void agreeToDisclaimer() {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(DISCLAIMER_AGREEMENT, true);
        prefsEditor.apply();
    }
}
