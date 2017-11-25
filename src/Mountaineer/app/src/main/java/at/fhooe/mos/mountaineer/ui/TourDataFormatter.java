package at.fhooe.mos.mountaineer.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.fhooe.mos.mountaineer.model.Tour;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataFormatter {
    private static DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private static DateFormat dateFormatter = new SimpleDateFormat("MM:DD");

    private static TourDataFormatter instance = null;

    public static synchronized TourDataFormatter getInstance() {
        if(instance == null){
            instance = new TourDataFormatter();
        }

        return instance;
    }


    public String getTotalStepsString(Tour tour) {
        return String.format(Locale.ENGLISH, "%04d", tour.getTotalSteps());
    }

    public String getDurationString(Tour tour) {
        int durationSeconds = tour.getDuration();
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public String getStartTimeString(Tour tour) {
        Date date = new Date(tour.getStartTimestamp());
        return timeFormatter.format(date);
    }

    public String getStopTimeString(Tour tour) {
        Date date = new Date(tour.getStopTimestamp());
        return timeFormatter.format(date);
    }
}
