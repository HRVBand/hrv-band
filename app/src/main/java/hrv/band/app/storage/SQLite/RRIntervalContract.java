package hrv.band.app.storage.SQLite;

import android.provider.BaseColumns;

/**
 * Created by Julian on 23.06.2016.
 */
class RRIntervalContract {

    /**
     * Private constructor to prevent object creation
     */
    private RRIntervalContract() {}

    public static abstract class RRIntervalEntry implements BaseColumns
    {
        public final static String TABLE_NAME = "RRIinterval";
        public final static String COLUMN_NAME_ENTRY_ID = "id";
        public final static String COLUMN_NAME_ENTRY_VALUE = "rrvalue";
    }
}
