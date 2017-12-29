package at.fhooe.mos.mountaineer.model.tour;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;

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
    private static DateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
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

    public String getDate(Tour tour) {
        Date date = new Date(tour.getStartTimestampMillis());
        return dateFormatter.format(date);
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

    public String getName(Tour tour, String name) {
        if(tour.getName() == null && name == null) {
            return "My Tour";
        }

        else if(tour.getName() != null) {
            return tour.getName();
        }

        return name;
    }

    public Bitmap getImage(Tour tour, String path, Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;

        if(tour.getImagePath() == null && path == null) {
            return BitmapFactory.decodeResource(activity.getResources(), R.drawable.stockimage, options);
        }

        else if(tour.getImagePath() != null) {
            return BitmapFactory.decodeFile(tour.getImagePath(), options);
        }

        return BitmapFactory.decodeFile(path, options);
    }

    public int getWeatherIcon(Tour tour) {
        int drawable;

        if(tour.getWeather() != null) {
            switch (tour.getWeather().getWeather().get(0).getDescription()) {
                case "clear sky":
                    drawable = R.drawable.ic_weather_sun; break;
                case "few clouds":
                    drawable = R.drawable.ic_weather_partly_cloudy; break;
                case "scattered clouds":
                    drawable = R.drawable.ic_weather_cloud; break;
                case "broken clouds":
                    drawable = R.drawable.ic_weather_cloud; break;
                case "shower rain":
                    drawable = R.drawable.ic_weather_rain; break;
                case "rain":
                    drawable = R.drawable.ic_weather_rain; break;
                case "thunderstorm":
                    drawable = R.drawable.ic_weather_thunderstorm; break;
                case "snow":
                    drawable = R.drawable.ic_weather_snow; break;
                case "mist":
                    drawable = R.drawable.ic_weather_fog; break;
                default:
                    drawable = R.drawable.ic_weather_sun;
            }
        }

        else {
            drawable = R.drawable.ic_weather_sun;
        }

        return drawable;
    }

    public int getWeatherIconShadow(Tour tour, Activity activity) {
        int drawable;

        if(tour.getWeather() != null) {
            switch (tour.getWeather().getWeather().get(0).getDescription()) {
                case "clear sky":
                    drawable = R.drawable.ic_weather_sun_shadow; break;
                case "few clouds":
                    drawable = R.drawable.ic_weather_partly_cloudy_shadow; break;
                case "scattered clouds":
                    drawable = R.drawable.ic_weather_cloud_shadow; break;
                case "broken clouds":
                    drawable = R.drawable.ic_weather_cloud_shadow; break;
                case "shower rain":
                    drawable = R.drawable.ic_weather_rain_shadow; break;
                case "rain":
                    drawable = R.drawable.ic_weather_rain_shadow; break;
                case "thunderstorm":
                    drawable = R.drawable.ic_weather_thunderstorm_shadow; break;
                case "snow":
                    drawable = R.drawable.ic_weather_snow_shadow; break;
                case "mist":
                    drawable = R.drawable.ic_weather_fog_shadow; break;
                default:
                    drawable = R.drawable.ic_weather_sun_shadow;
            }
        }

        else {
            drawable = R.drawable.ic_weather_sun_shadow;
        }

        return drawable;
    }

    public String getPreviewDetails(Tour tour) {
        String duration = getDuration(tour);
        float km = tour.getDistance() / 1000;
        String distance = shortDecimalFormatter.format(km);
        return distance + " km" + ", " + duration + " h";
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
            return "0 mm";
        }

        return decimalFormatter.format(tour.getWeather().getRain()) + " mm";
    }

    public String getLocation(Tour tour) {
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

    /*public String getHumidity(Tour tour) {
        if (tour.getWeather() == null) {
            return "-- %";
        }

        return decimalFormatter.format(tour.getWeather().getMain().getHumidity()) + " %";
    }*/

    public String getWind(Tour tour) {
        if (tour.getWeather() == null) {
            return "-- km/h";
        }

        return decimalFormatter.format(tour.getWeather().getWind().getSpeed()) + " km/h";
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

    public String getRestingHeartRate(int heartRate) {
        if(heartRate == 0) {
            return "Resting: -- bpm";
        }

        return "Resting: " + heartRate + " bpm";
    }

    public String getRespiration(Tour tour) {
        if(tour.getAverageRespiration() == 0) {
            return "--/min.";
        }

        return tour.getAverageRespiration() + "/min";
    }

    public String getBurnedCalories(Tour tour) {
        if(tour.getBurnedKcal() == 0) {
            return "0 kCal";
        }

        return decimalFormatter.format(tour.getBurnedKcal()) + " kCal";
    }
}
