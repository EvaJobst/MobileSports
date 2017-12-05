package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManagerInterface;

/**
 * Created by stefan on 05.12.2017.
 */

public interface SensorFactory {
    PedometerManagerInterface getPedometerManager();
}
