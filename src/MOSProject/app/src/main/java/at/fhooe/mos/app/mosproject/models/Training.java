package at.fhooe.mos.app.mosproject.models;

/**
 * Created by Eva on 17.11.2017.
 */

public class Training {

    // General
    private int steps;
    private String duration;
    private String date;
    private int distance;
    private int elevation;
    private int speed;

    // Health
    private int heartRate;
    private int calories;
    private int respiration;

    public Training(int steps, String duration, String date, int distance, int elevation, int speed, int heartRate, int calories, int respiration) {
        this.steps = steps;
        this.duration = duration;
        this.date = date;
        this.distance = distance;
        this.elevation = elevation;
        this.speed = speed;
        this.heartRate = heartRate;
        this.calories = calories;
        this.respiration = respiration;
    }

    public Training() {
    }

    @Override
    public String toString() {
        return "Training{" +
                "steps=" + steps +
                ", duration='" + duration + '\'' +
                ", date='" + date + '\'' +
                ", distance=" + distance +
                ", elevation=" + elevation +
                ", speed=" + speed +
                ", heartRate=" + heartRate +
                ", calories=" + calories +
                ", respiration=" + respiration +
                '}';
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getRespiration() {
        return respiration;
    }

    public void setRespiration(int respiration) {
        this.respiration = respiration;
    }
}
