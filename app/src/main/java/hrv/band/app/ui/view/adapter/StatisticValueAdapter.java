package hrv.band.app.ui.view.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import hrv.HRVLibFacade;
import hrv.RRData;
import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.fragment.OverviewFragment;
import hrv.calc.parameter.HRVParameter;
import hrv.calc.parameter.HRVParameterEnum;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This adapter holds the hrv parameters to show in the {@link OverviewFragment}.
 */
public class StatisticValueAdapter extends BaseAdapter {
    /**
     * The context of activity holding the adapter.
     **/
    private final Context context;
    /**
     * The hrv type to display in the fragment.
     **/
    private final HRVParameterEnum type;
    /**
     * The hrv values to display in fragment as string.
     **/
    private List<String> values;
    /**
     * The hrv parameters to display.
     **/
    private List<Measurement> parameters;

    public StatisticValueAdapter(Context context,
                                 HRVParameterEnum type, List<Measurement> parameters) {
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
            convertView = inflater.inflate(R.layout.history_value_item, parent, false);
            holder = new ViewHolder();
            //holder.value = convertView.findViewById(R.id.stats_value);
            holder.time = convertView.findViewById(R.id.stats_time);
            holder.category = convertView.findViewById(R.id.stats_category);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.time.setText(formatDateTime(parameters.get(position).getDate()));
        holder.category.setText(parameters.get(position).getCategory().getText(context.getResources()));
        holder.value.setText(values.get(position));

        return convertView;
    }

    /**
     * Formats the date and time for the measurement depending on user local.
     *
     * @param date the date to format.
     * @return the formatted date and time for the measurement depending on user local.
     */
    private String formatDateTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", getCurrentLocale());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return dateFormat.format(date) + ", " + timeFormat.format(date);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    /**
     * Returns a list containing the value of the given hrv type of all given hrv parameters.
     *
     * @param measurements the parameters to extract the value from.
     * @param type         indicates which value to extract from the given parameters.
     * @return a list containing the values of the given hrv type of the given hrv parameters.
     */
    private List<String> getValues(List<Measurement> measurements, final HRVParameterEnum type) {

        List<String> hrvValues = new ArrayList<>();

        if (measurements == null) {
            return hrvValues;
        }

        for (Measurement measurement : measurements) {
            HRVLibFacade hrvCalc = new HRVLibFacade(RRData.createFromRRInterval(measurement.getRrIntervals(), TimeUnit.SECOND));
            if (!hrvCalc.validData()) {
                continue;
            }

            hrvCalc.setParameters(EnumSet.of(type));
            HRVParameter parameter = getParameter(hrvCalc.calculateParameters(), type);

            if (parameter != null) {
                hrvValues.add(new DecimalFormat("#.##").format(parameter.getValue()));
            }
        }

        return hrvValues;
    }

    private HRVParameter getParameter(List<HRVParameter> parameters, HRVParameterEnum hrvType) {
        for (HRVParameter parameter : parameters) {
            if (parameter.getType().equals(hrvType)) {
                return parameter;
            }
        }
        return null;
    }


    /**
     * Sets a new set of parameters after something changed (e.g. delete).
     *
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
        if (parameters == null) {
            return 0;
        }
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
