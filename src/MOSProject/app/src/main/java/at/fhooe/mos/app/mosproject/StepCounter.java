package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Created by stefan on 10.11.2017.
 */

public class StepCounter implements SensorEventListener, SimulatedSensorEventListener {

    private ArrayList<StepEventListener> stepEventListeners = new ArrayList<>();

    DebugEventListener debugListener;

    private int totalStepCount = 0;

    private float[][] filterBuffer = new float[][]{{0,0,0,0}, {0,0,0,0}, {0,0,0,0}};

    @Override   //from SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        if(sensorEvent.values.length != 3)
            return;

        processNewSensorValues(sensorEvent.values);
    }

    @Override   //from SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override   //from SimulatedSensorEventListener
    public void onSensorChanged(SensorEventData sensorEventData) {
        if(sensorEventData.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        if(sensorEventData.getValues().length != 3)
            return;

        processNewSensorValues(sensorEventData.getValues());
    }

    private float[] filter(float[] values)
    {
        float[] filteredVales = new float[values.length];

        for(int x = 0; x < filterBuffer.length ; x++)
        {
            float sum = 0;
            for (int i = 0; i < filterBuffer[x].length - 1; i++) {
                filterBuffer[x][i] = filterBuffer[x][i + 1];

                sum += filterBuffer[x][i];
            }

            sum += values[x];

            filterBuffer[x][filterBuffer[x].length - 1] = values[x];

            filteredVales[x] = sum / filterBuffer[x].length;
        }

        return filteredVales;
    }

    private void processNewSensorValues(float[] values)
    {
        float[] filteredValues = filter(values);

        if(debugListener != null)
            debugListener.onDebugEvent(filteredValues);

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

    public void registerDebugListener(DebugEventListener debugListener){
        this.debugListener = debugListener;
    }

    public int getTotalStepCount(){
        return totalStepCount;
    }

    public interface DebugEventListener{
        void onDebugEvent(float[] data);
    }
}
