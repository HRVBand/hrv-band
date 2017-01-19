package hrv.band.app.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.view.fragment.OverviewFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 *
 * Adapter for displaying available HRV values in the {@link OverviewFragment}.
 */
public class OverviewValueAdapter extends AbstractValueAdapter {

    private final Context context;
    private final int layout;

    public OverviewValueAdapter(Context context) {
        this.layout = R.layout.overview_list_item;
        this.context = context;
    }

    @Override
    public Object getItem(int i) {
        return HRVValue.values()[i];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, parent, false);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        //TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        setTextView(firstLine, HRVValue.values()[position].toString());
        //setTextView(secondLine, HRVValue.values()[position].getString(context.getResources()));
        return rowView;
    }
}
