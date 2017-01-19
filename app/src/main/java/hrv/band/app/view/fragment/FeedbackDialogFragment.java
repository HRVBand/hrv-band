package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import hrv.band.app.R;
import hrv.band.app.view.adapter.AbstractCategoryAdapter;
import hrv.band.app.view.adapter.FeedbackCategoryAdapter;

/**
 * Created by Thomas on 27.07.2016.
 */
public class FeedbackDialogFragment extends DialogFragment {
    private static final String FEEDBACK_EMAIL = "hrvband+feedback@gmail.com";
    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view =  View.inflate(getActivity(), R.layout.feedback_fragment, null);

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
                    public void onClick(DialogInterface dialog, int id) {
                        FeedbackDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Feedback");
        setSpinnerValues(view);
        return builder.create();
    }

    private void setSpinnerValues(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        AbstractCategoryAdapter spinnerArrayAdapter = new FeedbackCategoryAdapter(getActivity().getApplicationContext());

        spinner.setAdapter(spinnerArrayAdapter);
    }

    private String getSubject() {
        if (view == null) {
            return "";
        }
        Spinner spinner = (Spinner) view.findViewById(R.id.feedback_category);
        String subject = FeedbackCategoryAdapter.FeedbackCategory.values()[spinner.getSelectedItemPosition()].getText(getResources());
        return "Feedback - [" + subject + "]";
    }

    private String getText() {
        EditText text = (EditText) view.findViewById(R.id.feedback_text);
        return text.getText() + "\n" + getDeviceInformation();
    }

    private String getDeviceInformation() {
        String info = "Debug-infos:";
        info += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        info += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        info += "\n Device: " + android.os.Build.DEVICE;
        info += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
        return info;
    }


}
