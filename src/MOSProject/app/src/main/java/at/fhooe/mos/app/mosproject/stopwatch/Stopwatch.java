package at.fhooe.mos.app.mosproject.stopwatch;

import android.os.Handler;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by stefan on 21.11.2017.
 */

public class Stopwatch {
    public static final int PERIODIC_EVENT_TIME_MILLIS = 1000;

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    private Handler handler;

    public Stopwatch(){
        handler = new Handler();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    public void reset(){
        startTime = 0;
        stopTime = 0;
        running = false;
    }

    public long getElapsedSeconds() {
        return getElapsedMilliseconds() / 1000;
    }

    public static class StopwatchEvent{
        public long elapsedSeconds;

        public StopwatchEvent(long elapsedSeconds){
            this.elapsedSeconds = elapsedSeconds;
        }
    }

    private long getElapsedMilliseconds() {
        if (running) {
            return System.currentTimeMillis() - startTime;
        }
        return stopTime - startTime;
    }
}
