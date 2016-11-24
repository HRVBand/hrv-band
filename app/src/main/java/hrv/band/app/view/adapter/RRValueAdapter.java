package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hrv.band.app.R;

/**
 * Created by star_ on 12.10.2016.
 */

public class RRValueAdapter extends AbstractValueAdapter {

    private Context context;
    private int layout;
    private ArrayList<Double> rrValues;

    public RRValueAdapter(Context context, int textViewResourceId, ArrayList<Double> rrValues) {
        this.layout = textViewResourceId;
        this.context = context;
        this.rrValues = rrValues;
    }

    @Override
    public int getCount() {
        return rrValues.size();
    }


    @Override
    public Object getItem(int i) {
        return rrValues.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, viewGroup, false);
        TextView rrValue = (TextView) rowView.findViewById(R.id.rr_value);

        setTextView(rrValue, trimValue(rrValues.get(i)));
        return rowView;
    }
}
