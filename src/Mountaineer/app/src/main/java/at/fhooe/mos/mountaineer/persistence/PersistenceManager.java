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

    public int getHeight(){
        String preferenceHeightKey = context.getString(R.string.preference_height_key);
        String preferenceHeightDefault = context.getString(R.string.preference_height_default);

        String preferenceHeightValue = preferences.getString(preferenceHeightKey, preferenceHeightDefault);

        int height;
        try{
            height = Integer.parseInt(preferenceHeightValue);
        }
        catch (NumberFormatException ex){
            height = 0;
        }

        return height;
    }

    public int getBodyMass(){
        String preferenceMassKey = context.getString(R.string.preference_mass_key);
        String preferenceMassDefault = context.getString(R.string.preference_mass_default);

        String preferenceMassValue = preferences.getString(preferenceMassKey, preferenceMassDefault);

        int mass;
        try{
            mass = Integer.parseInt(preferenceMassValue);
        }
        catch (NumberFormatException ex){
            mass = 0;
        }

        return mass;
    }

    public int getPar(){
        String preferenceParKey = context.getString(R.string.preference_par_key);
        String preferenceParDefault = context.getString(R.string.preference_par_default);

        String preferenceParValue = preferences.getString(preferenceParKey, preferenceParDefault);

        int par;
        try{
            par = Integer.parseInt(preferenceParValue);
        }
        catch (NumberFormatException ex){
            par = 0;
        }

        return par;
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
