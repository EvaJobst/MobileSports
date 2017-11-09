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
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private AccelerometerSensor accelerometerSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context thisContext = this;

        accelerometerSensor = new AccelerometerSensor();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        final LineChart lineChart = (LineChart) findViewById(R.id.lineChart);

        final TextView infoTextView = (TextView) findViewById(R.id.infoTextView);

        final Button startRecordingButton = (Button) findViewById(R.id.startRecordingButton);
        final Button stopRecordingButton = (Button) findViewById(R.id.stopRecordingButton);
        final Button saveSensorDataButton = (Button) findViewById(R.id.saveSensorDataButton);

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accelerometerSensor.clearRecordedData();
                senSensorManager.registerListener(accelerometerSensor, senAccelerometer , SensorManager.SENSOR_DELAY_FASTEST);
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senSensorManager.unregisterListener(accelerometerSensor);

                ArrayList<float[]> recordedSensorData = accelerometerSensor.getRecordedSensorData();

                drawChart(lineChart, recordedSensorData);

                Toast.makeText(thisContext, "recorded " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
            }
        });

        saveSensorDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<float[]> recordedSensorData = accelerometerSensor.getRecordedSensorData();

                saveData(recordedSensorData);

                Toast.makeText(thisContext, "saved " + recordedSensorData.size() + " sensor events", Toast.LENGTH_SHORT).show();
            }
        });


        ArrayList<float[]> sensorData = loadData();

        //filter
        /*
        for(int i=3;i<sensorData.size();i++){
            float[] sumValues = new float[3];
            sumValues[0] = sensorData.get(i)[0] + sensorData.get(i - 1)[0] + sensorData.get(i - 2)[0] + sensorData.get(i - 3)[0];
            sumValues[1] = sensorData.get(i)[1] + sensorData.get(i - 1)[1] + sensorData.get(i - 2)[1] + sensorData.get(i - 3)[1];
            sumValues[2] = sensorData.get(i)[2] + sensorData.get(i - 1)[2] + sensorData.get(i - 2)[2] + sensorData.get(i - 3)[2];

            float[] filteredValues = new float[3];
            filteredValues[0] = sumValues[0]/4;
            filteredValues[1] = sumValues[1]/4;
            filteredValues[2] = sumValues[2]/4;

            sensorData.set(i, filteredValues);
        }*/

        drawChart(lineChart, sensorData);
    }

    private void drawChart(LineChart lineChart, ArrayList<float[]> sensorData){

        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<LineDataSet> dataSets = new ArrayList<>();

        for(int x = 0;x<3;x++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for(int i=0;i<sensorData.size();i++){
                entries.add(new Entry(sensorData.get(i)[x], i));
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

    private ArrayList<float[]> loadData(){
        SharedPreferences prefs = getSharedPreferences("accelerometerData", Context.MODE_PRIVATE);

        Type listType = new TypeToken<ArrayList<float[]>>(){}.getType();

        String jsonString = prefs.getString("data", "[]");
        ArrayList<float[]> data = new Gson().fromJson(jsonString, listType);

        return data;
    }

    private void saveData(ArrayList<float[]> recordedSensorData){
        SharedPreferences prefs = getSharedPreferences("accelerometerData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String jsonString = new Gson().toJson(recordedSensorData);
        editor.putString("data", jsonString);

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
        senSensorManager.unregisterListener(accelerometerSensor);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(accelerometerSensor, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
