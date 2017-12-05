package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManagerInterface;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManagerSimulator;

/**
 * Created by stefan on 05.12.2017.
 */

public class SimulationSensorFactory implements SensorFactory {
    @Override
    public PedometerManagerInterface getPedometerManager() {
        return new PedometerManagerSimulator();
    }
}
