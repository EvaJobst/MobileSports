package at.fhooe.mos.mountaineer.model.weather;

import java.util.ArrayList;

/**
 * Created by Eva on 30.11.2017.
 */

public class Weather {
    WeatherMain main;
    ArrayList<WeatherGeneral> weather;
    Wind wind;
    Rain rain;
    String name;

    public Weather() {
    }

    public Weather(WeatherMain main, ArrayList<WeatherGeneral> weather, Wind wind, Rain rain, String name) {
        this.main = main;
        this.weather = weather;
        this.wind = wind;
        this.rain = rain;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WeatherGeneral> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherGeneral> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", weather=" + weather +
                ", wind=" + wind +
                ", rain=" + rain +
                ", name='" + name + '\'' +
                '}';
    }
}
