package hrv.band.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import hrv.band.app.R;
import hrv.band.app.view.fragment.SampleDataFragment;

/**
 * Created by thomcz on 10.01.2017.
 */

public class IntroActivity extends AppIntro {

    public static final String APP_INTRO = "app_intro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();
        int introBackgroundColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);

        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel0), resources.getString(R.string.tutorial_desc0), R.drawable.intro0, introBackgroundColor));
        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel1), resources.getString(R.string.tutorial_desc1), R.drawable.intro1, introBackgroundColor));
        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel2), resources.getString(R.string.tutorial_desc2), R.drawable.intro2, introBackgroundColor));
        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel3), resources.getString(R.string.tutorial_desc3), R.drawable.intro3, introBackgroundColor));
        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel4), resources.getString(R.string.tutorial_desc4), R.drawable.intro4, introBackgroundColor));
        addSlide(AppIntroFragment.newInstance(resources.getString(R.string.tutorial_titel5), resources.getString(R.string.tutorial_desc5), R.drawable.intro5, introBackgroundColor));

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

    private static void setPreference(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(APP_INTRO, true);
        prefsEditor.apply();
    }

    private void cancelTutorial() {
        CancelIntroFragment cancelIntroFragment = CancelIntroFragment.newInstance();
        cancelIntroFragment.show(getFragmentManager(), "dialog");
    }


    public static class CancelIntroFragment extends DialogFragment {

        public static CancelIntroFragment newInstance() {
            return new CancelIntroFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.fragment_text_dialog, null);

            TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
            textView.setText(getResources().getString(R.string.tutorial_cancel_desc));

            builder.setView(view)
                    .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            setPreference(getActivity());
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton(R.string.common_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelIntroFragment.this.getDialog().cancel();
                        }
                    });
            builder.setTitle(getResources().getString(R.string.tutorial_cancel_title));
            return builder.create();
        }
    }
}