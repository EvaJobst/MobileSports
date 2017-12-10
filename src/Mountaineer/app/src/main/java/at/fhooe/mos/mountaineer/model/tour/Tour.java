package at.fhooe.mos.mountaineer.model.tour;

import at.fhooe.mos.mountaineer.model.weather.Weather;

/**
 * Created by Eva on 20.11.2017.
 */

public class Tour {
    // General
    private String name;
    private LocationPoint startLocation;
    private long startTimestamp;
    private long stopTimestamp;
    private long duration;
    private String imagePath;

    // Distance
    private int totalSteps;
    private int averageSteps;
    private int distance;
    private int elevation;

    // Health
    private int averageSpeed;
    private double currentHeartRate;
    private String normalHeartRate;
    private int averageRespiration;
    private int burnedKcal;

    // Weather
    private Weather weather;

    private TourDetails tourDetails;

    public Tour() {
        tourDetails = new TourDetails();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationPoint getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LocationPoint startLocation) {
        this.startLocation = startLocation;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getStartTimestampMillis() {
        return startTimestamp * 1000L;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getStopTimestamp() {
        return stopTimestamp;
    }

    public long getStopTimestampMillis() {
        return stopTimestamp * 1000L;
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

    public double getCurrentHeartRate() {
        return currentHeartRate;
    }

    public void setCurrentHeartRate(double currentHeartRate) {
        this.currentHeartRate = currentHeartRate;
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
                ", startLocation=" + startLocation +
                ", startTimestamp=" + startTimestamp +
                ", stopTimestamp=" + stopTimestamp +
                ", duration=" + duration +
                ", totalSteps=" + totalSteps +
                ", averageSteps=" + averageSteps +
                ", distance=" + distance +
                ", elevation=" + elevation +
                ", averageSpeed=" + averageSpeed +
                ", currentHeartRate=" + currentHeartRate +
                ", normalHeartRate='" + normalHeartRate + '\'' +
                ", averageRespiration=" + averageRespiration +
                ", burnedKcal=" + burnedKcal +
                ", weather=" + weather +
                ", tourDetails=" + tourDetails +
                '}';
    }

    public static Tour getEmptyTour() {
        return new Tour();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
