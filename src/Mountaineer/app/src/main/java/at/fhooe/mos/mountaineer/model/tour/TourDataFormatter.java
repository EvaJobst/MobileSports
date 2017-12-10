package at.fhooe.mos.mountaineer.model.tour;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.fhooe.mos.mountaineer.R;

/**
 * Created by stefan on 25.11.2017.
 */

public class TourDataFormatter {
    private static DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
    private static DateFormat dateFormatter = new SimpleDateFormat("MM:DD");
    private static DecimalFormat decimalFormatter = new DecimalFormat("0");
    private static DecimalFormat shortDecimalFormatter = new DecimalFormat("0.0");
    private static DecimalFormat longDecimalFormatter = new DecimalFormat("0.000");

    private static TourDataFormatter instance = null;

    public static synchronized TourDataFormatter getInstance() {
        if (instance == null) {
            instance = new TourDataFormatter();
        }

        return instance;
    }

    public String getTotalSteps(Tour tour) {
        //return String.format(Locale.ENGLISH, "%04d", tour.getTotalSteps());
        return String.valueOf(tour.getTotalSteps());
    }

    public String getDuration(Tour tour) {
        long durationSeconds = tour.getDuration();
        int minutes = (int) durationSeconds / 60;
        int seconds = (int) durationSeconds % 60;
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
    }

    public String getName(Tour tour) {
        if(tour.getName() == null) {
            return "My Tour";
        }

        return tour.getName();
    }

    public Bitmap getImage(Tour tour, Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        if(tour.getImagePath() == null) {
            return BitmapFactory.decodeResource(activity.getResources(), R.drawable.stockimage);
        }

        return BitmapFactory.decodeFile(tour.getImagePath(), options);
    }

    public String getStartTime(Tour tour) {
        Date date = new Date(tour.getStartTimestampMillis());
        return timeFormatter.format(date);
    }

    public String getStopTime(Tour tour) {
        Date date = new Date(tour.getStopTimestampMillis());
        return timeFormatter.format(date);
    }

    public String getCurrentHeartRate(Tour tour) {
        if(tour.getCurrentHeartRate() == 0) {
            return "--";
        }

        return decimalFormatter.format(tour.getCurrentHeartRate());
    }

    public String getDescription(Tour tour) {
        if(tour.getWeather() == null) {
            return "Description";
        }

        return tour.getWeather().getWeather().get(0).getDescription();
    }

    public String getRain(Tour tour) {
        if(tour.getWeather() == null || tour.getWeather().getRain() == null) {
            return "-- %";
        }

        return decimalFormatter.format(tour.getWeather().getRain());
    }

    public String getLocation(Tour tour) {
        /*if(tour.getStartLocation() == null){
            return "Lat: -- , Long: --";
        }

        return "Lat: " + longDecimalFormatter.format(tour.getStartLocation().getLatitude()) +
                ", Long: " + longDecimalFormatter.format(tour.getStartLocation().getLongitude());*/

        if(tour.getWeather() == null) {
            return "Location";
        }

        return tour.getWeather().getName();
    }

    public String getMinMaxTemp(Tour tour) {
        if (tour.getWeather() == null) {  //TODO: weather should never be null?
            return "--°C / --°C";
        }

        return shortDecimalFormatter.format(tour.getWeather().getMain().getTemp_max()) + "°C / "
                + shortDecimalFormatter.format(tour.getWeather().getMain().getTemp_min()) + "°C";
    }

    public String getHumidity(Tour tour) {
        if (tour.getWeather() == null) {
            return "-- %";
        }

        return decimalFormatter.format(tour.getWeather().getMain().getHumidity()) + " %";
    }

    public String getWind(Tour tour) {
        if (tour.getWeather() == null) {
            return "-- km/h";
        }

        return shortDecimalFormatter.format(tour.getWeather().getWind().getSpeed()) + " km/h";
    }

    public String getTemp(Tour tour) {
        if (tour.getWeather() == null) {
            return "--°";
        }

        return shortDecimalFormatter.format(tour.getWeather().getMain().getTemp()) + "°";
    }

    public String getSpeed(Tour tour) {
        if(tour.getAverageSpeed() == 0) {
            return "Speed: -- km/h";
        }

        return "Speed: " + tour.getAverageSpeed() + " km/h";
    }

    public String getDistance(Tour tour) {
        if(tour.getDistance() == 0) {
            return "Distance: -- m";
        }

        return "Distance: " + tour.getDistance() + " m";
    }

    public String getElevation(Tour tour) {
        if(tour.getElevation() == 0) {
            return "Elevation: -- m";
        }

        return "Elevation: " + tour.getElevation() + " m";
    }

    public String getNormalHeartRate(Tour tour) {
        if(tour.getNormalHeartRate() == null) {
            return "Normal: --";
        }

        return "Normal: " + tour.getNormalHeartRate();
    }

    public String getRespiration(Tour tour) {
        if(tour.getAverageRespiration() == 0) {
            return "--/min.";
        }

        return tour.getAverageRespiration() + "/min";
    }

    public String getBurnedCalories(Tour tour) {
        if(tour.getBurnedKcal() == 0) {
            return "-- kCal";
        }

        return tour.getBurnedKcal() + " kCal";
    }
}
