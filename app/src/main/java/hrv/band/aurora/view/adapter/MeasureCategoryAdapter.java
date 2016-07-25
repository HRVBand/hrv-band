package hrv.band.aurora.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hrv.band.aurora.R;

/**
 * Created by Thomas on 25.07.2016.
 */
public class MeasureCategoryAdapter extends BaseAdapter {
    private Context context;

    public MeasureCategoryAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return MeasureCategory.values().length;
    }

    @Override
    public Object getItem(int position) {
        return MeasureCategory.values()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.measure_category_spinner_item, parent, false);
        TextView categoryTxt = (TextView) view.findViewById(R.id.measure_category_name);
        ImageView categoryIcon = (ImageView) view.findViewById(R.id.measure_category_icon);

        categoryTxt.setText(MeasureCategory.values()[position].getText(context.getResources()));
        categoryIcon.setImageDrawable(MeasureCategory.values()[position].getIcon(context.getResources()));
        return view;
    }

    public enum MeasureCategory {
        GENERAL(R.string.measure_category_general, R.drawable.ic_general),
        RELAXED(R.string.measure_category_relaxed, R.drawable.ic_relaxed),
        WORK(R.string.measure_category_work, R.drawable.ic_work),
        STRESSED(R.string.measure_category_stressed, R.drawable.ic_stressed),
        SPORT(R.string.measure_category_sport, R.drawable.ic_sport)
        ;

        private final int text;
        private final int icon;

        MeasureCategory(int text, int icon) {
            this.text = text;
            this.icon = icon;
        }

        public String getText(Resources resources) {
            return resources.getString(text);
        }

        public Drawable getIcon(Resources resources) {
            return ResourcesCompat.getDrawable(resources, icon, null);
        }
    }
}
