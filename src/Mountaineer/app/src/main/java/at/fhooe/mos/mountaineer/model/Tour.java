package at.fhooe.mos.mountaineer.model;

/**
 * Created by Eva on 20.11.2017.
 */

public class Tour {
    // General
    String name;
    String location;
    String day;
    String duration;

    // Distance
    int totalSteps;
    int averageSteps;
    int distance;
    int elevation;

    // Health
    int averageSpeed;
    int averageHeartRate;
    String normalHeartRate;
    int averageRespiration;
    int burnedKcal;

    // Weather
    int maxTemp;
    int minTemp;
    int rain;
    int humidity;
    int wind;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getAverageSteps() {
        return averageSteps;
    }

    public void setAverageSteps(int averageSteps) {
        this.averageSteps = averageSteps;
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

    public int getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(int averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public String getNormalHeartRate() {
        return normalHeartRate;
    }

    public void setNormalHeartRate(String normalHeartRate) {
        this.normalHeartRate = normalHeartRate;
    }

    public int getAverageRespiration() {
        return averageRespiration;
    }

    public void setAverageRespiration(int averageRespiration) {
        this.averageRespiration = averageRespiration;
    }

    public int getBurnedKcal() {
        return burnedKcal;
    }

    public void setBurnedKcal(int burnedKcal) {
        this.burnedKcal = burnedKcal;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", day='" + day + '\'' +
                ", duration='" + duration + '\'' +
                ", totalSteps=" + totalSteps +
                ", averageSteps=" + averageSteps +
                ", distance=" + distance +
                ", elevation=" + elevation +
                ", averageSpeed=" + averageSpeed +
                ", averageHeartRate=" + averageHeartRate +
                ", normalHeartRate='" + normalHeartRate + '\'' +
                ", averageRespiration=" + averageRespiration +
                ", burnedKcal=" + burnedKcal +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", rain=" + rain +
                ", humidity=" + humidity +
                ", wind=" + wind +
                '}';
    }
}
