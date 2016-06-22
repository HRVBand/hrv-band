package inno.hacks.ms.band.storage;

import android.content.Context;

import java.util.Date;
import java.util.List;

import inno.hacks.ms.band.Control.HRVParameters;

/**
 * Created by Thomas on 18.06.2016.
 */
public interface IStorage {
    void saveData(Context context, List<HRVParameters> parameters);

    void saveData(Context context, HRVParameters parameter);

    List<HRVParameters> loadData(Context context, Date date);
}
