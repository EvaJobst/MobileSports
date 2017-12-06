package at.fhooe.mos.mountaineer.model.weather;

/**
 * Created by Eva on 30.11.2017.
 */

public class Wind {
    double speed;
    double deg;

    public Wind(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public Wind() {
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }
}
