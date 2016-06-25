package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.BaseAdapter;
import android.widget.TextView;

import hrv.band.aurora.R;

/**
 * Created by Thomas on 25.06.2016.
 */
public abstract class AbstractValueAdapter extends BaseAdapter {

    private String[] values;

    public AbstractValueAdapter(Context context) {
        Resources res = context.getResources();
        values = res.getStringArray(R.array.value_names);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public String[] getValues() {
        return values;
    }

    public void setTextView(TextView view, String value) {
        view.setText(value);
    }
}
