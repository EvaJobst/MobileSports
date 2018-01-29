package at.fhooe.mos.mountaineer.model.tour;

import at.fhooe.mos.mountaineer.model.user.UserInformation;
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

    //user
    private UserInformation userInformation;

    // Steps / Distance
    private int totalSteps;
    private int averageSteps;
    private int stepFrequency;
    private int distanceFromSteps;
    private double speedFromSteps;
    private double energyExpenditureFromSteps;
    private int distance;
    private int elevation;

    // Health
    private int averageSpeed;
    private double currentHeartRate;
    private String normalHeartRate;
    private int averageRespiration;
    private double burnedKcal;

    // Weather
    private Weather weather;
    private TourDetails tourDetails;

    public Tour() {
        tourDetails = new TourDetails();
    }

    public static Tour getEmptyTour() {
        return new Tour();
    }

    public UserInformation getUserInformation() {
        return userInformation;
    }

    public void setUserInformation(UserInformation userInformation) {
        this.userInformation = userInformation;
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

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getStartTimestampMillis() {
        return startTimestamp * 1000L;
    }

    public long getStopTimestamp() {
        return stopTimestamp;
    }

    public void setStopTimestamp(long stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    public long getStopTimestampMillis() {
        return stopTimestamp * 1000L;
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

    public int getStepFrequency() {
        return stepFrequency;
    }

    public void setStepFrequency(int stepFrequency) {
        this.stepFrequency = stepFrequency;
    }

    public int getDistanceFromSteps() {
        return distanceFromSteps;
    }

    public void setDistanceFromSteps(int distanceFromSteps) {
        this.distanceFromSteps = distanceFromSteps;
    }

    public double getSpeedFromSteps() {
        return speedFromSteps;
    }

    public void setSpeedFromSteps(double speedFromSteps) {
        this.speedFromSteps = speedFromSteps;
    }

    public double getEnergyExpenditureFromSteps() {
        return energyExpenditureFromSteps;
    }

    public void setEnergyExpenditureFromSteps(double energyExpenditureFromSteps) {
        this.energyExpenditureFromSteps = energyExpenditureFromSteps;
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

    public double getBurnedKcal() {
        return burnedKcal;
    }

    public void setBurnedKcal(double burnedKcal) {
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "name='" + name + '\'' +
                ", startLocation=" + startLocation +
                ", startTimestamp=" + startTimestamp +
                ", stopTimestamp=" + stopTimestamp +
                ", duration=" + duration +
                ", imagePath='" + imagePath + '\'' +
                ", userInformation=" + userInformation +
                ", totalSteps=" + totalSteps +
                ", averageSteps=" + averageSteps +
                ", stepFrequency=" + stepFrequency +
                ", distanceFromSteps=" + distanceFromSteps +
                ", speedFromSteps=" + speedFromSteps +
                ", energyExpenditureFromSteps=" + energyExpenditureFromSteps +
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
}
