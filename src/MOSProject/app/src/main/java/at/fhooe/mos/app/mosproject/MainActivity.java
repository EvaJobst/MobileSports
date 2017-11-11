package at.fhooe.mos.app.mosproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements StepEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private SensorRecorder sensorRecorder;
    private StepDetector stepDetector;
    private SensorSimulator sensorSimulator;

    private TextView infoTextView;
int index = 0;
    ArrayList<SensorEventData> recordedSensorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context thisContext = this;

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorRecorder = new SensorRecorder();

        stepDetector = new StepDetector();

        stepDetector.registerListener(this);

        sensorSimulator = new SensorSimulator();
        sensorSimulator.registerListener(stepDetector);

        recordedSensorData = loadData();

        final LineChart lineChart = findViewById(R.id.lineChart);

        infoTextView = findViewById(R.id.infoTextView);

        final Button startRecordingButton = findViewById(R.id.startRecordingButton);
        final Button stopRecordingButton = findViewById(R.id.stopRecordingButton);
        final Button saveSensorDataButton = findViewById(R.id.saveSensorDataButton);
        final Button startSensorSimulator = findViewById(R.id.startSensorSimulator);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorRecorder.clearRecordedData();
                sensorRecorder.startRecording();
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorRecorder.stopRecording();

                recordedSensorData = sensorRecorder.getRecordedData();

                drawChart(lineChart, recordedSensorData);

                Toast.makeText(thisContext, "recorded " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
            }
        });

        saveSensorDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(recordedSensorData);

                Toast.makeText(thisContext, "saved " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
            }
        });

        startSensorSimulator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorSimulator.stop();

                if(recordedSensorData != null)
                {
                    sensorSimulator.setSensorEvents(recordedSensorData/*.subList(recordedSensorData.size()/5,recordedSensorData.size()/5+recordedSensorData.size()/5)*/);

                    sensorSimulator.start();

                    drawChart(lineChart, recordedSensorData/*.subList(recordedSensorData.size()/5,recordedSensorData.size()/5+recordedSensorData.size()/5)*/);
                }
            }
        });
    }


    private void drawChart(LineChart lineChart, List<SensorEventData> sensorData){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(sensorData.size() == 0)
            return;

        for(int x = 0;x<sensorData.get(0).getValues().length;x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<sensorData.size();i++){
                entries.add(new Entry(i, sensorData.get(i).getValues()[x]));
            }

            LineDataSet dataSet = new LineDataSet(entries, "x=" + x);

            if(x==0)
                dataSet.setColor(Color.rgb(255,0,0));
            if(x==1)
                dataSet.setColor(Color.rgb(0,255,0));
            if(x==2)
                dataSet.setColor(Color.rgb(0,0,255));

            dataSets.add(dataSet);
        }


        LineData data = new LineData(dataSets);

        lineChart.clear();
        lineChart.setData(data);
    }

    private ArrayList<SensorEventData> loadData(){
        SharedPreferences prefs = getSharedPreferences("accelerometerData", Context.MODE_PRIVATE);

        Type listType = new TypeToken<ArrayList<SensorEventData>>(){}.getType();

        String jsonString = prefs.getString("data2", "[]");
        ArrayList<SensorEventData> data = new Gson().fromJson(jsonString, listType);

        return data;

    }

    private void saveData(ArrayList<SensorEventData> recordedSensorData){
        SharedPreferences prefs = getSharedPreferences("accelerometerData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String jsonString = new Gson().toJson(recordedSensorData);
        editor.putString("data2", jsonString);

        editor.commit();
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(sensorRecorder);
    }

    protected void onResume() {
        super.onResume();
        // SENSOR_DELAY_UI: 60,000 microsecond delay
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        // SENSOR_DELAY_FASTEST: 0 microsecond delay
        senSensorManager.registerListener(sensorRecorder, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStepEvent() {
        infoTextView.setText("steps: " + stepDetector.getTotalStepCount());
    }
}
