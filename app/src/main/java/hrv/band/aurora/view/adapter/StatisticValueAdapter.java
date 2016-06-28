package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SharedPreferencesController;

/**
 * Created by Thomas on 28.06.2016.
 */
public class StatisticValueAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> values;
    private Date date;
    private String type;

    public StatisticValueAdapter(Context context, int textViewResourceId,
                                 String type) {
        this.layout = textViewResourceId;
        this.context = context;
        this.type = type;
        this.date = new Date();
        IStorage storage = new SharedPreferencesController();
        List<HRVParameters> parameters = storage.loadData(context, null);
        values = getValues(parameters, type);
    }

    private List<String> getValues(List<HRVParameters> parameters, String type) {
        List<String> values = new ArrayList<>();
        for (HRVParameters parameter : parameters) {
            if (type.equals("LFHF")) {
                values.add(trimValue(parameter.getLfhfRatio()));
            } else if (type.equals("SDNN")) {
                values.add(trimValue(parameter.getSdnn()));
            } else if (type.equals("SD1")) {
                values.add(trimValue(parameter.getSd1()));
            } else if (type.equals("SD2")) {
                values.add(trimValue(parameter.getSd2()));
            } else if (type.equals("Baevsky")) {
                values.add(trimValue(parameter.getBaevsky()));
            } else if (type.equals("RMSSD")) {
                values.add(trimValue(parameter.getRmssd()));
            }
        }
        return values;
    }

    private String trimValue(double value) {
        return new DecimalFormat("#.##").format(value);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);

        TextView value = (TextView) rowView.findViewById(R.id.stats_value);
        TextView date = (TextView) rowView.findViewById(R.id.stats_date);
        TextView type = (TextView) rowView.findViewById(R.id.stats_type);

        value.setText(values.get(position));
        date.setText(this.date.toString());
        type.setText(this.type);

        return rowView;
    }
}
