package at.fhooe.mos.app.mosproject.heartrate;

/**
 * Created by Eva on 29.11.2017.
 */

public class SimulationFactory implements SensorFactory {

    @Override
    public Trimp createTrimp() {
        return new SimulationTrimp();
    }

    @Override
    public HRM createHRM() {
        return new SimulationHRM();
    }
}
