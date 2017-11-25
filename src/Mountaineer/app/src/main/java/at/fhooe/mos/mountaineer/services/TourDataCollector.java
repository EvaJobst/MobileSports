package at.fhooe.mos.mountaineer.services;

import org.greenrobot.eventbus.EventBus;

import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerEventListener;
import at.fhooe.mos.mountaineer.sensors.stopwatch.StopwatchEventListener;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataCollector implements PedometerEventListener, StopwatchEventListener {
    private Tour tour = new Tour();

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

    private void publishData() {
        EventBus.getDefault().post(new TourDetailsEvent(tour));
    }

    public static class TourDetailsEvent {
        private Tour tour;

        protected TourDetailsEvent(Tour tour) {
            this.tour = tour;
        }

        public Tour getTour() {
            return tour;
        }
    }
}
