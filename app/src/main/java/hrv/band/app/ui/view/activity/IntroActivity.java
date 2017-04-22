package hrv.band.app.ui.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;

import hrv.band.app.R;
import hrv.band.app.ui.view.fragment.IntroFragment;
import hrv.band.app.ui.view.fragment.SampleDataFragment;
import hrv.band.app.ui.view.fragment.TextDialogFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This Activity shows a tutorial to the user.
 */
public class IntroActivity extends AppIntro {

    /**
     * Key value for tutorial if it should start at app start.
     **/
    public static final String APP_INTRO = "app_intro";

    /**
     * Sets the APP_INTRO preference.
     *
     * @param context of this activity.
     */
    private static void setPreference(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(APP_INTRO, true);
        prefsEditor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();

        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_welcome_title), resources.getString(R.string.tutorial_welcome_desc), R.drawable.hrv_logo));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_connecting_title), resources.getString(R.string.tutorial_connecting_desc), R.drawable.intro_connecting));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_measurement_title), resources.getString(R.string.tutorial_measurement_desc), R.drawable.intro_measurement));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_saving_title), resources.getString(R.string.tutorial_saving_desc), R.drawable.intro_saving));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_personalize_title), resources.getString(R.string.tutorial_personalize_desc), R.drawable.intro_personalize));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_values_title), resources.getString(R.string.tutorial_values_desc), R.drawable.intro_values));
        addSlide(IntroFragment.newInstance(resources.getString(R.string.tutorial_monitor_title), resources.getString(R.string.tutorial_monitor_desc), R.drawable.intro_monitor));

        setBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        cancelTutorial();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        cancelTutorial();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!sharedPreferences.getBoolean(IntroActivity.APP_INTRO, false)) {
            setPreference(getApplicationContext());

            SampleDataFragment sampleDataFragment = SampleDataFragment.newInstance(true);
            sampleDataFragment.show(getFragmentManager(), "dialog");
        } else {
            finish();
        }
    }

    /**
     * Opens a cancel dialog.
     */
    private void cancelTutorial() {
        CancelIntroFragment.newInstance().show(getSupportFragmentManager(), "dialog");
    }


    /**
     * Dialog which asks the user if he wants to cancel the tutorial.
     */
    public static class CancelIntroFragment extends TextDialogFragment {

        public static TextDialogFragment newInstance() {
            return new CancelIntroFragment();
        }

        @Override
        public void positiveButton() {
            setPreference(getActivity());
            getActivity().finish();
        }

        @Override
        public void negativeButton() {
            CancelIntroFragment.this.getDialog().cancel();
        }

        @Override
        public String getDialogTitle() {
            return getResources().getString(R.string.tutorial_cancel_title);
        }

        @Override
        public String getDialogDescription() {
            return getResources().getString(R.string.tutorial_cancel_desc);
        }

        @Override
        public int getDialogPositiveLabel() {
            return R.string.common_yes;
        }

        @Override
        public int getDialogNegativeLabel() {
            return R.string.common_no;
        }
    }
}