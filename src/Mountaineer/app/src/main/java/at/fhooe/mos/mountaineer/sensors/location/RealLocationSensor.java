package at.fhooe.mos.mountaineer.sensors.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by Eva on 30.11.2017.
 */

public class RealLocationSensor extends EventSource<LocationSensorEventListener> implements LocationSensor, LocationListener {
    LocationManager locationManager;

    @Override
    @SuppressLint("MissingPermission")
    public void setup(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onLocationChanged(Location location) {
        for (LocationSensorEventListener listener : super.eventListeners) {
            listener.onLocationReceivedEvent(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
