package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Eva on 04.12.2017.
 */

public class DeviceHRM implements HRM {
    @Override
    public int getHeartRate() {
        return BLEService.heartRate;
    }

    @Override
    public void initialize(Activity activity) {
        Intent intent = new Intent(activity, BLEService.class);
        activity.startService(intent);
    }
}
