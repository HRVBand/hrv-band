package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.Control.HRVParameters;
import hrv.band.app.R;

/**
 * Created by Thomas on 20.06.2016.
 */
public class ValueAdapter extends AbstractValueAdapter {

    private final Context context;
    private final int layout;
    private final HRVParameters parameter;

    public ValueAdapter(Context context, HRVParameters parameter) {
        this.layout = R.layout.measure_list_item;
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
        TextView desc = (TextView) rowView.findViewById(R.id.measure_value_desc);
        TextView value = (TextView) rowView.findViewById(R.id.hrv_value);
        TextView unit = (TextView) rowView.findViewById(R.id.measure_value_unit);

        setTextView(desc, HRVValue.values()[position].toString());
        if (parameter != null) {
            //setTextView(secondLine, getHRVValue(getValues()[position], parameter));
            setTextView(value, trimValue(getHRVValue(HRVValue.values()[position], parameter)));
            setTextView(unit, HRVValue.values()[position].getUnit());
        }
        return rowView;
    }
}
