package hrv.band.aurora;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

import hrv.band.aurora.storage.SQLite.SQLController;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImportFragment extends DialogFragment {

    View view;

    public static ImportFragment newInstance() {
        return new ImportFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_import, null);

         builder.setView(view)
                .setPositiveButton(R.string.common_import, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String saveDir = getActivity().getFilesDir() + "/export.sql";
                        SQLController sql = new SQLController();

                        try {
                            sql.importDB(saveDir, getActivity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle("Export");
        return builder.create();
    }

}
