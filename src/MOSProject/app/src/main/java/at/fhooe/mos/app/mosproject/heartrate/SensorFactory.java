package at.fhooe.mos.app.mosproject.heartrate;

/**
 * Created by Eva on 29.11.2017.
 */

public interface SensorFactory {
    HRM createHRM();
    Trimp createTrimp();
}
