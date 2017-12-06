package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.heartrate.HeartRateSensor;
import at.fhooe.mos.mountaineer.sensors.heartrate.RealHeartRateSensor;
import at.fhooe.mos.mountaineer.sensors.location.LocationSensor;
import at.fhooe.mos.mountaineer.sensors.location.RealLocationSensor;
import at.fhooe.mos.mountaineer.sensors.stepsensor.RealStepSensor;
import at.fhooe.mos.mountaineer.sensors.stepsensor.StepSensor;
import at.fhooe.mos.mountaineer.sensors.stopwatch.RealStopwatch;
import at.fhooe.mos.mountaineer.sensors.stopwatch.Stopwatch;

/**
 * Created by stefan on 05.12.2017.
 */

public class RealSensorFactory implements SensorFactory {
    @Override
    public StepSensor getStepSensor() {
        return new RealStepSensor();
    }

    @Override
    public Stopwatch getStopwatch() {
        return new RealStopwatch();
    }

    @Override
    public LocationSensor getLocationSensor() {
        return new RealLocationSensor();
    }

    @Override
    public HeartRateSensor getHeartRateSensor() {
        return new RealHeartRateSensor();
    }
}
