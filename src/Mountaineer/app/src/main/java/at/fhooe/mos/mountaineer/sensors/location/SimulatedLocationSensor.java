package at.fhooe.mos.mountaineer.sensors.location;

import android.content.Context;
import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by Eva on 30.11.2017.
 */

public class SimulatedLocationSensor extends EventSource<LocationSensorEventListener> implements LocationSensor {

    private Handler handler;
    private PeriodicRunnable periodicRunnable;
    private int nextRunInMs;

    @Override
    public void setup(Context context) {
        handler = new Handler();
        periodicRunnable = new PeriodicRunnable();
        nextRunInMs = 10_000;
        handler.postDelayed(periodicRunnable, nextRunInMs);
    }

    @Override
    public void destroy() {
        handler.removeCallbacks(periodicRunnable);
    }

    private class PeriodicRunnable implements Runnable{

        @Override
        public void run() {
            for (LocationSensorEventListener listener : eventListeners) {
                listener.onLocationReceivedEvent(45.123, 40.567);
            }

            handler.postDelayed(this, nextRunInMs);
        }
    }
}
