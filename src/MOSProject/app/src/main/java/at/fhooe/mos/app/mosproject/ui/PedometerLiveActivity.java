package at.fhooe.mos.app.mosproject.ui;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.StepDetectorService;
import at.fhooe.mos.app.mosproject.model.user.UserInformation;
import at.fhooe.mos.app.mosproject.pedometer.Pedometer;
import at.fhooe.mos.app.mosproject.pedometer.StepCalculation;
import at.fhooe.mos.app.mosproject.pedometer.StepEventListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PedometerLiveActivity extends AppCompatActivity {
    int stepCounter = 0;
    UserInformation userInformation;
    long startTime;
    Boolean isLiveDetectionActive = false;
    SensorManager sensorManager;
    Sensor sensorAccelerometer;
    Intent stepDetectorServiceIntent;

    @BindView(R.id.stepCount)
    TextView stepCount;

    @BindView(R.id.strideLength)
    TextView strideLength;

    @BindView(R.id.cadence)
    TextView cadence;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.speed)
    TextView speed;

    @BindView(R.id.kCal)
    TextView kCal;

    @BindView(R.id.startStopLive)
    Button startStopLive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_live);
        ButterKnife.bind(this);

        userInformation = new Gson().fromJson(getIntent().getExtras().get("user").toString(), UserInformation.class);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        stepDetectorServiceIntent = new Intent(this, StepDetectorService.class);
    }

    @Override
    protected void onPause() {
        if(isLiveDetectionActive) {
            EventBus.getDefault().unregister(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(isLiveDetectionActive){
            EventBus.getDefault().register(this);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(isLiveDetectionActive){
            EventBus.getDefault().unregister(this);
            stopService(stepDetectorServiceIntent);
        }
        super.onDestroy();
    }

    @OnClick(R.id.startStopLive)
    public void onStartStopLiveClick() {
        if(!isLiveDetectionActive) {
            isLiveDetectionActive = true;

            resetUI();
            strideLength.setText(String.valueOf(userInformation.getStrideLength()/100));

            startTime = Calendar.getInstance().getTimeInMillis();
            EventBus.getDefault().register(this);
            startService(stepDetectorServiceIntent);

            startStopLive.setText("Stop");
            Toast.makeText(this, "live step detection started", Toast.LENGTH_SHORT).show();
        }
        else {
            isLiveDetectionActive = false;
            stopService(stepDetectorServiceIntent);
            EventBus.getDefault().unregister(this);

            startStopLive.setText("Start");
            Toast.makeText(this, "live step detection stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(){
        DecimalFormat df = new DecimalFormat("#.##");
        long millis = Calendar.getInstance().getTimeInMillis() - startTime;
        int strideLength = userInformation.getStrideLength();

        // DISTANCE
        double currentDistance = StepCalculation.getDistanceInMeter(strideLength, stepCounter);
        distance.setText(df.format(currentDistance));

        // SPEED
        double currentSpeed = StepCalculation.getSpeedInKmH(millis, strideLength, stepCounter);
        speed.setText(df.format(currentSpeed));

        // CADENCE (Steps/Min.)
        double currentCadence = StepCalculation.getCadenceInMin(stepCounter, millis);
        cadence.setText(df.format(currentCadence));

        // KCAL
        double currentkCal = StepCalculation.getKcalFromWeight(userInformation.getBodyMass(), strideLength, stepCounter);
        double previousKCal = Double.valueOf(kCal.getText().toString());
        kCal.setText(df.format(previousKCal + currentkCal));

        // STEPS
        stepCount.setText(String.valueOf(stepCounter));
    }

    private void resetUI() {
        stepCount.setText("0");
        distance.setText("0");
        speed.setText("0");
        cadence.setText("0");
        kCal.setText("0");
        kCal.setText("0");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStepCountEvent(StepDetectorService.StepCountEvent event) {
        stepCounter++;
        updateUI();
    }
}
