package inno.hacks.ms.band.Control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Julian on 11.06.2016.
 */
public class SharedPreferencesController {

    public void SaveData(Context con, List<HRVParameters> params)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(params);
        prefsEditor.putString("ParamList", json);
        prefsEditor.commit();
    }

    public List<HRVParameters> LoadData(Context con)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(con);

        Gson gson = new Gson();
        String json = mPrefs.getString("ParamList", "");
        Type type = new TypeToken<List<HRVParameters>>(){}.getType();
        List<HRVParameters> obj = gson.fromJson(json, type);

        return obj;
    }

    public void AddParams(Context con, HRVParameters params)
    {
        List<HRVParameters> allData = LoadData(con);
        allData.add(params);
        SaveData(con, allData);
    }
}
