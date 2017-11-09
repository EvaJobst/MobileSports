package at.fhooe.mos.app.mosproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private AccelerometerSensor accelerometerSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView infoTextView = (TextView) findViewById(R.id.infoTextView);

        final Button saveSensorDataButton = (Button) findViewById(R.id.saveSensorDataButton);

        saveSensorDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<float[]> recordedSensorData = accelerometerSensor.getRecordedSensorData();

                saveData(recordedSensorData);

                infoTextView.setText("saved " + recordedSensorData.size() + "sensor events");

            }
        });

        accelerometerSensor = new AccelerometerSensor();

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(accelerometerSensor, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
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
