package hrv.band.app.ui.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hrv.band.app.R;
import hrv.band.app.model.Measurement;
import hrv.band.app.ui.view.activity.EditableMeasurementActivity;
import hrv.band.app.ui.view.activity.MainActivity;
import hrv.band.app.ui.view.util.DateUtil;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 17.09.2017
 */


public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.RecyclerViewHolder> {

    private List<Measurement> measurements;
    private Context context;
    private boolean setDate;

    public HistoryViewAdapter(List<Measurement> measurements, boolean setDate) {
        this.measurements = measurements;
        this.setDate = setDate;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_value_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final HistoryViewAdapter.RecyclerViewHolder holder, int position) {
        Measurement measurement = measurements.get(position);
        holder.time.setText(DateUtil.formatTime(context, measurements.get(position).getDate()));
        if (setDate) {
            holder.date.setText(DateUtil.formatDate(context, measurements.get(position).getDate(), "dd.MM"));
        }
        holder.categoryImage.setImageDrawable(measurement.getCategory().getIcon(context.getResources()));
        holder.category.setText(measurement.getCategory().getText(context.getResources()));
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
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView date;
        private ImageView categoryImage;
        private TextView category;

        RecyclerViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.stats_date);
            time = view.findViewById(R.id.stats_time);
            categoryImage = view.findViewById(R.id.stats_image);
            category = view.findViewById(R.id.stats_category);
        }
    }
}
