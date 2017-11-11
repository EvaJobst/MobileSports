package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

/**
 * Created by stefan on 10.11.2017.
 */

public class StepDetector implements SensorEventListener, SimulatedSensorEventListener {

    private ArrayList<StepEventListener> stepEventListeners = new ArrayList<>();

    DebugEventListener debugListener;

    private int totalStepCount = 0;

    private AverageFilter[] averageFilters = new AverageFilter[]
            {
                    new AverageFilter(4),
                    new AverageFilter(4),
                    new AverageFilter(4)
            };

    @Override   //from SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        if (sensorEvent.values.length != 3)
            return;

        processNewSensorValues(sensorEvent.values);
    }

    @Override   //from SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override   //from SimulatedSensorEventListener
    public void onSensorChanged(SensorEventData sensorEventData) {
        if (sensorEventData.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        if (sensorEventData.getValues().length != 3)
            return;

        processNewSensorValues(sensorEventData.getValues());
    }

    private void processNewSensorValues(float[] values) {

        float[] filteredValues = new float[values.length];

        for (int i = 0; i < averageFilters.length; i++) {
            filteredValues[i] = averageFilters[i].filter(values[i]);
        }

        if (debugListener != null)
            debugListener.onDebugEvent(filteredValues);

        totalStepCount++;

        notifyListeners();
    }

    private void notifyListeners() {
        for (StepEventListener listener : stepEventListeners) {
            listener.onStepEvent();
        }
    }

    public void registerListener(StepEventListener listener) {
        stepEventListeners.add(listener);
    }

    public void registerDebugListener(DebugEventListener debugListener) {
        this.debugListener = debugListener;
    }

    public int getTotalStepCount() {
        return totalStepCount;
    }

    public interface DebugEventListener {
        void onDebugEvent(float[] data);
    }
}
