package at.fhooe.mos.app.mosproject.heartrate;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Eva on 04.12.2017.
 */

public class DeviceHRM implements HRM {
    @Override
    public int getHeartRate() {
        return 0;
    }

    public void setDevice(BluetoothDevice bluetoothDevice) {

    }
}
