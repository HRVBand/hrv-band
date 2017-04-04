package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import hrv.band.app.R;
import hrv.band.app.view.activity.IntroActivity;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 19.01.2017
 *
 * Dialog showing the user the disclaimer and asks for agreement.
 */
public class DisclaimerDialogFragment extends DialogFragment {

    public static final String DISCLAIMER_AGREEMENT = "DisclaimerAgreementAccepted";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());

        builder.setTitle(R.string.disclaimer)
            .setMessage(R.string.disclaimerText)
            .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putBoolean(DISCLAIMER_AGREEMENT, true);
                    prefsEditor.apply();

                    startTutorial();
                }
            })
            .setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        this.getActivity().finishAffinity();
    }

    /**
     * Starts the tutorial if the user accepted the disclaimer.
     */
    private void startTutorial() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(!sharedPreferences.getBoolean(IntroActivity.APP_INTRO, false)) {
            getActivity().startActivity(new Intent(getActivity(), IntroActivity.class));
        }
    }
}
