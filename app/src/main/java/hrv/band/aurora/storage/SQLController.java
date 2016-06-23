package hrv.band.aurora.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;

/**
 * Created by Julian on 23.06.2016.
 */
public class SQLController extends SQLiteOpenHelper implements IStorage {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HRVParamDB.db";

    private static final String SQL_CREATE_HRVTABLE = "CREATE TABLE "
            + HRVParameterContract.HRVParameterEntry.TABLE_NAME
            + " (" + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ASC, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME + " INTEGER"
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD1 + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD2 + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_LF + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RMSSD + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SDNN + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_BAEVSKY + " REAL, "
            + HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RRDATAID + "INTEGER"
            + ")";

    private static final String SQL_CREATE_RRINTERVALTABLE = "CREATE TABLE"
            + RRIntervalContract.RRIntercalEntry.TABLE_NAME
            + "(" + RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_ID + " INTEGER, "
            + RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_VALUE + " REAL, ";

    private static final String SQL_DELETE_RRINTERVALS =
            "DROP TABLE IF EXISTS " + RRIntervalContract.RRIntercalEntry.TABLE_NAME;

    private static final String SQL_DELETE_HRVPAARAMETERS =
            "DROP TABLE IF EXISTS " + HRVParameterContract.HRVParameterEntry.TABLE_NAME;

    public SQLController(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void saveData(Context context, List<HRVParameters> parameters) {
        for (HRVParameters param: parameters)
        {
            saveData(context, param);
        }
    }

    @Override
    public void saveData(Context context, HRVParameters parameter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valuesParams = new ContentValues();
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME, parameter.getTime().getTime());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD1, parameter.getSd1());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD2, parameter.getSd2());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_LF, parameter.getLf());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF, parameter.getHf());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RMSSD, parameter.getRmssd());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SDNN, parameter.getSdnn());
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_BAEVSKY, parameter.getBaevsky());

        db.insert(HRVParameterContract.HRVParameterEntry.TABLE_NAME,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF,
                valuesParams);

        //Get the highest id
        SQLiteDatabase dbread = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
        c.moveToFirst();
        int firstId =  c.getInt(0);
        c.close();

        for(double rrVal : parameter.getRRIntervals())
        {
            ContentValues valuesRR = new ContentValues();
            valuesParams.put(RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_ID, firstId);
            valuesParams.put(RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_VALUE, rrVal);

            db.insert(RRIntervalContract.RRIntercalEntry.TABLE_NAME,
                    RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_VALUE,
                    valuesParams);
        }
    }

    @Override
    public List<HRVParameters> loadData(Context context, Date date) {

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD1,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD2,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_LF,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RMSSD,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SDNN,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_BAEVSKY,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RRDATAID,
        };

        //String sortOrder =

        return null;
    }
}


