package at.fhooe.mos.mountaineer.sensors.heartrate;

import android.content.Context;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 06.12.2017.
 */

public class RealHeartRateSensor extends EventSource<HeartRateSensorEventListener> implements HeartRateSensor {
    @Override
    public void setup(Context context) {

    }

    @Override
    public void destroy() {

    }
}
