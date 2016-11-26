package hrv.band.app.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hrv.band.app.R;

/**
 * Created by star_ on 02.11.2016.
 */

public class LicenseAdapter extends AbstractValueAdapter {

    private Context context;
    private int layout;
    private String[] titles;
    private String[] links;
    private String[] texts;


    public LicenseAdapter(Context context, int textViewResourceId) {
        this.layout = textViewResourceId;
        this.context = context;
        Resources resources = context.getResources();
        titles = resources.getStringArray(R.array.license_title_array);
        links = resources.getStringArray(R.array.license_link_array);
        texts = resources.getStringArray(R.array.license_text_array);
    }
    @Override
    public Object getItem(int i) {
        return titles[i];
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(layout, viewGroup, false);
        TextView title = (TextView) rowView.findViewById(R.id.licence_title);
        TextView link = (TextView) rowView.findViewById(R.id.licence_link);
        TextView text = (TextView) rowView.findViewById(R.id.license_text);

        title.setText(titles[i]);
        link.setText(links[i]);
        text.setText(texts[i]);

        //TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);

        //setTextView(firstLine, HRVValue.values()[position].toString());
        //setTextView(secondLine, HRVValue.values()[position].getString(context.getResources()));
        return rowView;
    }
}
