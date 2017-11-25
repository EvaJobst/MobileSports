package at.fhooe.mos.mountaineer.sensors.stopwatch;

import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;


/**
 * Created by stefan on 21.11.2017.
 */

public class Stopwatch extends EventSource<StopwatchEventListener> {
    public static final int PERIODIC_EVENT_TIME_MILLIS = 1000;

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    private Handler handler;
    private PeriodicUpdater periodicUpdater;

    public Stopwatch(){
        handler = new Handler();
        periodicUpdater = new PeriodicUpdater();
    }

    public void start() {
        startTime = System.currentTimeMillis();
        running = true;

        for(StopwatchEventListener listener : eventListeners){
            listener.onStartEvent(startTime);
        }

        handler.post(periodicUpdater);
    }

    public void stop() {
        handler.removeCallbacks(periodicUpdater);

        stopTime = System.currentTimeMillis();
        running = false;

        for(StopwatchEventListener listener : eventListeners){
            listener.onStartEvent(stopTime);
        }


    }

    public int getElapsedSeconds() {
        return (int) getElapsedMilliseconds() / 1000;
    }

    private long getElapsedMilliseconds() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }

    private class PeriodicUpdater implements Runnable {
        @Override
        public void run() {

            if(running){
                for(StopwatchEventListener listener : eventListeners){
                    listener.onElapsedSecondsEvent(getElapsedSeconds());
                }

                handler.postDelayed(this, PERIODIC_EVENT_TIME_MILLIS);
            }
        }
    }
}
