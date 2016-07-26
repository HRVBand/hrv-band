package hrv.band.aurora.storage;

import android.content.Context;

import java.util.Date;
import java.util.List;

import hrv.band.aurora.Control.HRVParameters;

/**
 * Created by Thomas on 18.06.2016.
 */
public interface IStorage {
    void saveData(Context context, List<HRVParameters> parameters);

    void saveData(Context context, HRVParameters parameter);

    List<HRVParameters> loadData(Context context, Date date);

    boolean deleteData(Context context, HRVParameters parameter);

    boolean deleteData(Context context, List<HRVParameters> parameters);
}
