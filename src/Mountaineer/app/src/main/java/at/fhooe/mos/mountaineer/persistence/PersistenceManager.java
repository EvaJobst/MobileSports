package at.fhooe.mos.mountaineer.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.TourState;
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

    public String getUserId(){
        return "user1";
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
        int preferenceAgeDefault = Integer.valueOf(context.getString(R.string.preference_age_default));

        int preferenceAgeValue = preferences.getInt(preferenceAgeKey, preferenceAgeDefault);

        return preferenceAgeValue;
    }

    public int getHeight(){
        String preferenceHeightKey = context.getString(R.string.preference_height_key);
        int preferenceHeightDefault = Integer.valueOf(context.getString(R.string.preference_height_default));

        int preferenceHeightValue = preferences.getInt(preferenceHeightKey, preferenceHeightDefault);

        return preferenceHeightValue;
    }

    public int getBodyMass(){
        String preferenceMassKey = context.getString(R.string.preference_mass_key);
        int preferenceMassDefault = Integer.valueOf(context.getString(R.string.preference_mass_default));

        int preferenceMassValue = preferences.getInt(preferenceMassKey, preferenceMassDefault);

        return preferenceMassValue;
    }

    public int getPar(){
        String preferenceParKey = context.getString(R.string.preference_par_key);
        int preferenceParDefault = Integer.valueOf(context.getString(R.string.preference_par_default));

        int preferenceParValue = preferences.getInt(preferenceParKey, preferenceParDefault);

        return preferenceParValue;
    }

    public int getRestingHeartRate(){
        String preferenceRestingHRKey = context.getString(R.string.preference_restinghr_key);
        int preferenceRestingHRDefault = Integer.valueOf(context.getString(R.string.preference_restinghr_default));

        int preferenceRestingHRValue = preferences.getInt(preferenceRestingHRKey, preferenceRestingHRDefault);

        return preferenceRestingHRValue;
    }

    public boolean getSimulateSensorData(){
        String preferenceSimulateKey = context.getString(R.string.preference_simulate_key);
        String preferenceSimulateDefault = context.getString(R.string.preference_simulate_default);

        boolean defaultValue = Boolean.parseBoolean(preferenceSimulateDefault);

        boolean preferenceSimulateValue = preferences.getBoolean(preferenceSimulateKey, defaultValue);

        return preferenceSimulateValue;
    }

    public static PersistenceManager Get(Context context){
        return new PersistenceManager(context);
    }
}
