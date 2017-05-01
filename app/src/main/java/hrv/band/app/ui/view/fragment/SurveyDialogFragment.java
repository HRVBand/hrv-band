package hrv.band.app.ui.view.fragment;

import android.content.Intent;

import hrv.band.app.R;
import hrv.band.app.ui.view.activity.WebActivity;
import hrv.band.app.ui.view.activity.web.WebsiteUrls;

/**
 * Copyright (c) 2017
 * Created by Thomas on 01.05.2017.
 */

public class SurveyDialogFragment extends TextDialogFragment {

    public static SurveyDialogFragment newInstance() {
        return new SurveyDialogFragment();
    }

    @Override
    public void positiveButton() {
        Intent intent = new Intent(getContext(), WebActivity.class);
        intent.putExtra(WebActivity.WEBSITE_URL_ID, WebsiteUrls.SURVEY_URL);
        startActivity(intent);
    }

    @Override
    public void negativeButton() {
        getDialog().cancel();
    }

    @Override
    public String getDialogTitle() {
        return getResources().getString(R.string.survey_title);
    }

    @Override
    public String getDialogDescription() {
        return getResources().getString(R.string.survey_desc);
    }

    @Override
    public int getDialogPositiveLabel() {
        return R.string.survey_take;
    }

    @Override
    public int getDialogNegativeLabel() {
        return R.string.survey_later;
    }
}
