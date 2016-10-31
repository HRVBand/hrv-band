package hrv.band.aurora.view;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import hrv.band.aurora.R;

/**
 * Created by Julian on 30.10.2016.
 */

public class DialogLicence extends DialogFragment {

    private String licenceText;

    public void setLicenceText(String licenceText) {
        this.licenceText = licenceText;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_licence, null);
        TextView text = (TextView)view.findViewById(R.id.licenceText);
        text.setText(licenceText);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)

                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogLicence.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
