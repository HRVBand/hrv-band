package hrv.band.app.view;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.view.fragment.MeasuringFragment;

/**
 * Created by Thomas on 17.07.2016.
 */
public class UiHandlingUtil {


    public static void showSnackbar(final String msg) {
        final Snackbar snackBar = Snackbar.make(MeasuringFragment.view, msg, Snackbar.LENGTH_INDEFINITE);
        String closeStr = snackBar.getView().getResources().getString(R.string.common_close);

        snackBar.setAction(closeStr, new View.OnClickListener() {
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
