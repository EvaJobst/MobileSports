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
        String preferenceAgeDefault = context.getString(R.string.preference_age_default);

        String preferenceAgeValue = preferences.getString(preferenceAgeKey, preferenceAgeDefault);

        int age;
        try{
            age = Integer.parseInt(preferenceAgeValue);
        }
        catch (NumberFormatException ex){
            age = 0;
        }

        return age;
    }

    public int getRestingHeartRate(){
        String preferenceRestingHRKey = context.getString(R.string.preference_restinghr_key);
        String preferenceRestingHRDefault = context.getString(R.string.preference_restinghr_default);

        String preferenceRestingHRValue = preferences.getString(preferenceRestingHRKey, preferenceRestingHRDefault);

        int restingHeartRate;
        try{
            restingHeartRate = Integer.parseInt(preferenceRestingHRValue);
        }
        catch (NumberFormatException ex){
            restingHeartRate = 0;
        }

        return restingHeartRate;
    }
}
