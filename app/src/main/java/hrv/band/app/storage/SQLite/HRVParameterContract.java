package hrv.band.app.storage.sqlite;

import android.provider.BaseColumns;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 23.06.2016.
 */
class HRVParameterContract {

    /**
     * Private constructor to prevent object creation
     */
    private HRVParameterContract() { }

    abstract static class HRVParameterEntry implements BaseColumns
    {
        static final String TABLE_NAME = "HRVEntry";
        static final String COLUMN_NAME_ENTRY_ID = "id";
        static final String COLUMN_NAME_TIME = "time";
        static final String COLUMN_NAME_SD1 = "sd1";
        static final String COLUMN_NAME_SD2 = "sd2";
        static final String COLUMN_NAME_LF = "lf";
        static final String COLUMN_NAME_HF = "hf";
        static final String COLUMN_NAME_RMSSD = "rmssd";
        static final String COLUMN_NAME_SDNN = "sdnn";
        static final String COLUMN_NAME_BAEVSKY = "baevsky";
        static final String COLUMN_NAME_RRDATAID = "rrid";
        static final String COLUMN_NAME_RATING = "rating";
        static final String COLUMN_NAME_CATEGORY = "category";
        static final String COLUMN_NAME_NOTE = "note";

        /**
         * "private construcotr hodes the implicit public one"
         */
        private HRVParameterEntry() { }
    }
}
