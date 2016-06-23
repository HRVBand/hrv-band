package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;

/**
 * Created by Thomas on 20.06.2016.
 */
public class ValueAdapter extends BaseAdapter {
    private Context context;
    private List<String> labels;
    private String[] values = new String[] { "LFHF", "SDNN", "RMSSD",
            "SD1", "SD2", "Baevsky"};
    private int layout;
    private HRVParameters parameter;

    public ValueAdapter(Context context, int textViewResourceId, HRVParameters parameter) {
        this.layout = textViewResourceId;
        this.context = context;
        this.parameter = parameter;
    }

    public ValueAdapter(Context context, int textViewResourceId) {
        this.layout = textViewResourceId;
        this.context = context;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int i) {
        return parameter;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        firstLine.setText(values[position]);
        if(parameter != null) {
            secondLine.setText(getHRVValue(values[position]));
        }
        return rowView;
    }

    private String getHRVValue(String value) {
        if (value.equals("LFHF")) {
            return String.valueOf(parameter.getLfhfRatio());
        } else if (value.equals("SDNN")) {
            return String.valueOf(parameter.getSdnn());
        } else if (value.equals("SD1")) {
            return String.valueOf(parameter.getSd1());
        } else if (value.equals("SD2")) {
            return String.valueOf(parameter.getSd2());
        } else if (value.equals("Baevsky")) {
            return String.valueOf(parameter.getBaevsky());
        } else if (value.equals("RMSSD")) {
            return String.valueOf(parameter.getRmssd());
        }
        return "";
    }
}
