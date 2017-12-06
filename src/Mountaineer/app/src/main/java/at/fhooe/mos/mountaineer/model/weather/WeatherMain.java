package at.fhooe.mos.mountaineer.model.weather;

/**
 * Created by Eva on 30.11.2017.
 */

public class WeatherMain {
    double temp;
    double humidity;
    double temp_min;
    double temp_max;

    public WeatherMain(double temp, double humidity, double temp_min, double temp_max) {
        this.temp = temp;
        this.humidity = humidity;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
    }

    public WeatherMain() {
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    @Override
    public String toString() {
        return "WeatherMain{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                ", temp_min=" + temp_min +
                ", temp_max=" + temp_max +
                '}';
    }
}