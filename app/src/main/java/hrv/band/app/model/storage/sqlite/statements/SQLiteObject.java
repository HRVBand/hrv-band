package hrv.band.app.model.storage.sqlite.statements;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * SQLite select needs a projection of the needed data.
 * This interface provides this projection.
 * The result of a select statement is a cursor object with data.
 * This data must be handled and set to a new object.
 *
 * Created by Julian on 04.02.2017.
 */
abstract class SQLiteObject<T> {

    SQLiteDatabase db;

    SQLiteObject(SQLiteDatabase db) {
        this.db = db;
    }

    abstract String getTableName();
    abstract String[] getProjection();
    protected Cursor getCursor(String whereClause, String[] whereClauseParams) {
        return db.query(getTableName(),
                        getProjection(),
                        whereClause,
                        whereClauseParams,
                        null,
                        null,
                        HRVParameterContract.HRVParameterEntry.COLUMN_NAME_TIME,
                        null);
    }

    public abstract List<T> select(String whereClause, String[] whereClauseParams);
}
