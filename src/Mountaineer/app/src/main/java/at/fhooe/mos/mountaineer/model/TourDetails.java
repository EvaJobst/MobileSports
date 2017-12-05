package at.fhooe.mos.mountaineer.model;

import java.util.HashMap;

/**
 * Created by stefan on 05.12.2017.
 */

public class TourDetails {
    //string as key because firebase does not support integer keys
    private HashMap<String, Integer> stepCountAtTime;

    public TourDetails(){
        stepCountAtTime = new HashMap<>();
    }

    public void addStepCountAtTime(int time, int stepCount){
        stepCountAtTime.put(Integer.toString(time), stepCount);
    }

    public HashMap<String, Integer> getStepCountAtTime() {
        return stepCountAtTime;
    }
}
