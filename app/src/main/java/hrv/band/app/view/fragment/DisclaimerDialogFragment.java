package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import hrv.band.app.R;

/**
 * Created by Julian on 15.11.2016.
 */

public class DisclaimerDialogFragment extends DialogFragment {

    public static final String DISCLAIMER_AGREEMENT = "DisclaimerAgreementAccepted";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.disclaimer)
            .setMessage(R.string.disclaimerText)
            .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putBoolean(DISCLAIMER_AGREEMENT, true);
                    prefsEditor.apply();
                }
            })
            .setNegativeButton(R.string.disagree, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });

        return builder.create();
    }
}
