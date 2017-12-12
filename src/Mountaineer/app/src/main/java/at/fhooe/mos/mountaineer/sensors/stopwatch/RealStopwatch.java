package at.fhooe.mos.mountaineer.sensors.stopwatch;

import android.content.Context;
import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;


/**
 * Created by stefan on 21.11.2017.
 */

public class RealStopwatch extends EventSource<StopwatchEventListener> implements Stopwatch {
    public static final int PERIODIC_EVENT_TIME_MS = 1000;

    private long startTimeMs = 0;
    private long stopTimeMs = 0;
    private boolean running = false;

    private Handler handler;
    private PeriodicEventSender periodicEventSender;

    @Override
    public void setup(Context context) {
        handler = new Handler();
        periodicEventSender = new PeriodicEventSender();

        start();
    }

    @Override
    public void destroy() {
        stop();
    }

    private void start() {
        startTimeMs = getUnixTime();
        running = true;

        startPeriodicUpdates();
    }

    private void stop() {
        stopPeriodicUpdates();

        stopTimeMs = getUnixTime();
        running = false;

        for (StopwatchEventListener listener : super.getEventListeners()) {
            listener.onFinalTimeEvent(startTimeMs, stopTimeMs, getElapsedSeconds());
        }
    }

    private long getElapsedSeconds() {
        if (running) {
            return getUnixTime() - startTimeMs;
        }
        return stopTimeMs - startTimeMs;
    }

    private long getUnixTime(){
        return System.currentTimeMillis() / 1000L;
    }

    private void startPeriodicUpdates() {
        handler.postDelayed(periodicEventSender, PERIODIC_EVENT_TIME_MS);
    }

    private void stopPeriodicUpdates() {
        handler.removeCallbacks(periodicEventSender);
    }

    private class PeriodicEventSender implements Runnable {
        @Override
        public void run() {

            if (running) {
                for (StopwatchEventListener listener : RealStopwatch.super.getEventListeners()) {
                    listener.onElapsedSecondsEvent(getElapsedSeconds());
                }

                handler.postDelayed(this, PERIODIC_EVENT_TIME_MS);
            }
        }
    }
}
