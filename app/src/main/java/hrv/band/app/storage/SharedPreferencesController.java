package hrv.band.app.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrv.band.app.Control.HRVParameters;

/**
 * Created by Julian on 11.06.2016.
 */
public class SharedPreferencesController implements IStorage{

    @Override
    public void saveData(Context con, List<HRVParameters> params) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(params);
        prefsEditor.putString("ParamList", json);
        prefsEditor.commit();
    }

    public List<HRVParameters> loadAllData(Context context)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        String json = mPrefs.getString("ParamList", "");
        Type type = new TypeToken<List<HRVParameters>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public List<HRVParameters> loadData(Context con, Date date) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(con);

        Gson gson = new Gson();
        String json = mPrefs.getString("ParamList", "");
        Type type = new TypeToken<List<HRVParameters>>() {}.getType();

        return gson.fromJson(json, type);
    }

    @Override
    public boolean deleteData(Context context, HRVParameters parameter) {

        List<HRVParameters> allData = loadAllData(context);
        deleteAllData(context);

        if(allData.contains(parameter)) {
            allData.remove(parameter);
        }

        saveData(context, allData);

        return false;
    }

    @Override
    public boolean deleteData(Context context, List<HRVParameters> parameters) {
        return false;
    }

    private void deleteAllData(Context context)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.remove("ParamList");
    }

    @Override
    public void saveData(Context con, HRVParameters params) {
        List<HRVParameters> allData = loadData(con, null);
        if (allData == null) {
            allData = new ArrayList<>();
        }
        allData.add(params);
        saveData(con, allData);
    }
}