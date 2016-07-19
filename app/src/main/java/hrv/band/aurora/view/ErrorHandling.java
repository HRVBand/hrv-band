package hrv.band.aurora.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import hrv.band.aurora.RRInterval.IRRInterval;
import hrv.band.aurora.view.fragment.MeasuringFragment;

/**
 * Created by Thomas on 17.07.2016.
 */
public class ErrorHandling {


    public static void showSnackbar(final String msg) {
        final Snackbar snackBar = Snackbar.make(MeasuringFragment.view, msg, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }


    public static void updateTextView(Activity activity, final TextView txt, final String string) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt.setText(string);
            }
        });
    }
}
