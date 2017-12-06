package at.fhooe.mos.mountaineer.model.tour;

import java.util.HashMap;

/**
 * Created by stefan on 05.12.2017.
 */

public class TourDetails {
    //string as key because firebase does not support integer keys
    private HashMap<String, Integer> stepCountAtTime;
    private HashMap<String, Double> heartRateAtTime;

    public TourDetails() {
        stepCountAtTime = new HashMap<>();
        heartRateAtTime = new HashMap<>();
    }

    public void addStepCountAtTime(long time, int stepCount) {
        stepCountAtTime.put(Long.toString(time), stepCount);
    }

    public void addHeartRateAtTime(long time, double heartRate) {
        heartRateAtTime.put(Long.toString(time), heartRate);
    }

    public HashMap<String, Integer> getStepCountAtTime() {
        return stepCountAtTime;
    }

    public HashMap<String, Double> getHeartRateAtTime() {
        return heartRateAtTime;
    }
}
