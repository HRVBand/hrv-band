package hrv.band.app.ui.view.adapter;


import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import hrv.band.app.R;

/**
 * Copyright (c) 2017
 * Created by Thomas Czogalik on 19.01.2017
 * <p>
 * Adapter for displaying feedback categories.
 */
public class FeedbackCategoryAdapter extends AbstractCategoryAdapter {

    /**
     * The context of the calling activity.
     **/
    private final Context context;

    public FeedbackCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        for (FeedbackCategory category : FeedbackCategory.values()) {
            categories.add(new Category(category.text, category.icon));
        }
        return categories;
    }

    /**
     * Enum that represents all possible feedback categories.
     */
    public enum FeedbackCategory {
        BUG(R.string.feedback_category_bug, R.drawable.ic_bug),
        IDEA(R.string.feedback_category_idea, R.drawable.ic_idea),
        QUESTION(R.string.feedback_category_question, R.drawable.ic_question);

        /**
         * The id of the category name.
         **/
        private final int text;
        /**
         * The id of the category icon.
         **/
        private final int icon;

        FeedbackCategory(int text, int icon) {
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
    }
}


