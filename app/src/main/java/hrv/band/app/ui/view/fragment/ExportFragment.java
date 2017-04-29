package hrv.band.app.ui.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hrv.band.app.R;
import hrv.band.app.ui.presenter.ExportPresenter;
import hrv.band.app.ui.presenter.IExportPresenter;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 19.01.2017
 * <p>
 * Dialog asking the user to export his data.
 */
public class ExportFragment extends DialogFragment implements IExportView {

    private IExportPresenter presenter;

    public static ExportFragment newInstance() {
        return new ExportFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        presenter = new ExportPresenter(this);

        final View view = View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sentence_export_db));

        AlertDialog.Builder builder = buildDialog(view);
        return builder.create();
    }

    @NonNull
    private AlertDialog.Builder buildDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.common_export, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.exportDatabase();
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExportFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle(getResources().getString(R.string.common_export));
        return builder;
    }

    @Override
    public void showToast(CharSequence message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getExportContext() {
        return getActivity();
    }
}
