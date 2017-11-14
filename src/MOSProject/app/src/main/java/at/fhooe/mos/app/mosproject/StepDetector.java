package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by stefan on 10.11.2017.
 */

public class StepDetector implements SensorEventListener, SimulatedSensorEventListener {
    ArrayList<float[]> chartValues = new ArrayList<>();

    private float[] min = minInstance();
    private float[] max = maxInstance();

    private float[] threshold = new float[3];
    private float[] precision = new float[3];
    private float[] oldSample = new float[3];
    private float[] newSample = new float[3];
    private float[] accelerationChange = new float[3];
    private int previousIdx = 0;

    private ArrayList<StepEventListener> stepEventListeners = new ArrayList<>();

    private int samplingCounter = 0;

    private AverageFilter[] averageFilters = new AverageFilter[]
            {
                    new AverageFilter(4), //x
                    new AverageFilter(4), //y
                    new AverageFilter(4)  //z
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
        // DIGITAL FILTER
        float[] result = new float[values.length];

        for (int i = 0; i < averageFilters.length; i++) {
            result[i] = averageFilters[i].filter(values[i]);
        }


        // STEP DETECTION
        for(int j = 0; j < result.length; j++) {
            if(max[j] < result[j]) {
                max[j] = result[j];
            }

            if(min[j] > result[j]) {
                min[j] = result[j];
            }
        }

        float newMin = 0;
        float newMax = 0;

        if(min[previousIdx] != Float.MAX_VALUE) {
            newMin = min[previousIdx];
        }

        if(max[previousIdx] != Float.MIN_VALUE) {
            newMax = max[previousIdx];
        }

        samplingCounter++;

        if(samplingCounter == 50) {
            samplingCounter = 0;

            for(int i = 0; i < max.length; i++) {
                threshold[i] = (max[i] + min[i])/2;
                precision[i] = 0.15f * Math.abs(max[i] - min[i]);
            }

            min = minInstance();
            max = maxInstance();
        }

        accelerationChange = max;

        for(int i = 0; i < newSample.length; i++) {
            if(Math.abs(result[i] - newSample[i]) > precision[i] ) {
                newSample[i] = result[i];

                //
            }
        }

        int largestIdx = -1;

        if(accelerationChange[0] > accelerationChange[1] &&
                accelerationChange[0] > accelerationChange[2]) {
            largestIdx = 0;
        }
        else if(accelerationChange[1] > accelerationChange[0] &&
                accelerationChange[1] > accelerationChange[2]) {
            largestIdx = 1;
        }
        else if(accelerationChange[2] > accelerationChange[0] &&
                accelerationChange[2] > accelerationChange[1]) {
            largestIdx = 2;
        }

        float stepHeight = 0;

        if(largestIdx != -1) {
            if(Math.abs(oldSample[largestIdx]) > Math.abs(threshold[largestIdx]) &&
                    Math.abs(threshold[largestIdx]) > Math.abs(newSample[largestIdx])) {
                notifyListeners(); // if a step has been detected
                previousIdx = largestIdx;
                stepHeight = result[previousIdx];
            }
        }

        for(int i = 0; i < oldSample.length; i++) {
            oldSample[i] = newSample[i];
        }


        float[] chartValue = new float[] {
                result[previousIdx],
                threshold[previousIdx],
                newMin,
                newMax,
                stepHeight
        };

        chartValues.add(chartValue);
    }

    private void notifyListeners() {
        for (StepEventListener listener : stepEventListeners) {
            listener.onStepEvent();
        }
    }

    private float[] minInstance() {
        return new float[] {
            Float.MAX_VALUE,
            Float.MAX_VALUE,
            Float.MAX_VALUE
        };
    }

    private float[] maxInstance() {
        return new float[] {
            Float.MIN_VALUE,
            Float.MIN_VALUE,
            Float.MIN_VALUE
        };
    }

    public void registerListener(StepEventListener listener) {
        stepEventListeners.add(listener);
    }

    public ArrayList<float[]> getChartValues() {
        return chartValues;
    }
}
