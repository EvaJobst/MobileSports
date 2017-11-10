package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

/**
 * Created by stefan on 10.11.2017.
 */

public class StepCounter implements SensorEventListener, SimulatedSensorEventListener {

    private ArrayList<StepEventListener> stepEventListeners = new ArrayList<>();

    private int totalStepCount = 0;

    @Override   //from SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        processNewSensorValues(sensorEvent.values);
    }

    @Override   //from SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override   //from SimulatedSensorEventListener
    public void onSensorChanged(SensorEventData sensorEventData) {
        processNewSensorValues(sensorEventData.getValues());
    }

    private void processNewSensorValues(float[] values)
    {
        totalStepCount++;

        notifyListeners();
    }

    private void notifyListeners(){
        for (StepEventListener listener : stepEventListeners) {
            listener.onStepEvent();
        }
    }

    public void registerListener(StepEventListener listener)
    {
        stepEventListeners.add(listener);
    }

    public int getTotalStepCount(){
        return totalStepCount;
    }

}
