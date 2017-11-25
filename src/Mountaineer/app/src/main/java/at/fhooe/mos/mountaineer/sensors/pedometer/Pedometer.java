package at.fhooe.mos.mountaineer.sensors.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 10.11.2017.
 */

public class Pedometer extends EventSource<PedometerEventListener> implements SensorEventListener {
    public static final float PRECISION = 0.20f;
    public static final float MIN_DYNAMIC_PRECISION = 0.5f;
    public static final int MIN_TIME_BETWEEN_STEPS_MS = 200;
    public static final int MAX_TIME_BETWEEN_STEPS_MS = 2000;

    private float[] minAcceleration = minInstance();
    private float[] maxAcceleration = maxInstance();
    private float[] dynamicThreshold = new float[]{0, 0, 0};
    private float[] dynamicPrecision = new float[]{0, 0, 0};
    private float[] oldSample = new float[]{0, 0, 0};
    private float[] newSample = new float[]{0, 0, 0};

    private int largestAxesIdx = -1;
    private int samplingCounter = 0;
    private long lastDetectedStepTimeMs = 0;

    // Higher filter amount than in the original algorithm
    // to increase the detection accuracy on our phones
    private AverageFilter[] averageFilters = new AverageFilter[]
            {
                    new AverageFilter(6), //x
                    new AverageFilter(6), //y
                    new AverageFilter(6)  //z
            };

    @Override   //from SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;

        //three acceleration values for each axis
        if (sensorEvent.values.length != 3)
            return;

        processNewSensorValues(sensorEvent.values);
    }

    @Override   //from SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * Implementation of the algorithm to detect steps from
     * http://www.analog.com/en/analog-dialogue/articles/pedometer-design-3-axis-digital-acceler.html.
     * For additional comments on the algorithmn see
     * http://groups.inf.ed.ac.uk/teaching/slipa11-12/reports/Marat.htm
     *
     * @param values
     */
    private void processNewSensorValues(float[] values) {
        // DIGITAL FILTER
        float[] result = new float[values.length];

        for (int i = 0; i < averageFilters.length; i++) {
            result[i] = averageFilters[i].filter(values[i]);
        }

        // STEP DETECTION
        for (int i = 0; i < result.length; i++) {
            maxAcceleration[i] = Math.max(maxAcceleration[i], result[i]);
            minAcceleration[i] = Math.min(minAcceleration[i], result[i]);
        }

        samplingCounter++;

        if (samplingCounter == 50) {
            samplingCounter = 0;

            float[] maxAbsolute = new float[3];

            for (int i = 0; i < maxAcceleration.length; i++) {
                dynamicThreshold[i] = (maxAcceleration[i] + minAcceleration[i]) / 2;

                dynamicPrecision[i] = PRECISION * Math.abs(maxAcceleration[i] - minAcceleration[i]);
                dynamicPrecision[i] = Math.max(dynamicPrecision[i], MIN_DYNAMIC_PRECISION);

                maxAbsolute[i] = Math.max(Math.abs(maxAcceleration[i]), Math.abs(minAcceleration[i]));
            }

            if (maxAbsolute[0] > maxAbsolute[1] && maxAbsolute[0] > maxAbsolute[2]) {
                largestAxesIdx = 0;
            } else if (maxAbsolute[1] > maxAbsolute[0] && maxAbsolute[1] > maxAbsolute[2]) {
                largestAxesIdx = 1;
            } else if (maxAbsolute[2] > maxAbsolute[0] && maxAbsolute[2] > maxAbsolute[1]) {
                largestAxesIdx = 2;
            }

            minAcceleration = minInstance();
            maxAcceleration = maxInstance();
        }

        for (int i = 0; i < newSample.length; i++) {
            if (Math.abs(result[i] - newSample[i]) > dynamicPrecision[i]) {
                newSample[i] = result[i];
            }
        }

        if (largestAxesIdx != -1) {
            if (oldSample[largestAxesIdx] > dynamicThreshold[largestAxesIdx] &&
                    newSample[largestAxesIdx] < dynamicThreshold[largestAxesIdx]) {
                if ((System.currentTimeMillis() - lastDetectedStepTimeMs) > MIN_TIME_BETWEEN_STEPS_MS &&
                        (System.currentTimeMillis() - lastDetectedStepTimeMs) < MAX_TIME_BETWEEN_STEPS_MS) {

                    notifyListeners(); // if a step has been detected
                }

                lastDetectedStepTimeMs = System.currentTimeMillis();
            }
        }

        for (int i = 0; i < oldSample.length; i++) {
            oldSample[i] = newSample[i];
        }
    }

    protected void notifyListeners() {
        for (PedometerEventListener listener : eventListeners) {
            listener.onStepDetectedEvent();
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

}
