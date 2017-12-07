package at.fhooe.mos.mountaineer.model.tour;

/**
 * Created by stefan on 07.12.2017.
 */

public class LocationPoint {
    private double latitude;
    private double longitude;
    private double altitude;

    public LocationPoint(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    @Override
    public String toString() {
        return "LocationPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}
