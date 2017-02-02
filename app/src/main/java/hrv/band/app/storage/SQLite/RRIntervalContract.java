package hrv.band.app.storage.sqlite;

import android.provider.BaseColumns;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 23.06.2016.
 */
class RRIntervalContract {

    /**
     * Private constructor to prevent object creation
     */
    private RRIntervalContract() {}

    abstract static class RRIntervalEntry implements BaseColumns
    {
        static final String TABLE_NAME = "RRIinterval";
        static final String COLUMN_NAME_ENTRY_ID = "id";
        static final String COLUMN_NAME_ENTRY_VALUE = "rrvalue";

        /**
         * Private constructor to prevent object creation
         */
        private RRIntervalEntry() {}
    }
}
