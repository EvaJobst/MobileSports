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

import java.util.ArrayList;
import java.util.Collections;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.heartrate.BLEService;
import at.fhooe.mos.app.mosproject.heartrate.DeviceFactory;
import at.fhooe.mos.app.mosproject.heartrate.DeviceHRM;
import at.fhooe.mos.app.mosproject.heartrate.HRCalculation;
import at.fhooe.mos.app.mosproject.heartrate.HRM;
import at.fhooe.mos.app.mosproject.heartrate.SensorFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeartRateDeviceActivity extends AppCompatActivity {
    Boolean isRunning = false;
    HRCalculation hrCalculation = new HRCalculation();
    Handler handler = new Handler();
    ArrayList<Integer> heartRateValues = new ArrayList<>();
    HRM heartRateMonitor;

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

    @BindView(R.id.ageInput)
    TextInputLayout ageInput;

    @BindView(R.id.maxHRInput)
    TextInputLayout maxHRInput;

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

        maxHRPercent.setText(String.valueOf(hrCalculation.getPercentHRmax(
                maxHRInput.getEditText().getText().toString(),
                ageInput.getEditText().getText().toString(),
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
