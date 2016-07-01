package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.R;
import hrv.band.aurora.storage.IStorage;
import hrv.band.aurora.storage.SQLite.SQLController;
import hrv.band.aurora.storage.SharedPreferencesController;

/**
 * Created by Thomas on 28.06.2016.
 */
public class StatisticValueAdapter extends AbstractValueAdapter {
    private Context context;
    private int layout;
    private List<String> values;
    private List<HRVParameters> parameters;
    private Date date;
    private HRVValue type;
    private final String dateFormat = "yyyy-MM-dd";
    private final String timeFormat = "hh:mm aa";

    public StatisticValueAdapter(Context context, int textViewResourceId,
                                 HRVValue type) {
        this.layout = textViewResourceId;
        this.context = context;
        this.type = type;
        this.date = new Date();
        //IStorage storage = new SharedPreferencesController();
        IStorage storage = new SQLController();
        parameters = storage.loadData(context, this.date);
        values = getValues(parameters, type);
    }

    private List<String> getValues(List<HRVParameters> parameters, HRVValue type) {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            values.add(getHRVValue(type, parameters.get(i)));
        }
        return values;
    }



    @Override
    public Object getItem(int i) {
        return values.get(i);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);

        TextView value = (TextView) rowView.findViewById(R.id.stats_value);
        TextView date = (TextView) rowView.findViewById(R.id.stats_date);
        TextView time = (TextView) rowView.findViewById(R.id.stats_time);
        TextView unit = (TextView) rowView.findViewById(R.id.stats_type);

        value.setText(values.get(position));
        DateFormat df = new DateFormat();
        Date parameterDate = parameters.get(position).getTime();
        date.setText(df.format(dateFormat, parameterDate));
        time.setText(df.format(timeFormat, parameterDate));

        unit.setText(type.getUnit());

        return rowView;
    }
}
