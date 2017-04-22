package hrv.band.app.ui.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 06.02.2017
 *
 * Dialog with title, description and a positive and negative button.
 */
public abstract class TextDialogFragment extends DialogFragment {

    /**
     * Action to perform if the positive button is pressed.
     */
    public abstract void positiveButton();

    /**
     * Action to perform if the negative button is pressed.
     */
    public abstract void negativeButton();

    /**
     * Title of the dialog.
     * @return the title of the dialog.
     */
    public abstract String getDialogTitle();

    /**
     * The description of the dialog.
     * @return the description of the dialog.
     */
    public abstract String getDialogDescription();

    /**
     * The string id of the label of the positive button.
     * @return the string id of the label of the positive button.
     */
    public abstract int getDialogPositiveLabel();

    /**
     * The string id of the label of the negative button.
     * @return the string id of the label of the negative button.
     */
    public abstract int getDialogNegativeLabel();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view =  View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getDialogDescription());

        builder.setView(view)
                .setPositiveButton(getDialogPositiveLabel(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        positiveButton();
                    }
                })
                .setNegativeButton(getDialogNegativeLabel(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        negativeButton();
                    }
                });
        builder.setTitle(getDialogTitle());
        return builder.create();
    }
}
