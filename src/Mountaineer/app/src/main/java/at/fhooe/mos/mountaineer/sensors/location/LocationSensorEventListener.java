package at.fhooe.mos.mountaineer.sensors.location;

/**
 * Created by Eva on 30.11.2017.
 */

public interface LocationSensorEventListener {
    void onLocationReceivedEvent(double latitude, double longitude);
}
