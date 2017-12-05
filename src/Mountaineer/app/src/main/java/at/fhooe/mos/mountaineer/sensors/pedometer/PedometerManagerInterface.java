package at.fhooe.mos.mountaineer.sensors.pedometer;

import android.content.Context;

import at.fhooe.mos.mountaineer.EventSourceInterface;

/**
 * Created by stefan on 05.12.2017.
 */

public interface PedometerManagerInterface extends EventSourceInterface<PedometerEventListener> {
    void setup(Context context);
    void destroy();
}
