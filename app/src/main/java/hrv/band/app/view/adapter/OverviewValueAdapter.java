package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.view.fragment.OverviewFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Adapter for displaying available HRV values in the {@link OverviewFragment}.
 */
public class OverviewValueAdapter extends BaseAdapter {

    /** The context of the activity holding this adapter. **/
    private final Context context;

    public OverviewValueAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.overview_list_item, parent, false);
            holder = new ViewHolder();
            holder.firstLine = (TextView) convertView.findViewById(R.id.firstLine);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.firstLine.setText(HRVValue.values()[position].toString());
        return convertView;
    }

    @Override
    public int getCount() {
        return HRVValue.values().length;
    }

    @Override
    public Object getItem(int i) {
        return HRVValue.values()[i];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * The ViewHolder of this adapter.
     */
    private static class ViewHolder {
        private TextView firstLine;
    }
}
