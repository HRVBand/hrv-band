package hrv.band.app.storage.sqlite.statements;

import android.provider.BaseColumns;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 23.06.2016.
 */
public class RRIntervalContract {

    /**
     * Private constructor to prevent object creation
     */
    private RRIntervalContract() {}

    public abstract static class RRIntervalEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "RRIinterval";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_ENTRY_VALUE = "rrvalue";

        /**
         * Private constructor to prevent object creation
         */
        private RRIntervalEntry() {}
    }
}
