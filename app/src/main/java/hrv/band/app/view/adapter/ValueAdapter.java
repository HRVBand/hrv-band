package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

import hrv.band.app.control.HRVParameters;
import hrv.band.app.R;
import hrv.band.app.view.fragment.MeasuredValueFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This adapter holds the hrv parameters to show in the {@link MeasuredValueFragment}.
 */
public class ValueAdapter extends BaseAdapter {

    /** The context of activity holding the adapter. **/
    private final Context context;
    /** The hrv parameter to display. **/
    private final HRVParameters parameter;

    public ValueAdapter(Context context, HRVParameters parameter) {
        this.context = context;
        this.parameter = parameter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.measure_list_item, parent, false);
            holder = new ViewHolder();
            holder.descText = (TextView) convertView.findViewById(R.id.measure_value_desc);
            holder.valueText = (TextView) convertView.findViewById(R.id.hrv_value);
            holder.unitText = (TextView) convertView.findViewById(R.id.measure_value_unit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.descText.setText(HRVValue.values()[position].toString());
        if (parameter != null) {
            double value = HRVValue.getHRVValue(HRVValue.values()[position], parameter);
            holder.valueText.setText(new DecimalFormat("#.##").format(value));
            holder.unitText.setText(HRVValue.values()[position].getUnit());
        }

        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return parameter;
    }

    @Override
    public int getCount() {
        return HRVValue.values().length;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * The ViewHolder of this adapter.
     */
    private static class ViewHolder {
        private TextView descText;
        private TextView valueText;
        private TextView unitText;
    }
}

