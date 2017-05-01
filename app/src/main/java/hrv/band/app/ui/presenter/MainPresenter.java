package hrv.band.app.ui.presenter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hrv.band.app.R;
import hrv.band.app.ui.view.activity.IMainView;
import hrv.band.app.ui.view.activity.ImprintActivity;
import hrv.band.app.ui.view.activity.IntroActivity;
import hrv.band.app.ui.view.activity.SettingsActivity;
import hrv.band.app.ui.view.activity.WebActivity;

import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_PRIVACY_URL;
import static hrv.band.app.ui.view.activity.web.WebsiteUrls.WEBSITE_URL;

/**
 * Copyright (c) 2017
 * Created by Thomas on 23.04.2017.
 */

public class MainPresenter implements IMainPresenter {
    private IMainView mainView;
    private SharedPreferences sharedPreferences;

    private static final String SURVEY_ID = "servey_id";

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainView.getMainActivity());
    }

    @Override
    public boolean agreedToDisclaimer() {
        return sharedPreferences.getBoolean(DisclaimerDialogPresenter.DISCLAIMER_AGREEMENT, false);
    }

    @Override
    public void handleNavBar(int id) {
        switch (id) {
            case R.id.menu_help:
                mainView.startActivity(IntroActivity.class);
                break;
            case R.id.menu_website:
                mainView.startActivity(WebActivity.class, WebActivity.WEBSITE_URL_ID, WEBSITE_URL);
                break;
            case R.id.menu_share:
                mainView.openShareIntent();
                break;
            case R.id.menu_privacy:
                mainView.startActivity(WebActivity.class, WebActivity.WEBSITE_URL_ID, WEBSITE_PRIVACY_URL);
                break;
            case R.id.menu_feedback:
                mainView.startFeedbackDialog();
                break;
            case R.id.menu_survey:
                mainView.startSurveyDialog();
                break;
            case R.id.menu_imprint:
                mainView.startActivity(ImprintActivity.class);
                break;
            case R.id.menu_rate:
                mainView.rateApp();
                break;
            case R.id.menu_Settings:
                mainView.startActivity(SettingsActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean askedForSurvey() {
        if (!sharedPreferences.getBoolean(SURVEY_ID, false)) {
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            prefsEditor.putBoolean(SURVEY_ID, true);
            prefsEditor.apply();
            return false;
        }
        return true;
    }
}
