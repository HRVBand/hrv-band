package hrv.band.app.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import hrv.band.app.R;
import hrv.band.app.storage.SQLite.SQLController;


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
        view = inflater.inflate(R.layout.fragment_text_dialog, null);

        TextView textView = (TextView) view.findViewById(R.id.dialog_textview);
        textView.setText(getResources().getString(R.string.sentence_import_db));

         builder.setView(view)
                .setPositiveButton(R.string.common_import, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String saveDir = getActivity().getFilesDir() + "/export.sql";
                        SQLController sql = new SQLController();

                        try {
                            int duration = Toast.LENGTH_SHORT;

                            if(!sql.importDB(saveDir, getActivity())) {
                                CharSequence text = getResources().getText(R.string.sentence_import_failed);
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                                toast.show();
                            }
                            else {
                                CharSequence text = getResources().getText(R.string.sentence_import_worked);
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
                                toast.show();
                            }
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
        builder.setTitle(getResources().getString(R.string.common_import));
        return builder.create();
    }

}
