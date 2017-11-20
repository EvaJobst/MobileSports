package at.fhooe.mos.app.mosproject.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

import at.fhooe.mos.app.mosproject.pedometer.simulator.SimulatedSensorEventListener;

/**
 * Created by stefan on 10.11.2017.
 */

public class StepDetector implements SensorEventListener, SimulatedSensorEventListener {
    public static final float PRECISION = 0.20f;
    public static final float MIN_DYNAMIC_PRECISION = 0.5f;
    public static final int MIN_TIME_BETWEEN_STEPS_MS = 200;
    private ArrayList<float[]> chartValues = new ArrayList<>();

    private float[] min = minInstance();
    private float[] max = maxInstance();

    private float[] lastMin = new float[]{0, 0, 0};
    private float[] lastMax = new float[]{0, 0, 0};

    private float[] dynamicThreshold = new float[]{0, 0, 0};
    private float[] dynamicPrecision = new float[]{0, 0, 0};
    private float[] oldSample = new float[]{0, 0, 0};
    private float[] newSample = new float[]{0, 0, 0};

    private int largestAxesIdx = -1;

    private ArrayList<StepEventListener> stepEventListeners = new ArrayList<>();

    private int samplingCounter = 0;

    private long lastDetectedStepTimeMs = 0;

    private boolean generateChartValues = false;

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
        for (int i = 0; i < result.length; i++) {
            max[i] = Math.max(max[i], result[i]);
            min[i] = Math.min(min[i], result[i]);
        }

        samplingCounter++;

        if (samplingCounter == 50) {
            samplingCounter = 0;

            float[] maxAbsolute = new float[3];

            for (int i = 0; i < max.length; i++) {
                lastMin[i] = min[i];
                lastMax[i] = max[i];

                dynamicThreshold[i] = (max[i] + min[i]) / 2;

                dynamicPrecision[i] = PRECISION * Math.abs(max[i] - min[i]);
                dynamicPrecision[i] = Math.max(dynamicPrecision[i], MIN_DYNAMIC_PRECISION);

                maxAbsolute[i] = Math.max(Math.abs(max[i]), Math.abs(min[i]));
            }

            if (maxAbsolute[0] > maxAbsolute[1] && maxAbsolute[0] > maxAbsolute[2]) {
                largestAxesIdx = 0;
            } else if (maxAbsolute[1] > maxAbsolute[0] && maxAbsolute[1] > maxAbsolute[2]) {
                largestAxesIdx = 1;
            } else if (maxAbsolute[2] > maxAbsolute[0] && maxAbsolute[2] > maxAbsolute[1]) {
                largestAxesIdx = 2;
            }

            min = minInstance();
            max = maxInstance();
        }

        for (int i = 0; i < newSample.length; i++) {
            if (Math.abs(result[i] - newSample[i]) > dynamicPrecision[i]) {
                newSample[i] = result[i];
            }
        }

        float[] chartValue = new float[]{0, 0, 0, 0, 0};

        if (largestAxesIdx != -1) {
            if (oldSample[largestAxesIdx] > dynamicThreshold[largestAxesIdx] &&
                    dynamicThreshold[largestAxesIdx] > newSample[largestAxesIdx]) {
                if((System.currentTimeMillis() - lastDetectedStepTimeMs) > MIN_TIME_BETWEEN_STEPS_MS){
                    notifyListeners(); // if a step has been detected

                    chartValue[4] = result[largestAxesIdx];
                }

                lastDetectedStepTimeMs = System.currentTimeMillis();
            }

            chartValue[0] = result[largestAxesIdx];
            chartValue[1] = dynamicThreshold[largestAxesIdx];
            chartValue[2] = lastMin[largestAxesIdx];
            chartValue[3] = lastMax[largestAxesIdx];
        }

        for (int i = 0; i < oldSample.length; i++) {
            oldSample[i] = newSample[i];
        }

        if(generateChartValues){
            chartValues.add(chartValue);
        }
    }

    private void notifyListeners() {
        for (StepEventListener listener : stepEventListeners) {
            listener.onStepEvent();
        }
    }

    private float[] minInstance() {
        return new float[]{
                Float.MAX_VALUE,
                Float.MAX_VALUE,
                Float.MAX_VALUE
        };
    }

    private float[] maxInstance() {
        return new float[]{
                Float.MAX_VALUE * (-1),  //MAX_VALUE * -1 is the lowest number, MIN_VALUE is the lowest precision of float
                Float.MAX_VALUE * (-1),
                Float.MAX_VALUE * (-1)
        };
    }

    public void registerListener(StepEventListener listener) {
        stepEventListeners.add(listener);
    }

    public void enableChartValueGeneration(){
        generateChartValues = true;
    }

    public ArrayList<float[]> getChartValues() {
        return chartValues;
    }
}
