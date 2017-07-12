package hrv.band.app.ui.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.ISampleDataPresenter;
import hrv.band.app.ui.presenter.SampleDataPresenter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Dialog asking user to create sample data.
 */
public class SampleDataFragment extends DialogFragment {

    private ISampleDataPresenter presenter;

    /**
     * Key value that indicates if the activity calling this dialog should be closed.
     **/
    private static final String ARG_CLOSE_VALUE = "arg_close_value";

    /**
     * Returns a new instance of this fragment.
     *
     * @param close true if calling activity should be closed after dialog closes, false otherwise.
     * @return a new instance of this fragment.
     */
    public static SampleDataFragment newInstance(boolean close) {
        SampleDataFragment fragment = new SampleDataFragment();

        Bundle args = new Bundle();
        args.putBoolean(ARG_CLOSE_VALUE, close);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        presenter = new SampleDataPresenter(getActivity().getApplicationContext());

        final View view = View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sample_text));

        builder.setView(view)
                .setPositiveButton(R.string.common_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.createSampleData();
                        closeCallingActivity();
                    }
                })
                .setNegativeButton(R.string.common_no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SampleDataFragment.this.getDialog().cancel();
                        closeCallingActivity();
                    }
                });
        builder.setTitle(getResources().getString(R.string.sample_title));
        return builder.create();
    }

    /**
     * Closes the calling activity if the context fits.
     */
    private void closeCallingActivity() {
        if (getArguments().getBoolean(ARG_CLOSE_VALUE)) {
            getActivity().finish();
        }
    }
}
