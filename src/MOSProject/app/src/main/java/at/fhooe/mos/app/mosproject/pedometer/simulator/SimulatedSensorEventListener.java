package at.fhooe.mos.app.mosproject.pedometer.simulator;

import at.fhooe.mos.app.mosproject.pedometer.SensorEventData;

/**
 * Created by stefan on 10.11.2017.
 */

public interface SimulatedSensorEventListener {
    void onSensorChanged(SensorEventData sensorEventData);
}
