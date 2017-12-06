package at.fhooe.mos.mountaineer.sensors;

import at.fhooe.mos.mountaineer.sensors.heartrate.HeartRateSensor;
import at.fhooe.mos.mountaineer.sensors.heartrate.SimulatedHeartRateSensor;
import at.fhooe.mos.mountaineer.sensors.location.LocationSensor;
import at.fhooe.mos.mountaineer.sensors.location.SimulatedLocationSensor;
import at.fhooe.mos.mountaineer.sensors.stepsensor.SimulatedStepSensor;
import at.fhooe.mos.mountaineer.sensors.stepsensor.StepSensor;
import at.fhooe.mos.mountaineer.sensors.stopwatch.SimulatedStopwatch;
import at.fhooe.mos.mountaineer.sensors.stopwatch.Stopwatch;

/**
 * Created by stefan on 05.12.2017.
 */

public class SimulationSensorFactory implements SensorFactory {
    @Override
    public StepSensor getStepSensor() {
        return new SimulatedStepSensor();
    }

    @Override
    public Stopwatch getStopwatch() {
        return new SimulatedStopwatch();
    }

    @Override
    public LocationSensor getLocationSensor() {
        return new SimulatedLocationSensor();
    }

    @Override
    public HeartRateSensor getHeartRateSensor() {
        return new SimulatedHeartRateSensor();
    }
}
