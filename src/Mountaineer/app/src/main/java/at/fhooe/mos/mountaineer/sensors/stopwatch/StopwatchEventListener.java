package at.fhooe.mos.mountaineer.sensors.stopwatch;

/**
 * Created by stefan on 25.11.2017.
 */

public interface StopwatchEventListener {
    void onElapsedSecondsEvent(int elapsedSeconds);

    void onStartEvent(long timestamp);

    void onStopEvent(long timestamp);
}