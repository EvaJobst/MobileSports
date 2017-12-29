package at.fhooe.mos.mountaineer.model.weather;

/**
 * Created by Eva on 29.12.2017.
 */

public class SystemInformation {
    long sunset;

    public SystemInformation(long sunset) {
        this.sunset = sunset;
    }

    public SystemInformation() {
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}
