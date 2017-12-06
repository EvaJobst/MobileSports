package at.fhooe.mos.mountaineer.model.weather;

/**
 * Created by Eva on 30.11.2017.
 */

public class Weather {
    WeatherMain main;
    Wind wind;
    Rain rain;

    public Weather(WeatherMain main, Wind wind, Rain rain) {
        this.main = main;
        this.wind = wind;
        this.rain = rain;
    }

    public Weather() {
    }

    public WeatherMain getMain() {
        return main;
    }

    public void setMain(WeatherMain main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", wind=" + wind +
                ", rain=" + rain +
                '}';
    }
}
