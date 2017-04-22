package hrv.band.app.model.storage.sqlite.statements;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite Adapter for the RR-Intervall Array
 *
 * Created by Julian on 05.02.2017.
 */
class RRIntervalObjectAdapter extends SQLiteObject<double[]> {


    RRIntervalObjectAdapter(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public String getTableName() {
        return RRIntervalContract.RRIntervalEntry.TABLE_NAME;
    }

    @Override
    public String[] getProjection() {
        return null;
    }

    @Override
    public List<double[]> select(String whereClause, String[] whereClauseParams) {
        Cursor crr = getCursor(whereClause, whereClauseParams);
        ArrayList<double[]> values = new ArrayList<>();

        double[] rrValues = new double[crr.getCount()];
        int rrIndex = 0;
        crr.moveToFirst();
        if (!crr.isAfterLast()) {
            do {
                int columnIndex = crr.getColumnIndex(RRIntervalContract.RRIntervalEntry.COLUMN_NAME_ENTRY_VALUE);
                double loadedValue = crr.getDouble(columnIndex);
                rrValues[rrIndex] = loadedValue;
                rrIndex++;
            } while (crr.moveToNext());
        }

        values.add(rrValues);
        return values;
    }

    @Override
    protected Cursor getCursor(String whereClause, String[] whereClauseParams) {
        return db.query(
                getTableName(),
                getProjection(),
                whereClause,
                whereClauseParams,
                null,
                null,
                null,
                null
        );
    }
}
