package hrv.band.app.ui.view.adapter;

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

import java.util.List;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Abstract adapter for displaying categories.
 */
public abstract class AbstractCategoryAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return getCategories().size();
    }

    @Override
    public Object getItem(int i) {
        return getCategories().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_category_item, viewGroup, false);
            holder = new ViewHolder();
            holder.categoryTxt = (TextView) convertView.findViewById(R.id.category_name);
            holder.categoryIcon = (ImageView) convertView.findViewById(R.id.category_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.categoryTxt.setText(getCategories().get(i).getName(getContext().getResources()));
        holder.categoryIcon.setImageDrawable(getCategories().get(i).getIcon(getContext().getResources()));
        return convertView;
    }

    /**
     * Returns the context of the ui.view.activity using this adapter.
     *
     * @return the context of the ui.view.activity using this adapter.
     */
    protected abstract Context getContext();

    /**
     * Returns a list containing a tuple of name and icon for each category.
     *
     * @return a list containing a tuple of name and icon for each category.
     */
    protected abstract List<Category> getCategories();

    /**
     * The ViewHolder of this adapter.
     */
    private static class ViewHolder {
        private TextView categoryTxt;
        private ImageView categoryIcon;
    }

    /**
     * Represents a Category containing a name and an icon.
     */
    class Category {
        /**
         * The id of the category name.
         **/
        private final int name;
        /**
         * The id of the category icon.
         **/
        private final int icon;

        Category(int text, int icon) {
            this.name = text;
            this.icon = icon;
        }

        /**
         * Returns the name of a category.
         *
         * @param resources the resources to resolve the category name from it's id.
         * @return the name of a category.
         */
        String getName(Resources resources) {
            return resources.getString(name);
        }

        /**
         * Returns the icon of a category.
         *
         * @param resources the resources to resolve the category icon from it's id.
         * @return the icon of a category.
         */
        Drawable getIcon(Resources resources) {
            return ResourcesCompat.getDrawable(resources, icon, null);
        }
    }
}
