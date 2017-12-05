package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManager;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManagerInterface;

/**
 * Created by stefan on 05.12.2017.
 */

public class RealSensorFactory implements SensorFactory {
    @Override
    public PedometerManagerInterface getPedometerManager() {
        return new PedometerManager();
    }
}
