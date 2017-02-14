package hrv.band.app.storage.sqlite.statements;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.control.HRVParameters;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Object that helps assembling the SQLite select statement for HRVParameters
 * <p>
 * Created by Julian on 04.02.2017.
 */

public class HRVParamSQLiteObjectAdapter extends SQLiteObject<HRVParameters> {

    public HRVParamSQLiteObjectAdapter(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getTableName() {
        return HRVParameterContract.HRVParameterEntry.TABLE_NAME;
    }

    @Override
    public String[] getProjection() {
        return new String[]{
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RATING,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY,
                HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE
        };
    }

    @Override
    public List<HRVParameters> select(String whereClause, String[] whereClauseParams) {

        ArrayList<HRVParameters> returnList = new ArrayList<>();

        Cursor c = getCursor(whereClause, whereClauseParams);

        if(c != null && c.moveToFirst()) {
            //Load new HRV-Params
            while (!c.isAfterLast()) {

                int columnIndex = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID);
                int rrid = c.getInt(columnIndex);
                HRVParameters newParam = loadHRVParam(c);

                //Load rr data.
                RRIntervalObjectAdapter rrSelectObj = new RRIntervalObjectAdapter(db);
                String rrWhereClause = RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_ID + " = ?";
                String[] rrWhereParams = new String[]{Integer.toString(rrid)};

                final List<double[]> select = rrSelectObj.select(rrWhereClause, rrWhereParams);
                if(select != null && !select.isEmpty()) {
                    double[] rr = select.get(0);
                    newParam.setRRIntervals(rr);
                }

                returnList.add(newParam);

                c.moveToNext();
            }

            c.close();
        }

        return returnList;
    }

    private HRVParameters loadHRVParam(Cursor c) {

        HRVParameters newParam = new HRVParameters();
        long time = c.getLong(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME));
        Date timeAsDate = new Date(time);

        newParam.setTime(timeAsDate);
        newParam.setRating(c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RATING)));

        //Read nullable category
        int columnIndexCategory = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY);
        if (!c.isNull(columnIndexCategory)) {
            String category = c.getString(columnIndexCategory);
            newParam.setCategory(MeasurementCategoryAdapter.MeasureCategory.valueOf(category.toUpperCase()));
        }

        //Check whether stored note is null
        int columnIndexNote = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE);
        if (!c.isNull(columnIndexNote)) {
            newParam.setNote(c.getString(columnIndexNote));
        }

        return newParam;
    }
}
