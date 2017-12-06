package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Eva on 04.12.2017.
 */

public class BLEService extends Service {
    private final static String TAG = BLEService.class.getSimpleName();
    public static BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
