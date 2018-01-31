package at.fhooe.mos.app.mosproject.ui;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.heartrate.BLEService;
import at.fhooe.mos.app.mosproject.heartrate.DeviceFactory;
import at.fhooe.mos.app.mosproject.heartrate.DeviceHRM;
import at.fhooe.mos.app.mosproject.heartrate.EnergyExpenditureCalculator;
import at.fhooe.mos.app.mosproject.heartrate.HRCalculation;
import at.fhooe.mos.app.mosproject.heartrate.HRM;
import at.fhooe.mos.app.mosproject.heartrate.SensorFactory;
import at.fhooe.mos.app.mosproject.model.user.UserInformation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeartRateDeviceActivity extends AppCompatActivity {
    Boolean isRunning = false;
    HRCalculation hrCalculation = new HRCalculation();
    Handler handler = new Handler();
    ArrayList<Integer> heartRateValues = new ArrayList<>();
    HRM heartRateMonitor;
    String inputHrMaxPercent;
    //String age;
    double calories = 0;
    UserInformation userInformation;

    @BindView(R.id.currentHR)
    TextView currentHR;

    @BindView(R.id.averageHR)
    TextView averageHR;

    @BindView(R.id.minHR)
    TextView minHR;

    @BindView(R.id.maxHR)
    TextView maxHR;

    @BindView(R.id.maxHRPercent)
    TextView maxHRPercent;

    @BindView(R.id.kCal)
    TextView kCal;

    @BindView(R.id.startStopDevice)
    Button startStopDevice;

    @OnClick(R.id.startStopDevice)
    public void onStartStopScanDeviceClick() {
        isRunning = !isRunning;

        if(isRunning) {
            startStopDevice.setText("Stop");
            fetchHR();
        }

        else {
            startStopDevice.setText("Start");
            resetUI();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_device);
        ButterKnife.bind(this);

        userInformation = new Gson().fromJson(getIntent().getExtras().get("user").toString(), UserInformation.class);
        //age = String.valueOf(userInformation.getAge());
        inputHrMaxPercent = getIntent().getExtras().get("inputHrMaxPercent").toString();

        SensorFactory sensorFactory;
        sensorFactory = new DeviceFactory();

        heartRateMonitor = sensorFactory.createHRM();
        heartRateMonitor.initialize(this);
    }

    @Override
    protected void onDestroy() {
        if(heartRateMonitor instanceof DeviceHRM) {
            ((DeviceHRM)heartRateMonitor).stopService();
        }

        super.onDestroy();
    }

    public void resetUI() {
        heartRateValues = new ArrayList<>();
        currentHR.setText("0");
        minHR.setText("0");
        maxHR.setText("0");
        averageHR.setText("0");
        maxHRPercent.setText("0");
    }

    public void updateUI() {
        currentHR.setText(String.valueOf(heartRateValues.get(heartRateValues.size()-1)));
        minHR.setText(String.valueOf(Collections.min(heartRateValues)));
        maxHR.setText(String.valueOf(Collections.max(heartRateValues)));
        averageHR.setText(String.valueOf(hrCalculation.getAverageHR(heartRateValues)));

        double heartRate = heartRateValues.get(heartRateValues.size()-1);
        double newCalories = EnergyExpenditureCalculator.calculateEnergyExpenditureEstimation(userInformation, heartRate) / 60;
        calories = calories + newCalories;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        kCal.setText(decimalFormat.format(calories));

        maxHRPercent.setText(String.valueOf(hrCalculation.getPercentHRmax(
                inputHrMaxPercent,
                String.valueOf(userInformation.getAge()),
                maxHR.getText().toString()
        )));
    }

    public void fetchHR() {
        if (isRunning) {
            int heartRate = heartRateMonitor.getHeartRate();
            heartRateValues.add(heartRate);
            updateUI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchHR();
                }
            }, 1000);
        }
    }
}
