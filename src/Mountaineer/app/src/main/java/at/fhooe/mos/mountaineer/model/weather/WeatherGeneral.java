package at.fhooe.mos.mountaineer.model.weather;

/**
 * Created by Eva on 09.12.2017.
 */

public class WeatherGeneral {
    String description;

    public WeatherGeneral(String description) {
        this.description = description;
    }

    public WeatherGeneral() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WeatherGeneral{" +
                "description='" + description + '\'' +
                '}';
    }
}
