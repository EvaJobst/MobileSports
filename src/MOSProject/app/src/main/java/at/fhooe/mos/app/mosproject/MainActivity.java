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

        // SENSOR_DELAY_UI: 60,000 microsecond delay
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        // SENSOR_DELAY_FASTEST: 0 microsecond delay
        senSensorManager.registerListener(sensorRecorder, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);

        final LineChart lineChart = findViewById(R.id.lineChart);

        infoTextView = findViewById(R.id.infoTextView);

        final Button startRecordingButton = findViewById(R.id.startRecordingButton);
        final Button stopRecordingButton = findViewById(R.id.stopRecordingButton);
        final Button saveSensorDataButton = findViewById(R.id.saveSensorDataButton);
        final Button startSensorSimulator = findViewById(R.id.startSensorSimulator);

        lineChart.setData(new LineData(new String[]{}));

        LineData lineData = lineChart.getData();
        LineDataSet lds1 = new LineDataSet(new ArrayList<Entry>(), "x=" + 1);
        lds1.setColor(Color.rgb(255,0,0));
        LineDataSet lds2 = new LineDataSet(new ArrayList<Entry>(), "x=" + 2);
        lds2.setColor(Color.rgb(0,255,0));
        LineDataSet lds3 = new LineDataSet(new ArrayList<Entry>(), "x=" + 3);
        lds3.setColor(Color.rgb(0,0,255));

        lineData.addDataSet(lds1);
        lineData.addDataSet(lds2);
        lineData.addDataSet(lds3);

        StepDetector.DebugEventListener debugEventListener = new StepDetector.DebugEventListener() {
            @Override
            public void onDebugEvent(float[] data) {

                LineData lineData = lineChart.getData();

                if (lineData != null) {

                    // get the dataset where you want to add the entry
                    LineDataSet set1 = lineData.getDataSetByIndex(0);
                    LineDataSet set2 = lineData.getDataSetByIndex(1);
                    LineDataSet set3 = lineData.getDataSetByIndex(2);

                    if(set1.getEntryCount() > 200)
                    {
                        set1.removeEntry(0);
                        set2.removeEntry(0);
                        set3.removeEntry(0);
                    }

                    set1.addEntry(new Entry(data[0], index));
                    set2.addEntry(new Entry(data[1], index));
                    set3.addEntry(new Entry(data[2], index));
                    set1.notifyDataSetChanged();
                    set2.notifyDataSetChanged();
                    set3.notifyDataSetChanged();
                    // add a new x-value first
                    lineData.getXVals().add("" + index);
                    //lineData.addEntry(new Entry(data[0], index++), 0);
                    lineData.notifyDataChanged();
                    // let the chart know it's data has changed
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                    index++;
                }

            }
        };

        sensorRecorder = new SensorRecorder();

        //sensorRecorder.registerDebugListener(debugEventListener);

        stepDetector = new StepDetector();

        stepDetector.registerListener(this);

        stepDetector.registerDebugListener(debugEventListener);



        sensorSimulator = new SensorSimulator();
        sensorSimulator.registerListener(stepDetector);
        recordedSensorData = loadData();

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
                    //filter
/*
                    for(int i=3;i<recordedSensorData.size();i++){
                        SensorEventData tmpSensorEven = recordedSensorData.get(i);

                        float[] sumValues = new float[3];
                        sumValues[0] = tmpSensorEven.getValues()[0] + recordedSensorData.get(i - 1).getValues()[0] + recordedSensorData.get(i - 2).getValues()[0] + recordedSensorData.get(i - 3).getValues()[0];
                        sumValues[1] = tmpSensorEven.getValues()[1] + recordedSensorData.get(i - 1).getValues()[1] + recordedSensorData.get(i - 2).getValues()[1] + recordedSensorData.get(i - 3).getValues()[1];
                        sumValues[2] = tmpSensorEven.getValues()[2] + recordedSensorData.get(i - 1).getValues()[2] + recordedSensorData.get(i - 2).getValues()[2] + recordedSensorData.get(i - 3).getValues()[2];

                        float[] filteredValues = new float[3];
                        filteredValues[0] = sumValues[0]/4;
                        filteredValues[1] = sumValues[1]/4;
                        filteredValues[2] = sumValues[2]/4;

                        tmpSensorEven.values = filteredValues;

                        recordedSensorData.set(i, tmpSensorEven);
                    }
*/
                    sensorSimulator.setSensorEvents(recordedSensorData/*.subList(recordedSensorData.size()/5,recordedSensorData.size()/5+recordedSensorData.size()/5)*/);

                    sensorSimulator.start();

                    //drawChart(lineChart, recordedSensorData.subList(recordedSensorData.size()/5,recordedSensorData.size()/5+recordedSensorData.size()/5));
                }
            }
        });


    }


    private void drawChart(LineChart lineChart, List<SensorEventData> sensorData){

        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        for(int x = 0;x<3;x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<sensorData.size();i++){
                entries.add(new Entry(sensorData.get(i).getValues()[x], i));
                if(x==0)
                    labels.add("" + i);
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


        LineData data = new LineData(labels, dataSets);
        //dataset.setDrawCubic(true);
        //dataset.setDrawFilled(true);

        lineChart.clear();
        lineChart.setData(data);
        //lineChart.animateY(5000);
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

        /*
        try {
            FileOutputStream fos = openFileOutput("accelerometerData", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(recordedSensorData);

            os.close();
            fos.close();
        }
        catch (FileNotFoundException e)
        {

        }
        catch (IOException e)
        {

        }
        */
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(sensorRecorder);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(sensorRecorder, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStepEvent() {
        infoTextView.setText("steps: " + stepDetector.getTotalStepCount());
    }
}
