package at.fhooe.mos.mountaineer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Eva on 17.11.2017.
 */

public class PersistenceManager {
    private String CURRENT_STATE_KEY = "current_state";
    private SharedPreferences preferences;

    public PersistenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setCurrentState(TourState currentState) {
        preferences.edit().putString(CURRENT_STATE_KEY, currentState.name()).apply();
    }

    public TourState getCurrentState() {
        String state = preferences.getString(CURRENT_STATE_KEY, "");

        if (state.isEmpty()) {
            return TourState.newTour;
        }

        return TourState.valueOf(state);
    }
}
