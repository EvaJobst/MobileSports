package at.fhooe.mos.mountaineer.sensors.location;

import android.content.Context;
import android.os.Handler;

import java.util.Random;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by Eva on 30.11.2017.
 */

public class SimulatedLocationSensor extends EventSource<LocationSensorEventListener> implements LocationSensor {

    private Handler handler;
    private PeriodicRunnable periodicRunnable;
    private int nextRunInMs;
    private double nextLatitude;
    private double nextLongitude;
    private double nextAltitude;
    private Random random;

    @Override
    public void setup(Context context) {
        if (handler != null) {
            throw new RuntimeException("SimulatedLocationSensor is already set up. setup() called more than once!");
        }

        handler = new Handler();
        periodicRunnable = new PeriodicRunnable();

        nextRunInMs = 10_000;
        nextLatitude = 48.123;
        nextLongitude = 14.567;
        nextAltitude = 100;
        random = new Random();

        handler.post(periodicRunnable);
    }

    @Override
    public void destroy() {
        handler.removeCallbacks(periodicRunnable);
    }

    private class PeriodicRunnable implements Runnable {

        @Override
        public void run() {
            for (LocationSensorEventListener listener : getEventListeners()) {
                listener.onLocationReceivedEvent(nextLatitude, nextLongitude, nextAltitude);
            }

            nextLatitude += (random.nextDouble() - 0.5) / 100;
            nextLongitude += (random.nextDouble() - 0.5) / 100;
            nextAltitude += (random.nextDouble() - 0.5) * 10;

            handler.postDelayed(this, nextRunInMs);
        }
    }
}
