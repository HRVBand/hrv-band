package hrv.band.aurora.storage.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;
import hrv.band.aurora.storage.IStorage;

/**
 * Created by Julian on 23.06.2016.
 */
public class SQLController implements IStorage {

    @Override
    public void saveData(Context context, List<HRVParameters> parameters) {

        for (HRVParameters param: parameters)
        {
            saveData(context, param);
        }
    }

    @Override
    public void saveData(Context context, HRVParameters parameter) {


        SQLiteStorageController controller = new SQLiteStorageController(context);

        SQLiteDatabase db = controller.getWritableDatabase();

        ContentValues valuesParams = new ContentValues();
        long time = parameter.getTime().getTime();

        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME, time);
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

        db.close();


        //Get the highest id
        SQLiteDatabase dbread = controller.getReadableDatabase();
        Cursor c = dbread.rawQuery("SELECT MAX(id) FROM " + HRVParameterContract.HRVParameterEntry.TABLE_NAME, null);
        c.moveToFirst();
        int firstId =  c.getInt(0);
        c.close();
        dbread.close();


        SQLiteDatabase db2 = controller.getWritableDatabase();
        for(Double rrVal : parameter.getRRIntervals())
        {
            ContentValues valuesRR = new ContentValues();
            valuesRR.put(RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_ID, firstId);
            valuesRR.put(RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_VALUE, rrVal);

            db2.insert(RRIntervalContract.RRIntercalEntry.TABLE_NAME,
                    RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_VALUE,
                    valuesRR);
        }
    }

    @Override
    public List<HRVParameters> loadData(Context context, Date date) {

        SQLiteStorageController controller = new SQLiteStorageController(context);


        SQLiteDatabase db = controller.getReadableDatabase();

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

        String timeStr = Long.toString(date.getTime());

        Cursor c = db.query(
                HRVParameterContract.HRVParameterEntry.TABLE_NAME,
                projection,  //All Columns
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME + " = ?",
                new String[] { timeStr },
                null,
                null,
                null,
                null
                );

        HRVParameters newParam = new HRVParameters();

        c.moveToFirst();

        long time =  c.getLong(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME));
        Date timeAsDate = new Date(time);

        newParam.setTime(timeAsDate);
        newParam.setSd1(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD1)));
        newParam.setSd2(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SD2)));
        newParam.setLf(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_LF)));
        newParam.setHf(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_HF)));
        newParam.setRmssd(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RMSSD)));
        newParam.setSdnn(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_SDNN)));
        newParam.setBaevsky(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_BAEVSKY)));
        int rrid = c.getInt(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RRDATAID));

        //Laden der rr daten

        Cursor crr = db.query(
                RRIntervalContract.RRIntercalEntry.TABLE_NAME,
                null,  //All Columns
                RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_ID + " = ?" ,
                new String[] { Integer.toString(rrid) },
                null,
                null,
                null,
                null
            );

        String query1 = "select * from " + RRIntervalContract.RRIntercalEntry.TABLE_NAME;
        String query2 = "select * from " + RRIntervalContract.RRIntercalEntry.TABLE_NAME
                + " WHERE " + RRIntervalContract.RRIntercalEntry.COLUMN_NAME_ENTRY_ID + " = 5";

        crr = db.rawQuery(query1, null);

        ArrayList<Double> rrValues = new ArrayList<Double>();
        crr.moveToFirst();
        if(!crr.isAfterLast())
        {
            do {
                int id = c.getInt(0);
                rrValues.add(c.getDouble(1));
            } while(crr.moveToNext());
        }

        newParam.setRRIntervals(rrValues);
        List<HRVParameters> returnList = new ArrayList<HRVParameters>();
        returnList.add(newParam);

        return returnList;
    }
}


