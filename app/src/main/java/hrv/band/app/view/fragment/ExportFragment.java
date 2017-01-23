package hrv.band.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import hrv.band.app.R;
import hrv.band.app.storage.SQLite.SQLController;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 19.01.2017
 *
 * Dialog asking the user to export his data.
 */
public class ExportFragment extends DialogFragment {

    public static ExportFragment newInstance() {
        return new ExportFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View view =  View.inflate(getActivity(), R.layout.dialog_simple_text, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sentence_export_db));


        builder.setView(view)
                .setPositiveButton(R.string.common_export, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                exportDB();
            }
        })
        .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExportFragment.this.getDialog().cancel();
            }
        });
        builder.setTitle(getResources().getString(R.string.common_export));
        return builder.create();
    }

    /**
     * Exports the user database on the phone.
     */
    private void exportDB() {
        SQLController sql = new SQLController();
        try {
            int duration = Toast.LENGTH_SHORT;

            if(!sql.exportDB("export.sql", getActivity())) {
                CharSequence text = getResources().getText(R.string.sentence_export_failed);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                toast.show();
            }
            else {
                CharSequence text = getResources().getText(R.string.sentence_export_worked);
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                toast.show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
