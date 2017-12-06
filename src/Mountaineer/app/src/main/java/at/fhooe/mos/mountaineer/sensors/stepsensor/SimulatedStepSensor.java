package at.fhooe.mos.mountaineer.sensors.stepsensor;

import android.content.Context;
import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 05.12.2017.
 */

public class SimulatedStepSensor extends EventSource<StepSensorEventListener> implements StepSensor {
    private Handler handler;
    private PeriodicRunnable periodicRunnable;
    private int nextRunInMs;

    public void setup(Context context) {
        if (handler != null) {
            throw new RuntimeException("SimulatedStepSensor is already set up. setup() called more than once!");
        }

        handler = new Handler();
        periodicRunnable = new PeriodicRunnable();
        nextRunInMs = 1000;
        handler.postDelayed(periodicRunnable, nextRunInMs);
    }


    public void destroy() {
        handler.removeCallbacks(periodicRunnable);
    }

    private class PeriodicRunnable implements Runnable {

        @Override
        public void run() {
            for (StepSensorEventListener listener : eventListeners) {
                listener.onStepDetectedEvent();
            }

            handler.postDelayed(this, nextRunInMs);
        }
    }
}
