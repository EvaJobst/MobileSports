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
    SystemInformation sys;

    public Weather() {
    }

    public Weather(WeatherMain main, ArrayList<WeatherGeneral> weather, Wind wind, Rain rain, String name, SystemInformation sys) {
        this.main = main;
        this.weather = weather;
        this.wind = wind;
        this.rain = rain;
        this.name = name;
        this.sys = sys;
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

    public SystemInformation getSys() {
        return sys;
    }

    public void setSys(SystemInformation sys) {
        this.sys = sys;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", weather=" + weather +
                ", wind=" + wind +
                ", rain=" + rain +
                ", name='" + name + '\'' +
                ", sys=" + sys +
                '}';
    }
}
