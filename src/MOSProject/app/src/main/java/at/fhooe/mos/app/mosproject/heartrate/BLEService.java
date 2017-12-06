package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by Eva on 04.12.2017.
 */

public class BLEService extends Service {
    private final static String HEART_RATE_CHARACTERISTIC = "00002a37-0000-1000-8000-00805f9b34fb";
    public static BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;
    public static byte heartRate = 0;

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGatt.discoverServices();
            }

            else if(newState == BluetoothProfile.STATE_CONNECTING) {
                Log.d("STATE", "");
            }

            else if(newState == BluetoothProfile.STATE_DISCONNECTING) {
                Log.d("STATE", "");
            }

            else if(newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("STATE", "");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("BLE SERVICE DISCOVERED", gatt.toString() + ", status: " + status);
            setHeartRateCharacteristic(gatt.getServices());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            heartRate = characteristic.getValue()[1];
        }
    };

    public void setHeartRateCharacteristic(List<BluetoothGattService> services) {
        for(BluetoothGattService service : services) {
            for(BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                if(characteristic.getUuid().toString().equals(HEART_RATE_CHARACTERISTIC)) {
                    bluetoothGatt.setCharacteristicNotification(characteristic, true);

                    BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(0);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        bluetoothGatt.disconnect();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        bluetoothGatt = bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);
        bluetoothGatt.discoverServices();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}