package at.fhooe.mos.app.mosproject.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mos.app.mosproject.PersistenceManager;
import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.pedometer.SensorEventData;
import at.fhooe.mos.app.mosproject.pedometer.SensorRecorder;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SensorSimulator;
import at.fhooe.mos.app.mosproject.pedometer.simulator.SimulationFinishedEvent;
import at.fhooe.mos.app.mosproject.pedometer.StepDetector;
import at.fhooe.mos.app.mosproject.pedometer.StepEventListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepDetectorTestActivity extends AppCompatActivity implements StepEventListener, SimulationFinishedEvent {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorRecorder sensorRecorder;
    private SensorSimulator sensorSimulator;
    private PersistenceManager persistenceManager;

    private StepDetector stepDetector;
    private int stepCounter = 0;

    int index = 0;
    ArrayList<SensorEventData> recordedSensorData;

    boolean liveStepDetectionActive = false;

    @BindView(R.id.lineChart)
    LineChart lineChart;

    @BindView(R.id.infoTextView)
    TextView infoTextView;

    @BindView(R.id.recordingButton)
    Button recordingButton;

    @BindView(R.id.liveStepDetection)
    Button liveStepDetection;

    @BindView(R.id.saveSensorDataButton)
    Button saveSensorDataButton;

    @BindView(R.id.startSensorSimulator)
    Button startSensorSimulator;

    @OnClick(R.id.recordingButton)
    public void onRecordingClick() {
        if(liveStepDetectionActive) {
            Toast.makeText(StepDetectorTestActivity.this, "live step detection is running", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!sensorRecorder.isRecording()) {
            sensorRecorder.clearRecordedData();
            sensorRecorder.startRecording();

            resetStepCounter();
            lineChart.clear();

            recordingButton.setText("stop recording");
            Toast.makeText(StepDetectorTestActivity.this, "recording started", Toast.LENGTH_SHORT).show();
        }
        else {
            sensorRecorder.stopRecording();
            recordedSensorData = sensorRecorder.getRecordedData();

            drawRecordedSensorDataChart();

            recordingButton.setText("start recording");
            Toast.makeText(StepDetectorTestActivity.this, "recorded " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.liveStepDetection)
    public void onLiveStepDetectionClick() {
        if(!liveStepDetectionActive) {
            liveStepDetectionActive = true;

            resetStepCounter();
            lineChart.clear();

            stepDetector = new StepDetector();
            stepDetector.enableChartValueGeneration();
            stepDetector.registerListener(StepDetectorTestActivity.this);

            sensorManager.registerListener(stepDetector, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

            liveStepDetection.setText("stop live");
            Toast.makeText(StepDetectorTestActivity.this, "live step detection started", Toast.LENGTH_SHORT).show();
        }
        else {
            liveStepDetectionActive = false;

            sensorManager.unregisterListener(stepDetector);
            drawStepDetectionDetailsChart();

            liveStepDetection.setText("start live");
            Toast.makeText(StepDetectorTestActivity.this, "live step detection stopped", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.saveSensorDataButton)
    public void onSaveSensorDataClick() {
        if(liveStepDetectionActive) {
            Toast.makeText(StepDetectorTestActivity.this, "live step detection is running", Toast.LENGTH_SHORT).show();
            return;
        }

        saveData(recordedSensorData);
        Toast.makeText(StepDetectorTestActivity.this, "saved " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.startSensorSimulator)
    public void onStartSensorSimulatorClick() {
        if(liveStepDetectionActive) {
            Toast.makeText(StepDetectorTestActivity.this, "live step detection is running", Toast.LENGTH_SHORT).show();
            return;
        }

        resetStepCounter();

        sensorSimulator = new SensorSimulator();
        sensorSimulator.registerSimulationFinishedEvent(StepDetectorTestActivity.this);

        stepDetector = new StepDetector();
        stepDetector.enableChartValueGeneration();
        stepDetector.registerListener(StepDetectorTestActivity.this);

        sensorSimulator.registerListener(stepDetector);

        if(recordedSensorData != null) {
            sensorSimulator.setSensorEvents(recordedSensorData);
            sensorSimulator.start();
            drawRecordedSensorDataChart();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detector_test);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorRecorder = new SensorRecorder();

        persistenceManager = new PersistenceManager(this);
        recordedSensorData = loadData();

        ButterKnife.bind(this);
    }


    private void drawRecordedSensorDataChart(){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(recordedSensorData.size() == 0)
            return;

        for(int x = 0;x<recordedSensorData.get(0).getValues().length;x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<recordedSensorData.size();i++) {
                entries.add(new Entry(i, recordedSensorData.get(i).getValues()[x]));
            }

            LineDataSet dataSet = new LineDataSet(entries, "x=" + x);

            if(x==0) {
                dataSet.setColor(Color.rgb(255, 0, 0));
                dataSet.setDrawCircleHole(false);
                dataSet.setDrawCircles(false);
            }
            if(x==1) {
                dataSet.setColor(Color.rgb(0, 255, 0));
                dataSet.setDrawCircleHole(false);
                dataSet.setDrawCircles(false);
            }
            if(x==2) {
                dataSet.setColor(Color.rgb(0, 0, 255));
                dataSet.setDrawCircleHole(false);
                dataSet.setDrawCircles(false);
            }

            dataSets.add(dataSet);
        }


        LineData data = new LineData(dataSets);

        lineChart.clear();
        lineChart.setData(data);
    }

    private void drawStepDetectionDetailsChart(){
        if(stepDetector == null)
            return;

        List<float[]> data = stepDetector.getChartValues();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(data.size() == 0)
            return;

        for(int x = 0;x<data.get(0).length;x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<data.size();i++){
                entries.add(new Entry(i, data.get(i)[x]));
            }

            LineDataSet dataSet = new LineDataSet(entries, "x=" + x);

            if(x==0) {
                dataSet.setColor(Color.rgb(255, 0, 0));
                dataSet.setDrawCircles(false);
            }
            if(x==1) {
                dataSet.setColor(Color.argb(100, 0, 255, 0));
                dataSet.setDrawCircles(false);
            }
            if(x==2) {
                dataSet.setColor(Color.argb(75, 0, 0, 255));
                dataSet.setDrawCircles(false);
            }
            if(x==3) {
                dataSet.setColor(Color.argb(75, 200, 100, 0));
                dataSet.setDrawCircles(false);
            }
            if(x==4) {
                dataSet.setColor(Color.rgb(255, 255, 255));
                dataSet.setDrawCircleHole(false);
                dataSet.setLineWidth(0);
            }

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
        updateStepCounterTextView();
    }

    private void updateStepCounterTextView(){
        infoTextView.setText("steps: " + stepCounter);
    }

    private ArrayList<SensorEventData> loadData(){
        return persistenceManager.getAccelerometer();
    }

    private void saveData(ArrayList<SensorEventData> recordedSensorData){
        persistenceManager.setAccelerometer(recordedSensorData);
    }

    @Override
    public void onStepEvent() {
        stepCounter++;
        updateStepCounterTextView();
    }

    @Override
    public void onSimulationFinished() {
        drawStepDetectionDetailsChart();
    }
}
