package hrv.band.app.model.storage.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hrv.band.app.model.Measurement;
import hrv.band.app.model.storage.FileUtils;
import hrv.band.app.model.storage.IStorage;
import hrv.band.app.model.storage.sqlite.statements.HRVParamSQLiteObjectAdapter;
import hrv.band.app.model.storage.sqlite.statements.HRVParameterContract;
import hrv.band.app.model.storage.sqlite.statements.RRIntervalContract;

/**
 * Copyright (c) 2017
 * Created by Julian Martin on 23.06.2016.
 * <p>
 * Responsible for saving and loading user data.
 */
public class HRVSQLController implements IStorage {

    private static Date getEndOfDay(Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

    private static Date getStartOfDay(Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    @Override
    public void saveData(Context context, List<Measurement> parameters) {

        for (Measurement param : parameters) {
            saveData(context, param);
        }
    }

    @Override
    public void saveData(Context context, Measurement parameter) {
        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(context);

        //Create new Entry in DB, this entry stores meta data to a given RR-Interval
        ContentValues valuesParams = new ContentValues();
        long time = parameter.getTime().getTime();

        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME, time);
        valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RATING, parameter.getRating());

        if (parameter.getCategory() != null) {
            valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY, parameter.getCategory().toString());
        } else {
            valuesParams.putNull(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY);
        }

        if (parameter.getNote() != null) {
            valuesParams.put(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE, parameter.getNote());
        } else {
            valuesParams.putNull(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE);
        }

        //Insert new entry and get the Id of the new entry
        SQLiteDatabase db = controller.getWritableDatabase();
        long firstId = db.insert(HRVParameterContract.HRVParameterEntry.TABLE_NAME,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID,
                valuesParams);

        db.close();

        //Store the RR-Interval-Raw-Data
        SQLiteDatabase db2 = controller.getWritableDatabase();
        db2.beginTransaction();

        for (Double rrVal : parameter.getRRIntervals()) {
            ContentValues valuesRR = new ContentValues();
            valuesRR.put(RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_ID, firstId);
            valuesRR.put(RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_VALUE, rrVal);

            db2.insert(RRIntervalContract.RRIntervalEntry.TABLE_NAME,
                    RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_VALUE,
                    valuesRR);
        }
        db2.setTransactionSuccessful();
        db2.endTransaction();
        db2.close();
    }

    @Override
    public List<Measurement> loadData(Context context, Date date) {

        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(context);
        SQLiteDatabase db = controller.getReadableDatabase();

        HRVParamSQLiteObjectAdapter hrvParamSelect = new HRVParamSQLiteObjectAdapter(db);
        String whereClause = HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME + " BETWEEN ? AND ?";
        String timeOfDayEndStr = Long.toString(getEndOfDay(date).getTime());
        String timeOfDayStartStr = Long.toString(getStartOfDay(date).getTime());
        String[] whereClauseParams = new String[]{timeOfDayStartStr, timeOfDayEndStr};
        return hrvParamSelect.select(whereClause, whereClauseParams);
    }

    private List<Measurement> loadAllHRVParams(Context context) {
        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(context);
        SQLiteDatabase db = controller.getReadableDatabase();

        HRVParamSQLiteObjectAdapter hrvParamSelect = new HRVParamSQLiteObjectAdapter(db);
        return hrvParamSelect.select(null, null);
    }

    @Override
    public boolean deleteData(Context context, Measurement parameter) {
        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(context);

        SQLiteDatabase db = controller.getReadableDatabase();

        String timeStr = Long.toString(parameter.getTime().getTime());

        String whereClause = HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME + "=?";
        String[] whereArgs = new String[]{timeStr};

        return db.delete(HRVParameterContract.HRVParameterEntry.TABLE_NAME,
                whereClause, whereArgs
        ) > 0;
    }

    @Override
    public boolean deleteData(Context context, List<Measurement> parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            deleteData(context, parameters.get(i));
        }

        return true;
    }

    public boolean deleteAllData(Context context) {
        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(context);
        SQLiteDatabase db = controller.getReadableDatabase();
        return db.delete(HRVParameterContract.HRVParameterEntry.TABLE_NAME, null, null) >= 0;
    }

    /**
     * Tries to export the database to the users documents folder.
     * Returns whether the export was successfully or not
     *
     * @param con Context
     * @return True if export was successful, false otherwise
     * @throws IOException
     */
    public boolean exportDB(Context con) throws IOException {

        //Experimental zone
        if (!isExternalStorageWritable()) {
            return false;
        }
        //Activity has to check for write external storage permission
        // try to write the file and return error if not able to write.

        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/hrvband");
        documentsDir.mkdirs();

        List<Measurement> allHrvParams = loadAllHRVParams(con);

        try {
            for (int i = 0; i < allHrvParams.size(); i++) {
                if (!exportIBIFile(documentsDir, allHrvParams.get(i))) {
                    return false;
                }
            }

            return true;

        } catch (SecurityException e) {
            Log.e(e.getClass().getName(), "SecurityException", e);
        }

        return false;
    }


    /**
     * Exports the given param to the given directory
     *
     * @param documentsDir Directory to export to
     * @param param        param to export
     * @return Whether the export was successful
     * @throws IOException
     */
    private boolean exportIBIFile(File documentsDir, Measurement param) throws IOException {
        final String s = param.getTime().toString();
        File ibiFile = new File(documentsDir.getAbsolutePath(), "/RR" + s + ".ibi");
        ibiFile.deleteOnExit();

        if (ibiFile.exists() && !ibiFile.delete()
                || !ibiFile.createNewFile()) {
            return false;
        }

        FileOutputStream outStr = new FileOutputStream(ibiFile);
        PrintWriter out = new PrintWriter(outStr);

        double[] rrIntervals = param.getRRIntervals();
        for (double rrInterval : rrIntervals) {
            out.println(rrInterval);
        }

        out.close();
        outStr.close();
        return true;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean importDB(String dbPath, Context con) throws IOException {

        SQLiteStorageController controller = SQLiteStorageController.getINSTANCE(con);
        String dbToImportToPath = String.valueOf(con.getDatabasePath(controller.getDatabaseName()));

        File newDB = new File(dbPath);
        File oldDB = new File(dbToImportToPath);

        if (newDB.exists()) {
            FileUtils.copyFile(new FileInputStream(newDB), new FileOutputStream(oldDB));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            controller.getWritableDatabase().close();

            return true;
        }

        return false;
    }
}

