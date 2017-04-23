package hrv.band.app.ui.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.ui.view.activity.IMainView;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This dialog allows the user to cancel the measurement.
 */
public class CancelMeasuringDialogFragment extends DialogFragment {

    public static CancelMeasuringDialogFragment newInstance() {
        return new CancelMeasuringDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view = View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.measure_cancel_desc));

        builder.setView(view)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Activity activity = getActivity();
                        if (activity instanceof IMainView) {
                            ((IMainView) getActivity()).stopMeasuring();
                        }
                    }
                })
                .setNegativeButton(R.string.common_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CancelMeasuringDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle(getResources().getString(R.string.measure_cancel_title));
        return builder.create();
    }
}
