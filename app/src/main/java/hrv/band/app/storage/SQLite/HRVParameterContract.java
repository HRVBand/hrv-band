package hrv.band.app.storage.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Julian on 23.06.2016.
 */
class HRVParameterContract {

    /**
     * Private constructor to prevent object creation
     */
    private HRVParameterContract() {}

    public static abstract class HRVParameterEntry implements BaseColumns
    {
        public final static String TABLE_NAME = "HRVEntry";
        public final static String COLUMN_NAME_ENTRY_ID = "id";
        public final static String COLUMN_NAME_TIME = "time";
        public final static String COLUMN_NAME_SD1 = "sd1";
        public final static String COLUMN_NAME_SD2 = "sd2";
        public final static String COLUMN_NAME_LF = "lf";
        public final static String COLUMN_NAME_HF = "hf";
        public final static String COLUMN_NAME_RMSSD = "rmssd";
        public final static String COLUMN_NAME_SDNN = "sdnn";
        public final static String COLUMN_NAME_BAEVSKY = "baevsky";
        public final static String COLUMN_NAME_RRDATAID = "rrid";
        public final static String COLUMN_NAME_RATING = "rating";
        public final static String COLUMN_NAME_CATEGORY = "category";
        public final static String COLUMN_NAME_NOTE = "note";
    }
}
