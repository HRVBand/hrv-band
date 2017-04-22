package hrv.band.app.model.storage.sqlite.statements;

import android.provider.BaseColumns;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 23.06.2016.
 */
public class HRVParameterContract {

    /**
     * Private constructor to prevent object creation
     */
    private HRVParameterContract() { }

    public abstract static class HRVParameterEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "HRVEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_RRDATAID = "rrid";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_NOTE = "note";

        /**
         * "private constructor hides the implicit public one"
         */
        private HRVParameterEntry() { }
    }
}
