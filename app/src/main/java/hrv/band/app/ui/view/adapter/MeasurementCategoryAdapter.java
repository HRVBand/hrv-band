package hrv.band.app.ui.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Adapter for displaying measurement categories.
 */
public class MeasurementCategoryAdapter extends AbstractCategoryAdapter {

    /**
     * The context of the calling activity.
     **/
    private final Context context;

    public MeasurementCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    protected List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        for (MeasureCategory category : MeasureCategory.values()) {
            categories.add(new Category(category.text, category.icon));
        }
        return categories;
    }

    /**
     * Enum that represents all possible measurement categories.
     */
    public enum MeasureCategory {
        GENERAL(R.string.measure_category_general, R.drawable.ic_general),
        RELAXED(R.string.measure_category_relaxed, R.drawable.ic_relaxed),
        WORK(R.string.measure_category_work, R.drawable.ic_work),
        STRESSED(R.string.measure_category_stressed, R.drawable.ic_stressed),
        SPORT(R.string.measure_category_sport, R.drawable.ic_sport);

        /**
         * The id of the category name.
         **/
        private final int text;
        /**
         * The id of the category icon.
         **/
        private final int icon;

        MeasureCategory(int text, int icon) {
            this.text = text;
            this.icon = icon;
        }

        /**
         * Returns the name of a category.
         *
         * @param resources the resources to resolve the category name from it's id.
         * @return the name of a category.
         */
        public String getText(Resources resources) {
            return resources.getString(text);
        }

        /**
         * Returns the icon of a category.
         *
         * @param resources the resources to resolve the category icon from it's id.
         * @return the icon of a category.
         */
        public Drawable getIcon(Resources resources) {
            return ResourcesCompat.getDrawable(resources, icon, null);
        }
    }
}
