package at.fhooe.mos.app.mosproject.heartrate;

/**
 * Created by Eva on 04.12.2017.
 */

public class DeviceFactory implements SensorFactory {

    @Override
    public Trimp createTrimp() {
        return null;
    }

    @Override
    public HRM createHRM() {
        return new DeviceHRM();
    }
}
