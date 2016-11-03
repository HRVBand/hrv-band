package hrv.band.aurora.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

import hrv.band.aurora.R;
import hrv.band.aurora.storage.SQLite.SQLController;

public class ExportDatabaseFragment extends DialogFragment {

    public static ExportDatabaseFragment newInstance() {
        return new ExportDatabaseFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_export_database, null);

        builder.setView(view)
                .setPositiveButton(R.string.common_export, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                SQLController sql = new SQLController();
                try {
                    sql.exportDB("export.sql", getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })
        .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExportDatabaseFragment.this.getDialog().cancel();
            }
        });
        builder.setTitle("Export");
        return builder.create();
    }
}
