package hrv.band.app.ui.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hrv.band.app.R;
import hrv.band.app.ui.view.fragment.LicenseFragment;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * This adapter handel's the licenses in the {@link LicenseFragment}.
 */
public class LicenseAdapter extends BaseAdapter {

    /**
     * Context of Activity holding this adapter.
     **/
    private final Context context;
    /**
     * The titles of one license.
     **/
    private final String[] titles;
    /**
     * The link to the used libary.
     **/
    private final String[] links;
    /**
     * The license text.
     **/
    private final String[] texts;

    public LicenseAdapter(Context context) {
        this.context = context;
        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.license_title_array);
        links = resources.getStringArray(R.array.license_link_array);
        texts = resources.getStringArray(R.array.license_text_array);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.license_item, viewGroup, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.licence_title);
            holder.link = (TextView) convertView.findViewById(R.id.licence_link);
            holder.text = (TextView) convertView.findViewById(R.id.license_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(titles[i]);
        holder.link.setText(links[i]);
        holder.text.setText(texts[i]);

        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return titles[i];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return titles.length;
    }


    /**
     * The ViewHolder of this adapter.
     */
    private static class ViewHolder {
        private TextView title;
        private TextView link;
        private TextView text;
    }
}
