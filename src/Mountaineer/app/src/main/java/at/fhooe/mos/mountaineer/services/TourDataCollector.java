package at.fhooe.mos.mountaineer.services;

import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.ImageChangedEventListener;
import at.fhooe.mos.mountaineer.NameChangedEventListener;
import at.fhooe.mos.mountaineer.model.tour.LocationPoint;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.model.weather.Weather;
import at.fhooe.mos.mountaineer.sensors.heartrate.HeartRateSensorEventListener;
import at.fhooe.mos.mountaineer.sensors.location.LocationSensorEventListener;
import at.fhooe.mos.mountaineer.sensors.stepsensor.StepSensorEventListener;
import at.fhooe.mos.mountaineer.sensors.stopwatch.StopwatchEventListener;
import at.fhooe.mos.mountaineer.webservices.OpenWeatherMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataCollector implements
        StepSensorEventListener,
        StopwatchEventListener,
        LocationSensorEventListener,
        NameChangedEventListener,
        ImageChangedEventListener,
        HeartRateSensorEventListener {

    private static final int PERIODIC_SUMMATION_TIME_MS = 60 * 1000;

    private Handler handler;
    private PeriodicSummation periodicSummation;

    private boolean publishTourDataUpdates;

    private Tour tour = new Tour();

    private double heartRateSum = 0;
    private int heartRateSumCount = 0;
    private int stepCountSum = 0;

    private boolean weatherFetched = false;

    public TourDataCollector() {
        publishTourDataUpdates = true;

        handler = new Handler();
        periodicSummation = new PeriodicSummation();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStepDetectedEvent() {
        tour.setTotalSteps(tour.getTotalSteps() + 1);
        stepCountSum++;

        publishData();
    }

    @Override
    public void onElapsedSecondsEvent(long elapsedSeconds) {
        tour.setDuration(elapsedSeconds);
        publishData();
    }

    @Override
    public void onFinalTimeEvent(long startTimestamp, long stopTimestamp, long elapsedSeconds) {
        tour.setStartTimestamp(startTimestamp);
        tour.setStopTimestamp(stopTimestamp);
        tour.setDuration(elapsedSeconds);
    }

    @Override
    public void onLocationReceivedEvent(double latitude, double longitude, double altitude) {
        if(tour.getStartLocation() == null)
        {
            tour.setStartLocation(new LocationPoint(latitude, longitude, altitude));
        }

        tour.getTourDetails().addLocationPointAtTime(tour.getDuration(), new LocationPoint(latitude, longitude, altitude));

        publishData();

        if(weatherFetched == false){
            OpenWeatherMap.fetchWeather(latitude, longitude, new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {
                    tour.setWeather(response.body());
                    publishData();
                    weatherFetched = true;
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    Log.e("TourDataCollector", "Could not fetch weather data!\n" + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onHeatRateEvent(double heartRate) {
        heartRateSum += heartRate;
        heartRateSumCount++;

        tour.setCurrentHeartRate(heartRate);
    }

    @Subscribe
    public void onMessageEvent(ControlEvent event) {
        publishTourDataUpdates = event.getPublishTourDataUpdates();
    }

    public void start() {
        startPeriodicSummation();
    }

    public void stop() {
        stopPeriodicSummation();
    }

    public void publishFinalTourData() {
        EventBus.getDefault().removeStickyEvent(FinalTourDataEvent.class);
        EventBus.getDefault().postSticky(new FinalTourDataEvent(tour));
    }

    public Tour getTour() {
        return tour;
    }

    private void publishData() {
        if (publishTourDataUpdates) {
            EventBus.getDefault().post(new TourDataUpdateEvent(tour));
        }
    }

    @Override
    public void onNameChangedEvent(String name) {
        tour.setName(name);
        publishData();
    }

    @Override
    public void onImageChangedEvent(String path) {
        tour.setImagePath(path);
        publishData();
    }

    public static class TourDataUpdateEvent {
        private Tour tour;

        protected TourDataUpdateEvent(Tour tour) {
            this.tour = tour;
        }

        public Tour getTour() {
            return tour;
        }
    }

    public static class FinalTourDataEvent {
        private Tour tour;

        protected FinalTourDataEvent(Tour tour) {
            this.tour = tour;
        }

        public Tour getTour() {
            return tour;
        }
    }

    public static class ControlEvent {
        private boolean publishTourDataUpdates;

        public ControlEvent(Boolean publishTourDataUpdates) {
            this.publishTourDataUpdates = publishTourDataUpdates;
        }

        public boolean getPublishTourDataUpdates() {
            return publishTourDataUpdates;
        }
    }

    private void startPeriodicSummation() {
        handler.postDelayed(periodicSummation, PERIODIC_SUMMATION_TIME_MS);
    }

    private void stopPeriodicSummation() {
        handler.removeCallbacks(periodicSummation);
    }

    private class PeriodicSummation implements Runnable {
        @Override
        public void run() {
            tour.getTourDetails().addStepCountAtTime(tour.getDuration(), stepCountSum);
            tour.setAverageSteps(stepCountSum);
            stepCountSum = 0;

            double averageHeartRateInPeriod = heartRateSum / heartRateSumCount;
            averageHeartRateInPeriod = Math.floor(averageHeartRateInPeriod * 100) / 100;

            tour.getTourDetails().addHeartRateAtTime(tour.getDuration(), averageHeartRateInPeriod);
            heartRateSum = 0;
            heartRateSumCount = 0;

            handler.postDelayed(this, PERIODIC_SUMMATION_TIME_MS);
        }
    }
}
