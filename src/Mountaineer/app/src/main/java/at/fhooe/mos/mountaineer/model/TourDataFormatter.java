package at.fhooe.mos.mountaineer.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public String getTotalSteps(Tour tour) {
        //return String.format(Locale.ENGLISH, "%04d", tour.getTotalSteps());
        return String.valueOf(tour.getTotalSteps());
    }

    public String getDuration(Tour tour) {
        int durationSeconds = tour.getDuration();
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public String getStartTime(Tour tour) {
        Date date = new Date(tour.getStartTimestamp());
        return timeFormatter.format(date);
    }

    public String getStopTime(Tour tour) {
        Date date = new Date(tour.getStopTimestamp());
        return timeFormatter.format(date);
    }

    public String getLocation(Tour tour) {
        return "Lat: " + tour.getLocationLat() + ", Long: " + tour.getLocationLong();
    }

    public String getMinMaxTemp(Tour tour) {
        if(tour.getWeather() == null){  //TODO: weather should never be null?
            return "";
        }

        return tour.getWeather().getMain().getTemp_max() + "°C/" + tour.getWeather().getMain().getTemp_min() + "°C";
    }

    public String getHumidity(Tour tour) {
        if(tour.getWeather() == null){  //TODO: weather should never be null?
            return "Humidity: ";
        }

        return "Humidity: " + tour.getWeather().getMain().getHumidity() + "%";
    }

    public String getWind(Tour tour) {
        if(tour.getWeather() == null){  //TODO: weather should never be null?
            return "Wind: ";
        }

        return "Wind: " + tour.getWeather().getWind().getSpeed() + "km/h";
    }

    public String getTemp(Tour tour) {
        if(tour.getWeather() == null){  //TODO: weather should never be null?
            return "";
        }

        return String.valueOf(tour.getWeather().getMain().getTemp());
    }
}
