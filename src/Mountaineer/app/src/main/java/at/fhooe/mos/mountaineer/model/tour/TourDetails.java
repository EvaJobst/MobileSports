package at.fhooe.mos.mountaineer.model.tour;

import java.util.HashMap;

/**
 * Created by stefan on 05.12.2017.
 */

public class TourDetails {
    //string as key because firebase does not support integer keys
    private HashMap<String, Integer> stepCountAtTime;
    private HashMap<String, Double> heartRateAtTime;
    private HashMap<String, LocationPoint> locationPointAtTime;

    public TourDetails() {
        stepCountAtTime = new HashMap<>();
        heartRateAtTime = new HashMap<>();
        locationPointAtTime = new HashMap<>();
    }

    public void addStepCountAtTime(long time, int stepCount) {
        stepCountAtTime.put(Long.toString(time), stepCount);
    }

    public void addHeartRateAtTime(long time, double heartRate) {
        heartRateAtTime.put(Long.toString(time), heartRate);
    }

    public void addLocationPointAtTime(long time, LocationPoint locationPoint) {
        locationPointAtTime.put(Long.toString(time), locationPoint);
    }

    public HashMap<String, Integer> getStepCountAtTime() {
        return stepCountAtTime;
    }

    public HashMap<String, Double> getHeartRateAtTime() {
        return heartRateAtTime;
    }

    public HashMap<String, LocationPoint> getLocationPointAtTime() {
        return locationPointAtTime;
    }
}
