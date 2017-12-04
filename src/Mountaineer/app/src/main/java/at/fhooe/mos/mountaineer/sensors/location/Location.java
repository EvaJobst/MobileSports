package at.fhooe.mos.mountaineer.sensors.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by Eva on 30.11.2017.
 */

public class Location extends EventSource<LocationEventListener> implements LocationListener {
    LocationManager locationManager;

    @SuppressLint("MissingPermission")
    public Location(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        for (LocationEventListener listener : super.eventListeners) {
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
