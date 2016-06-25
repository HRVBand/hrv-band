package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;

/**
 * Created by Thomas on 20.06.2016.
 */
public class ValueAdapter extends AbstractValueAdapter {

    private Context context;
    private int layout;
    private HRVParameters parameter;

    public ValueAdapter(Context context, int textViewResourceId, HRVParameters parameter) {
        super(context);
        this.layout = textViewResourceId;
        this.context = context;
        this.parameter = parameter;
    }

    @Override
    public Object getItem(int i) {
        return parameter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        setTextView(firstLine, getValues()[position]);
        if(parameter != null) {
            setTextView(secondLine, getHRVValue(getValues()[position]));
        }
        return rowView;
    }

    private String getHRVValue(String value) {
        if (value.equals("LFHF")) {
            return trimValue(parameter.getLfhfRatio()) + " %";
        } else if (value.equals("SDNN")) {
            return trimValue(parameter.getSdnn()) + " ms";
        } else if (value.equals("SD1")) {
            return trimValue(parameter.getSd1()) + " ms";
        } else if (value.equals("SD2")) {
            return trimValue(parameter.getSd2()) + " ms";
        } else if (value.equals("Baevsky")) {
            return trimValue(parameter.getBaevsky()) + " %";
        } else if (value.equals("RMSSD")) {
            return trimValue(parameter.getRmssd()) + " ms";
        }
        return "";
    }

    private String trimValue(double value) {
        return new DecimalFormat("#.##").format(value);
    }
}
