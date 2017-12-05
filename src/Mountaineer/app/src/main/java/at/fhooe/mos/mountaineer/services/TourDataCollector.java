package at.fhooe.mos.mountaineer.services;

import android.os.Handler;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.sensors.location.LocationEventListener;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerEventListener;
import at.fhooe.mos.mountaineer.sensors.stopwatch.StopwatchEventListener;
import at.fhooe.mos.mountaineer.model.Weather;
import at.fhooe.mos.mountaineer.sensors.weather.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataCollector implements
        PedometerEventListener,
        StopwatchEventListener,
        LocationEventListener,
        Callback<Weather> {

    private static final int PERIODIC_SUMMATION_TIME_MS = 60*1000;
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private final String API_KEY = "17b9da43ec1ff13f7f3b0b4ba8e21bb6";

    private Handler handler;
    private PeriodicSummation periodicSummation;

    private boolean publishTourDataUpdates;

    private Tour tour = new Tour();

    public TourDataCollector() {
        publishTourDataUpdates = true;

        handler = new Handler();
        periodicSummation = new PeriodicSummation();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStepDetectedEvent() {
        tour.setTotalSteps(tour.getTotalSteps() + 1);
        publishData();
    }

    @Override
    public void onElapsedSecondsEvent(int elapsedSeconds) {
        tour.setDuration(elapsedSeconds);
        publishData();
    }

    @Override
    public void onStartEvent(long timestamp) {
        tour.setStartTimestamp(timestamp);
    }

    @Override
    public void onStopEvent(long timestamp) {
        tour.setStopTimestamp(timestamp);
    }

    public Tour getTour() {
        return tour;
    }

    @Subscribe
    public void onMessageEvent(ControlEvent event) {
        publishTourDataUpdates = event.publishTourDataUpdates;
    }

    public void start(){
        startPeriodicSummation();
    }

    public void stop(){
        stopPeriodicSummation();
    }

    public void publishFinalTourData() {
        EventBus.getDefault().removeStickyEvent(FinalTourDataEvent.class);

        EventBus.getDefault().postSticky(new FinalTourDataEvent(tour));
    }

    private void publishData() {
        if (publishTourDataUpdates) {
            EventBus.getDefault().post(new TourDataUpdateEvent(tour));
        }
    }

    @Override
    public void onLocationReceivedEvent(double latitude, double longitude) {
        // TODO Fetch weather information

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> data = weatherService.fetch(String.valueOf(latitude), String.valueOf(longitude), "metric", API_KEY);
        data.enqueue(this);

        tour.setLocationLat(latitude);
        tour.setLocationLong(longitude);

        publishData();
    }

    @Override
    public void onResponse(Call<Weather> call, Response<Weather> response) {
        tour.setWeather(response.body());
        Log.d("RESPONDED", response.body().toString());
        publishData();
    }

    @Override
    public void onFailure(Call<Weather> call, Throwable t) {
        Log.e("FAILURE", t.getMessage());
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
        int lastTotalSteps = 0;

        @Override
        public void run() {

            int totalSteps = tour.getTotalSteps();

            int stepsInLastPeriod = totalSteps - lastTotalSteps;

            tour.getStepsPerPeriod().add(stepsInLastPeriod);

            lastTotalSteps = totalSteps;

            handler.postDelayed(this, PERIODIC_SUMMATION_TIME_MS);
        }
    }
}
