package hrv.band.aurora.storage;

import android.provider.BaseColumns;

/**
 * Created by Julian on 23.06.2016.
 */
public class HRVParameterContract {

    public HRVParameterContract() {}

    public static abstract class HRVParameterEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "HRVEntry";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_SD1 = "sd1";
        public static final String COLUMN_NAME_SD2 = "sd2";
        public static final String COLUMN_NAME_LF = "lf";
        public static final String COLUMN_NAME_HF = "hf";
        public static final String COLUMN_NAME_RMSSD = "rmssd";
        public static final String COLUMN_NAME_SDNN = "sdnn";
        public static final String COLUMN_NAME_BAEVSKY = "baevsky";
        public static final String COLUMN_NAME_RRDATAID = "rrid";
    }
}
