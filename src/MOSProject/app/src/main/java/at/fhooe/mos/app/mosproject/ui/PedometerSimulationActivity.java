package at.fhooe.mos.app.mosproject.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.fhooe.mos.app.mosproject.PersistenceManager;
import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.model.user.UserInformation;
import at.fhooe.mos.app.mosproject.pedometer.Pedometer;
import at.fhooe.mos.app.mosproject.pedometer.StepCalculation;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SensorEventData;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SensorRecorder;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SensorSimulator;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SimulationFinishedEvent;
import at.fhooe.mos.app.mosproject.pedometer.StepEventListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PedometerSimulationActivity extends AppCompatActivity implements StepEventListener, SimulationFinishedEvent {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorRecorder sensorRecorder;
    private SensorSimulator sensorSimulator;
    private PersistenceManager persistenceManager;

    private Pedometer pedometer;
    private int stepCounter = 0;
    UserInformation userInformation;
    long startTime;
    ArrayList<SensorEventData> recordedSensorData;

    boolean liveStepDetectionActive = false;

    @BindView(R.id.lineChart)
    LineChart lineChart;

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

    @BindView(R.id.recordingButton)
    Button recordingButton;

    @BindView(R.id.startSensorSimulator)
    Button startSensorSimulator;

    @OnClick(R.id.recordingButton)
    public void onRecordingClick() {
        if(liveStepDetectionActive) {
            Toast.makeText(PedometerSimulationActivity.this, "live step detection is running", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!sensorRecorder.isRecording()) {
            sensorRecorder.clearRecordedData();
            sensorRecorder.startRecording();

            resetStepCounter();
            lineChart.clear();

            recordingButton.setText("Save");
            Toast.makeText(PedometerSimulationActivity.this, "recording started", Toast.LENGTH_SHORT).show();
        }
        else {
            sensorRecorder.stopRecording();
            recordedSensorData = sensorRecorder.getRecordedData();
            saveData(recordedSensorData);

            drawRecordedSensorDataChart();

            recordingButton.setText("New");
            Toast.makeText(PedometerSimulationActivity.this, "recorded " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.startSensorSimulator)
    public void onStartSensorSimulatorClick() {
        if(liveStepDetectionActive) {
            Toast.makeText(PedometerSimulationActivity.this, "live step detection is running", Toast.LENGTH_SHORT).show();
            return;
        }

        resetStepCounter();

        sensorSimulator = new SensorSimulator();
        sensorSimulator.registerSimulationFinishedEvent(PedometerSimulationActivity.this);

        pedometer = new Pedometer();
        pedometer.enableChartValueGeneration();
        pedometer.registerListener(PedometerSimulationActivity.this);

        sensorSimulator.registerListener(pedometer);

        if(recordedSensorData != null) {
            sensorSimulator.setSensorEvents(recordedSensorData);
            sensorSimulator.start();
            drawRecordedSensorDataChart();
        }

        startTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_simulation);
        ButterKnife.bind(this);

        userInformation = new Gson().fromJson(getIntent().getExtras().get("user").toString(), UserInformation.class);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorRecorder = new SensorRecorder();

        persistenceManager = new PersistenceManager(this);
        recordedSensorData = loadData();

        DecimalFormat df = new DecimalFormat("#.##");
        strideLength.setText(df.format(userInformation.getStrideLength() / 100));
    }


    private void drawRecordedSensorDataChart(){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(recordedSensorData.size() == 0)
            return;

        for(int x = 0; x < recordedSensorData.get(0).getValues().length; x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<recordedSensorData.size();i++) {
                entries.add(new Entry(i, recordedSensorData.get(i).getValues()[x]));
            }

            LineDataSet dataSet = new LineDataSet(entries, "x=" + x);
            dataSet = ChartVisualization.pedometerSimulationData(dataSet, x);
            dataSets.add(dataSet);
        }


        LineData data = new LineData(dataSets);

        lineChart.clear();
        lineChart.setData(data);
    }

    private void drawStepDetectionDetailsChart(){
        if(pedometer == null)
            return;

        List<float[]> data = pedometer.getChartValues();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(data.size() == 0)
            return;

        for(int x = 0; x < data.get(0).length; x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<data.size();i++){
                entries.add(new Entry(i, data.get(i)[x]));
            }

            LineDataSet dataSet = new LineDataSet(entries, "x=" + x);
            dataSet = ChartVisualization.pedometerResultsData(dataSet, x);
            dataSets.add(dataSet);
        }

        LineData lineData = new LineData(dataSets);

        lineChart.clear();
        lineChart.setData(lineData);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorRecorder);
    }

    protected void onResume() {
        super.onResume();
        // SENSOR_DELAY_UI: 60,000 microsecond delay
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        // SENSOR_DELAY_FASTEST: 0 microsecond delay
        sensorManager.registerListener(sensorRecorder, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    private void resetStepCounter(){
        stepCounter = 0;
        resetUI();
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

    private ArrayList<SensorEventData> loadData(){
        return persistenceManager.getAccelerometer();
    }

    private void saveData(ArrayList<SensorEventData> recordedSensorData){
        persistenceManager.setAccelerometer(recordedSensorData);
    }

    @Override
    public void onStepDetected() {
        stepCounter++;
        updateUI();
    }

    @Override
    public void onSimulationFinished() {
        drawStepDetectionDetailsChart();
    }
}
