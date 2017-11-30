package at.fhooe.mos.mountaineer.services;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerEventListener;
import at.fhooe.mos.mountaineer.sensors.stopwatch.StopwatchEventListener;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataCollector implements PedometerEventListener, StopwatchEventListener {
    private boolean publishTourDataUpdates;

    private Tour tour = new Tour();

    public TourDataCollector() {
        publishTourDataUpdates = true;

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

    public void publishFinalTourData() {
        EventBus.getDefault().removeStickyEvent(FinalTourDataEvent.class);

        EventBus.getDefault().postSticky(new FinalTourDataEvent(tour));
    }

    private void publishData() {
        if (publishTourDataUpdates) {
            EventBus.getDefault().post(new TourDataUpdateEvent(tour));
        }
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
}
