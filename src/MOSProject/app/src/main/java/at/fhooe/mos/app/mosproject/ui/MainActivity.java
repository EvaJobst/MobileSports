package at.fhooe.mos.app.mosproject.ui;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.model.user.Gender;
import at.fhooe.mos.app.mosproject.model.user.UserInformation;
import at.fhooe.mos.app.mosproject.pedometer.StepCalculation;
import at.fhooe.mos.app.mosproject.stopwatch.Stopwatch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@EActivity
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textInputAge)
    TextInputLayout textInputAge;

    @BindView(R.id.textInputHeight)
    TextInputLayout textInputHeight;

    @BindView(R.id.textInputWeight)
    TextInputLayout textInputWeight;

    @BindView(R.id.textInputHRMax)
    TextInputLayout textInputHRMax;

    @BindView(R.id.textInputPAR)
    TextInputLayout textInputPAR;

    @BindView(R.id.textInputRestingHR)
    TextInputLayout textInputRestingHR;

    @BindView(R.id.textInputStride)
    TextInputLayout textInputStride;

    @BindView(R.id.spinnerGender)
    Spinner spinnerGender;

    @OnClick(R.id.heartRateSimulationMode)
    public void onHeartRateSimulationModeClick() {
        Intent intent = new Intent(MainActivity.this, HeartRateSimulationActivity.class);
        startActivityWithIntent(intent);
    }

    @OnClick(R.id.heartRateLiveMode)
    public void onHeartRateLiveModeClick() {
        Intent intent = new Intent(MainActivity.this, ScanHeartRateDeviceActivity.class);
        startActivityWithIntent(intent);
    }

    @OnClick(R.id.pedometerLiveMode)
    public void onPedometerLiveModeClick() {
        Intent intent = new Intent(MainActivity.this, PedometerLiveActivity.class);
        startActivityWithIntent(intent);
    }

    @OnClick(R.id.pedometerSimulationMode)
    public void onPedometerSimulationModeClick() {
        Intent intent = new Intent(MainActivity.this, PedometerSimulationActivity.class);
        startActivityWithIntent(intent);
    }

    public void startActivityWithIntent(Intent intent) {
        UserInformation info = getUserInformation();
        if (info != null) {
            String hrMax = textInputHRMax.getEditText().getText().toString();
            setSharedPreferences(info, hrMax);
            intent.putExtra("user", new Gson().toJson(info));
            intent.putExtra("inputHrMaxPercent", hrMax);
            startActivity(intent);
        }
    }

    public void setSharedPreferences(UserInformation info, String hrMax) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("user", new Gson().toJson(info)).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("hrMax", hrMax).apply();
    }

    public UserInformation getUserFromSharedPreferences() {
        String user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", null);
        return new Gson().fromJson(user, UserInformation.class);
    }

    public String getHrMaxFromSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("hrMax", "");
    }

    public UserInformation getUserInformation() {
        if(spinnerGender.getSelectedItem().toString().isEmpty() ||
                textInputAge.getEditText().getText().toString().isEmpty() ||
                textInputHeight.getEditText().getText().toString().isEmpty() ||
                textInputWeight.getEditText().getText().toString().isEmpty() ||
                textInputPAR.getEditText().getText().toString().isEmpty() ||
                textInputRestingHR.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in the required information first", Toast.LENGTH_SHORT).show();
            return null;
        }

        else {
            Gender gender = Gender.fromLongGenderString(spinnerGender.getSelectedItem().toString());
            int age = Integer.parseInt(textInputAge.getEditText().getText().toString());
            int height = Integer.parseInt(textInputHeight.getEditText().getText().toString());
            int weight = Integer.parseInt(textInputWeight.getEditText().getText().toString());
            int par = Integer.parseInt(textInputPAR.getEditText().getText().toString());
            int restingHR = Integer.parseInt(textInputRestingHR.getEditText().getText().toString());

            int stride;

            if(textInputStride.getEditText().getText().toString().isEmpty()) {
                stride = StepCalculation.getStrideLengthInCm(gender, age, height, weight);
            }

            else {
                stride = Integer.parseInt(textInputStride.getEditText().getText().toString());
            }

            return new UserInformation(gender, age, height, weight, par, stride, restingHR);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        UserInformation info = getUserFromSharedPreferences();
        String hrMax = getHrMaxFromSharedPreferences();

        if(info != null && !hrMax.isEmpty()) {
            textInputHRMax.getEditText().setText(hrMax);
            textInputRestingHR.getEditText().setText(String.valueOf(info.getRestingHeartRate()));
            textInputAge.getEditText().setText(String.valueOf(info.getAge()));
            textInputHeight.getEditText().setText(String.valueOf(info.getHeight()));
            textInputWeight.getEditText().setText(String.valueOf(info.getBodyMass()));
            textInputPAR.getEditText().setText(String.valueOf(info.getPar()));
            textInputStride.getEditText().setText(String.valueOf(info.getStrideLength()));

            int selection = info.getGender().equals(Gender.Male) ? 0 : 1;
            spinnerGender.setSelection(selection);
        }
    }
}
