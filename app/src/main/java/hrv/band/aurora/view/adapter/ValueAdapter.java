package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hrv.band.aurora.R;

/**
 * Created by Thomas on 20.06.2016.
 */
public class ValueAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> values;
    private int layout;

    public ValueAdapter(Context context, int textViewResourceId, List<String> values) {
        super(context, textViewResourceId, values);
        this.layout = textViewResourceId;
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        firstLine.setText(values.get(position));
        //secondLine.setText("pp");

        return rowView;
    }
}
