package hrv.band.aurora.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import java.util.Calendar;

import hrv.band.aurora.view.StatisticActivity;

/**
 * Created by Thomas on 03.07.2016.
 */
public class CalenderPickerFragment extends DialogFragment  {

    private View view;
    public CalenderPickerFragment() {}


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (StatisticActivity) getActivity(), year, month, day);
        }
}
