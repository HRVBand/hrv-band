package hrv.band.app.ui.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import hrv.band.app.R;
import hrv.band.app.ui.view.fragment.MeasuredParameterFragment;
import hrv.calc.parameter.HRVParameter;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This adapter holds the hrv parameters to show in the {@link MeasuredParameterFragment}.
 */
public class ValueAdapter extends BaseAdapter {

    /**
     * The context of activity holding the adapter.
     **/
    private final Context context;
    /**
     * The hrv parameter to display.
     **/
    private final List<HRVParameter> parameters;

    public ValueAdapter(Context context, List<HRVParameter> parameters) {
        this.context = context;
        this.parameters = parameters;
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

        if (parameters != null) {
            HRVParameter param = (HRVParameter) getItem(position);
            holder.descText.setText(param.getName());
            holder.valueText.setText(new DecimalFormat("#.##").format(param.getValue()));
            holder.unitText.setText(param.getUnit());
        }

        return convertView;
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
        private TextView descText;
        private TextView valueText;
        private TextView unitText;
    }
}

