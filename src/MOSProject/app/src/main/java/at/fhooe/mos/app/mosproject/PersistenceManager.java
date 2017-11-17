package at.fhooe.mos.app.mosproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mos.app.mosproject.pedometer.SensorEventData;


/**
 * Created by Eva on 17.11.2017.
 */

public class PersistenceManager {
    private String ACCELEROMETER_KEY = "data2";
    private String TRAINING_IDS_KEY = "training_ids";
    private SharedPreferences preferences;

    public PersistenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAccelerometer(ArrayList<SensorEventData> data) {
        preferences.edit().putString(ACCELEROMETER_KEY, new Gson().toJson(data)).apply();
    }

    public ArrayList<SensorEventData> getAccelerometer() {
        Type type = new TypeToken<ArrayList<SensorEventData>>(){}.getType();
        String json = preferences.getString(ACCELEROMETER_KEY, "[]");
        ArrayList<SensorEventData> data = new Gson().fromJson(json, type);

        return data;
    }

    public ArrayList<String> getTrainingIds() {
        String json = preferences.getString(TRAINING_IDS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public void setTrainingIds(ArrayList<String> ids) {
        preferences.edit().putString(TRAINING_IDS_KEY, new Gson().toJson(ids)).apply();
    }

    public void addTrainingId(String id) {
        ArrayList<String> ids = getTrainingIds();
        ids.add(id);
        setTrainingIds(ids);
    }
}
