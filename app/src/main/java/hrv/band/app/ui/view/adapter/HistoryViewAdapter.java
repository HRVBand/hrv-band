package hrv.band.app.ui.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import hrv.band.app.ui.view.activity.EditableMeasurementActivity;
import hrv.band.app.ui.view.activity.MainActivity;
import hrv.calc.parameter.HRVParameter;
import hrv.calc.parameter.HRVParameterEnum;
import units.TimeUnit;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 17.09.2017
 */


public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.RecyclerViewHolder> {

    private HRVParameterEnum type;
    private List<Measurement> measurements;
    /**
     * The hrv values to display in fragment as string.
     **/
    private List<String> values;
    private Context context;

    public HistoryViewAdapter(HRVParameterEnum type, List<Measurement> measurements) {
        this.type = type;
        this.measurements = measurements;
        values = getValues(this.measurements, type);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistic_value_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final HistoryViewAdapter.RecyclerViewHolder holder, int position) {
        Measurement measurement = measurements.get(position);
        holder.time.setText(formatDateTime(measurements.get(position).getDate()));
        holder.value.setText(values.get(position));
        holder.category.setText(measurement.getCategory().toString());
        holder.itemView.setTag(measurement);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditableMeasurementActivity.class);
                intent.putExtra(MainActivity.HRV_PARAMETER_ID, measurements.get(holder.getAdapterPosition()));
                intent.putExtra(MainActivity.HRV_PARAMETER_ID_ID, measurements.get(holder.getAdapterPosition()).getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }

    public void addItems(List<Measurement> measurements) {
        this.measurements = measurements;
        values = getValues(this.measurements, type);
        notifyDataSetChanged();
    }

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

    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView value;
        private TextView time;
        private TextView category;

        RecyclerViewHolder(View view) {
            super(view);
            value = view.findViewById(R.id.stats_value);
            time = view.findViewById(R.id.stats_time);
            category = view.findViewById(R.id.stats_category);
        }
    }
}
