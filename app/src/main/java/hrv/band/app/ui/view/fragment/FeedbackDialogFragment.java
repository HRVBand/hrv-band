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
import hrv.band.app.ui.presenter.FeedbackPresenter;
import hrv.band.app.ui.presenter.IFeedbackPresenter;
import hrv.band.app.ui.view.adapter.FeedbackCategoryAdapter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Dialog asking the user for feedback.
 */
public class FeedbackDialogFragment extends DialogFragment implements IFeedbackDialogView {
    private static final String FEEDBACK_EMAIL = "feedback@hrvband.de";

    private View view;
    private IFeedbackPresenter presenter;

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
        presenter = new FeedbackPresenter(this);

        view = View.inflate(getActivity(), R.layout.dialog_feedback, null);
        setSpinnerValues(view);

        AlertDialog.Builder builder = buildDialog();
        return builder.create();
    }

    @NonNull
    private AlertDialog.Builder buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.feedback_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        startEmailActivity();
                    }
                })
                .setNegativeButton(R.string.feedback_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Feedback");
        return builder;
    }

    private void startEmailActivity() {
        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", FEEDBACK_EMAIL, null));
        email.putExtra(Intent.EXTRA_SUBJECT, presenter.getSubject());
        email.putExtra(Intent.EXTRA_TEXT, presenter.getMessage());
        startActivity(Intent.createChooser(email, "send feedback"));
    }

    /**
     * Sets the spinner values with possible subjects the user can choose.
     *
     * @param view te view holding the spinner.
     */
    private void setSpinnerValues(View view) {
        Spinner spinner = view.findViewById(R.id.feedback_category);
        spinner.setAdapter(new FeedbackCategoryAdapter(getActivity().getApplicationContext()));
    }

    @Override
    public String getSubjectText() {
        Spinner spinner = view.findViewById(R.id.feedback_category);
        return FeedbackCategoryAdapter.FeedbackCategory.values()[spinner.getSelectedItemPosition()].getText(getResources());
    }

    @Override
    public String getMessageText() {
        EditText text = view.findViewById(R.id.feedback_text);
        return text.getText().toString();
    }
}