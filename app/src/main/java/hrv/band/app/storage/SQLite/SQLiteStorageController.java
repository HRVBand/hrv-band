package hrv.band.app.storage.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julian on 23.06.2016.
 */
public class SQLiteStorageController extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HRVParamDB.db";

    private static final String SQL_CREATE_HRVTABLE = "CREATE TABLE "
            + HRVParameterContract.HRVParameterEntry.TABLE_NAME
            + " (" + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME + " INTEGER, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD1 + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD2 + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_LF + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RMSSD + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SDNN + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_BAEVSKY + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RATING + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY + " STRING, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE + " STRING, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RRDATAID + " INTEGER"
            + ")";

    private static final String SQL_CREATE_RRINTERVALTABLE = "CREATE TABLE "
            + RRIntervalContract.RRIntervalEntry.TABLE_NAME
            + "(" + RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_ID + " INTEGER, "
            + RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_VALUE + " REAL)";

    private static final String SQL_DELETE_RRINTERVALS =
            "DROP TABLE IF EXISTS " + RRIntervalContract.RRIntervalEntry.TABLE_NAME;

    private static final String SQL_DELETE_HRVPAARAMETERS =
            "DROP TABLE IF EXISTS " + HRVParameterContract.HRVParameterEntry.TABLE_NAME;

    private static SQLiteStorageController INSTANCE;

    private SQLiteStorageController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteStorageController getINSTANCE(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new SQLiteStorageController(context);
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_HRVTABLE);
        db.execSQL(SQL_CREATE_RRINTERVALTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newVersion)
    {
        db.execSQL(SQL_DELETE_HRVPAARAMETERS);
        db.execSQL(SQL_DELETE_RRINTERVALS);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
