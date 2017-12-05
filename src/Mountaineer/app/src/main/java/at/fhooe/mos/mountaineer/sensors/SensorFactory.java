package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.location.LocationSensor;
import at.fhooe.mos.mountaineer.sensors.stepsensor.StepSensor;
import at.fhooe.mos.mountaineer.sensors.stopwatch.RealStopwatch;
import at.fhooe.mos.mountaineer.sensors.stopwatch.Stopwatch;

/**
 * Created by stefan on 05.12.2017.
 */

public interface SensorFactory {
    StepSensor getStepSensor();
    Stopwatch getStopwatch();
    LocationSensor getLocationSensor();
}
