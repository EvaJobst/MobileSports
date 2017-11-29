package at.fhooe.mos.mountaineer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import at.fhooe.mos.mountaineer.model.Gender;


/**
 * Created by Eva on 17.11.2017.
 */

public class PersistenceManager {
    private String CURRENT_STATE_KEY = "current_state";
    private Context context;
    private SharedPreferences preferences;

    public PersistenceManager(Context context) {
        this.context = context;
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

    public Gender getGender(){
        String preferenceGenderKey = context.getString(R.string.preference_gender_key);
        String preferenceGenderDefault = context.getString(R.string.preference_gender_default);

        String preferenceGenderValue = preferences.getString(preferenceGenderKey, preferenceGenderDefault);

        Gender gender = Gender.fromShortGenderString(preferenceGenderValue);
        return gender;
    }

    public int getAge(){
        String preferenceAgeKey = context.getString(R.string.preference_age_key);
        String preferenceAgeDefault = context.getString(R.string.preference_age_default);

        String preferenceAgeValue = preferences.getString(preferenceAgeKey, preferenceAgeDefault);

        int age = Integer.parseInt(preferenceAgeValue);

        return age;
    }
}
