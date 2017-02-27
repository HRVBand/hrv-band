package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.R;
import hrv.band.app.view.fragment.StatisticFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * This adapter holds the hrv parameters to show in the {@link StatisticFragment}.
 */
public class StatisticValueAdapter extends BaseAdapter {
    /** The context of activity holding the adapter. **/
    private final Context context;
    /** The hrv values to display in fragment as string. **/
    private List<String> values;
    /** The hrv parameters to display. **/
    private List<Measurement> parameters;
    /** The hrv type to display in the fragment. **/
    private final HRVValue type;

    public StatisticValueAdapter(Context context,
                                 HRVValue type, List<Measurement> parameters) {
        this.context = context;
        this.type = type;
        this.parameters = parameters;
        values = getValues(this.parameters, type);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.statistic_value_item, parent, false);
            holder = new ViewHolder();
            holder.value = (TextView) convertView.findViewById(R.id.stats_value);
            holder.time = (TextView) convertView.findViewById(R.id.stats_time);
            holder.category = (TextView) convertView.findViewById(R.id.stats_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        holder.time.setText(dateFormat.format(parameters.get(position).getTime()));
        holder.category.setText(parameters.get(position).getCategory().getText(context.getResources()));
        holder.value.setText(values.get(position));

        return convertView;
    }

    /**
     * Returns a list containing the value of the given hrv type of a hrv parameter.
     * @param parameters the parameter to extract the value.
     * @param type indicates which value to extract from parameter.
     * @return a list containing the value of the given hrv type of a hrv parameter.
     */
    private List<String> getValues(List<Measurement> parameters, HRVValue type) {
        List<String> hrvValues = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            double value = HRVValue.getHRVValue(type, parameters.get(i));
            hrvValues.add(new DecimalFormat("#.##").format(value));
        }
        return hrvValues;
    }

    /**
     * Sets a new set of parameters after something changed (e.g. delete).
     * @param parameters new set of parameters
     */
    public void setDataset(List<Measurement> parameters) {
        this.parameters = parameters;
        values = getValues(this.parameters, type);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return parameters.get(i);
    }

    @Override
    public int getCount() {
        return parameters.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * The ViewHolder of this adapter.
     */
    private static class ViewHolder {
        private TextView value;
        private TextView time;
        private TextView category;
    }
}
