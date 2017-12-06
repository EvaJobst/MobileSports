package at.fhooe.mos.mountaineer.sensors;

import android.content.Context;

import at.fhooe.mos.mountaineer.EventSourceInterface;

/**
 * Created by stefan on 05.12.2017.
 */

public interface Sensor<T> extends EventSourceInterface<T> {
    void setup(Context context);

    void destroy();
}
