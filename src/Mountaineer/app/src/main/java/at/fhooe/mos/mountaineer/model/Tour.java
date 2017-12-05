package at.fhooe.mos.mountaineer.model;

import java.util.ArrayList;

/**
 * Created by Eva on 20.11.2017.
 */

public class Tour {
    // General
    private String name;
    private double locationLat;
    private double locationLong;
    private String day;
    private long startTimestamp;
    private long stopTimestamp;
    private int duration;

    // Distance
    private int totalSteps;
    private int averageSteps;
    private int distance;
    private int elevation;

    // Health
    private int averageSpeed;
    private int averageHeartRate;
    private String normalHeartRate;
    private int averageRespiration;
    private int burnedKcal;

    // Weather
    private Weather weather;
    /*private int maxTemp;
    private int minTemp;
    private int rain;
    private int humidity;
    private int wind;*/

    private TourDetails tourDetails;

    public Tour(){
        tourDetails = new TourDetails();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getStopTimestamp() {
        return stopTimestamp;
    }

    public void setStopTimestamp(long stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
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

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public TourDetails getTourDetails() {
        return tourDetails;
    }

    public void setTourDetails(TourDetails tourDetails) {
        this.tourDetails = tourDetails;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "name='" + name + '\'' +
                ", locationLat='" + locationLat + '\'' +
                ", locationLong='" + locationLong + '\'' +
                ", day='" + day + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", stopTimestamp=" + stopTimestamp +
                ", duration=" + duration +
                ", totalSteps=" + totalSteps +
                ", averageSteps=" + averageSteps +
                ", distance=" + distance +
                ", elevation=" + elevation +
                ", averageSpeed=" + averageSpeed +
                ", averageHeartRate=" + averageHeartRate +
                ", normalHeartRate='" + normalHeartRate + '\'' +
                ", averageRespiration=" + averageRespiration +
                ", burnedKcal=" + burnedKcal +
                ", weather=" + weather +
                '}';
    }

    /*public int getMaxTemp() {
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
    }*/


}
