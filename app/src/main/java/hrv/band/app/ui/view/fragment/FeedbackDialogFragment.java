package hrv.band.app.ui.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import hrv.band.app.R;
import hrv.band.app.ui.view.adapter.FeedbackCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Dialog asking the user for feedback.
 */
public class FeedbackDialogFragment extends DialogFragment {
    private static final String FEEDBACK_EMAIL = "hrvband+feedback@gmail.com";
    private View view;

    /**
     * Returns a new instance of this fragment.
     *
     * @return a new instance of this fragment.
     */
    public static FeedbackDialogFragment newInstance() {
        return new FeedbackDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = View.inflate(getActivity(), R.layout.dialog_feedback, null);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", FEEDBACK_EMAIL, null));
                        email.putExtra(Intent.EXTRA_SUBJECT, getSubject());
                        email.putExtra(Intent.EXTRA_TEXT, getText());
                        startActivity(Intent.createChooser(email, "send feedback"));
                    }
                })
                .setNegativeButton(R.string.feedback_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Feedback");
        setSpinnerValues(view);
        return builder.create();
    }

    /**
     * Sets the spinner values with possible subjects the user can choose.
     *
     * @param view te view holding the spinner.
     */
    private void setSpinnerValues(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        spinner.setAdapter(new FeedbackCategoryAdapter(getActivity().getApplicationContext()));
    }

    /**
     * Returns the subject of the feedback the user entered.
     *
     * @return the subject of the feedback the user entered.
     */
    private String getSubject() {
        if (view == null) {
            return "";
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        String subject = FeedbackCategoryAdapter.FeedbackCategory.values()[spinner.getSelectedItemPosition()].getText(getResources());
        return "Feedback - [" + subject + "]";
    }

    /**
     * Returns the message of the user.
     *
     * @return the message of the user.
     */
    private String getText() {
        EditText text = (EditText) view.findViewById(R.id.feedback_text);
        return text.getText() + "\n" + getDeviceInformation();
    }

    /**
     * Returns the device information of the user phone.
     *
     * @return the device information of the user phone.
     */
    private String getDeviceInformation() {
        String info = "Debug-infos:";
        info += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        info += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        info += "\n Device: " + android.os.Build.DEVICE;
        info += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        return info;
    }


}
