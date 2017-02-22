package hrv.band.app.storage.sqlite.statements;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.control.Measurement;
import hrv.band.app.view.adapter.MeasurementCategoryAdapter;

/**
 * Object that helps assembling the SQLite select statement for Measurement
 * <p>
 * Created by Julian on 04.02.2017.
 */

public class HRVParamSQLiteObjectAdapter extends SQLiteObject<Measurement> {

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
    public List<Measurement> select(String whereClause, String[] whereClauseParams) {

        ArrayList<Measurement> returnList = new ArrayList<>();

        Cursor c = getCursor(whereClause, whereClauseParams);

        if(c != null && c.moveToFirst()) {
            //Load new HRV-Params
            while (!c.isAfterLast()) {

                int columnIndex = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_ENTRY_ID);

                Measurement newParam = loadHRVParam(c, columnIndex);
                returnList.add(newParam);

                c.moveToNext();
            }

            c.close();
        }

        return returnList;
    }

    private double[] loadRRIntervals(Cursor c, int columnIndex) {
        int rrid = c.getInt(columnIndex);
        RRIntervalObjectAdapter rrSelectObj = new RRIntervalObjectAdapter(db);
        String rrWhereClause = RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] rrWhereParams = new String[]{Integer.toString(rrid)};

        final List<double[]> select = rrSelectObj.select(rrWhereClause, rrWhereParams);
        if(select != null && !select.isEmpty()) {
            return select.get(0);
        }
        return new double[0];
    }

    private Measurement loadHRVParam(Cursor c, int columnIndex) {

        long time = c.getLong(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME));
        Date timeAsDate = new Date(time);

        Measurement.MeasurementBuilder measurementBuilder = new Measurement
                .MeasurementBuilder(timeAsDate, loadRRIntervals(c, columnIndex))
                .rating(loadRating(c))
                .category(loadCategory(c))
                .note(loadNote(c));

        return measurementBuilder.build();
    }
    private float loadRating(Cursor c) {
        return c.getFloat(c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_RATING));
    }

    private MeasurementCategoryAdapter.MeasureCategory loadCategory(Cursor c) {
        int columnIndexCategory = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_CATEGORY);
        if (!c.isNull(columnIndexCategory)) {
            String category = c.getString(columnIndexCategory);
            return MeasurementCategoryAdapter.MeasureCategory.valueOf(category.toUpperCase());
        }
        return MeasurementCategoryAdapter.MeasureCategory.GENERAL;
    }

    private String loadNote(Cursor c) {
        int columnIndexNote = c.getColumnIndex(HRVParameterContract.HRVParameterEntry.COLUMN_NAME_NOTE);
        if (!c.isNull(columnIndexNote)) {
            return c.getString(columnIndexNote);
        }
        return "";
    }
}
