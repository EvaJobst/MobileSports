package at.fhooe.mos.mountaineer.sensors.pedometer;

import android.content.Context;
import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 05.12.2017.
 */

public class PedometerManagerSimulator extends EventSource<PedometerEventListener> implements PedometerManagerInterface {
    private Handler handler;
    private PeriodicRunnable periodicRunnable;
    private int nextRunTimeMs;

    public void setup(Context context) {
        if(handler != null){
            throw new RuntimeException("PedometerManagerSimulator is already set up. setup() called more than once!");
        }

        handler = new Handler();
        periodicRunnable = new PeriodicRunnable();
        nextRunTimeMs = 1000;
        handler.postDelayed(periodicRunnable, nextRunTimeMs);
    }


    public void destroy(){
        handler.removeCallbacks(periodicRunnable);
    }

    private class PeriodicRunnable implements Runnable{

        @Override
        public void run() {
            for (PedometerEventListener listener : eventListeners) {
                listener.onStepDetectedEvent();
            }

            handler.postDelayed(this, nextRunTimeMs);
        }
    }
}
