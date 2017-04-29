package hrv.band.app.ui.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.DisclaimerDialogPresenter;
import hrv.band.app.ui.presenter.IDisclaimerDialogPresenter;
import hrv.band.app.ui.view.activity.IntroActivity;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 19.01.2017
 * <p>
 * Dialog showing the user the disclaimer and asks for agreement.
 */
public class DisclaimerDialogFragment extends DialogFragment {

    private IDisclaimerDialogPresenter presenter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        presenter = new DisclaimerDialogPresenter(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());

        builder.setTitle(R.string.disclaimer)
                .setMessage(R.string.disclaimerText)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.agreeToDisclaimer();
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

    private void startTutorial() {
        getActivity().startActivity(new Intent(getActivity(), IntroActivity.class));
    }
}
