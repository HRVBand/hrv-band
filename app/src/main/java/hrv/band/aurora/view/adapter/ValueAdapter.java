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
    private List<String> values;
    private int layout;
    private HRVParameters parameters;

    public ValueAdapter(Context context, int textViewResourceId, List<String> labels, List<String> values) {
        this.layout = textViewResourceId;
        this.context = context;
        this.labels = labels;
        this.values = values;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        firstLine.setText(labels.get(position));
        secondLine.setText(values.get(position));

        return rowView;
    }
}
