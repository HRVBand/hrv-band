package hrv.band.app.ui.view.activity;

import android.app.Activity;

/**
 * Copyright (c) 2017
 * Created by Thomas on 23.04.2017.
 */

public interface IMainView {
    void startActivity(Class<? extends Activity> activity);

    void startActivity(Class<? extends Activity> activity, String extraId, String extra);

    void startFeedbackDialog();

    void startSurveyDialog();

    Activity getMainActivity();

    void openShareIntent();

    void rateApp();

    void stopMeasuring();

}
