package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

/**
 * Created by stefan on 09.11.2017.
 */

public class AccelerometerSensor implements SensorEventListener {

    private ArrayList<float[]> accelerometerSensorEvents = new ArrayList<>();

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelerometerSensorEvents.add(new float[]{x, y, z});
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public ArrayList<float[]> getRecordedSensorData(){
        return accelerometerSensorEvents;
    }
}
