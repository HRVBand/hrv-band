package hrv.band.app.ui.presenter;

import android.graphics.drawable.Drawable;

/**
 * * Copyright (c) 2017
 * Created by Thomas on 17.04.2017.
 */
public interface IMeasurementDetailsPresenter {
    String getDate();

    String getRating();

    String getNote();

    String getCategory();

    Drawable getCategoryIcon();
}
