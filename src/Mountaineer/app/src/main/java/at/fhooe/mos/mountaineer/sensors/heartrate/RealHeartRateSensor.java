package at.fhooe.mos.mountaineer.sensors.heartrate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 06.12.2017.
 */

public class RealHeartRateSensor extends EventSource<HeartRateSensorEventListener> implements HeartRateSensor {
    private final static String MAC = "E6:5D:9E:F6:06:93";
    private final static String HEART_RATE_CHARACTERISTIC = "00002a37-0000-1000-8000-00805f9b34fb";

    private Context context;
    private Handler handler;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGattConnection;
    private boolean isScanning;


    @Override
    public void setup(Context context) {
        this.context = context;
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "BLE is not supported.", Toast.LENGTH_SHORT).show();
            return;
        }

        handler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(context, "BLE is not enabled.", Toast.LENGTH_SHORT).show();
        } else {
            bluetoothAdapter.startLeScan(leScanCallback);
            isScanning = true;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isScanning){
                        Toast.makeText(RealHeartRateSensor.this.context, "No BLE device found!", Toast.LENGTH_SHORT).show();
                        bluetoothAdapter.stopLeScan(leScanCallback);
                        isScanning = false;
                    }
                }
            }, 10000);
        }
    }

    @Override
    public void destroy() {
        if(bluetoothAdapter != null && isScanning){
            bluetoothAdapter.stopLeScan(leScanCallback);
            isScanning = false;
        }

        if (bluetoothGattConnection != null) {
            bluetoothGattConnection.disconnect();
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (bluetoothDevice.getAddress().equals(MAC)) {
                bluetoothAdapter.stopLeScan(this);
                isScanning = false;

                if(bluetoothGattConnection == null){  //if no connection exists yet
                    bluetoothDevice.connectGatt(context, true, bluetoothGattCallback);
                }
            }
        }
    };

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGattConnection = gatt;
                bluetoothGattConnection.discoverServices();
            } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                Log.d("STATE", "");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                Log.d("STATE", "");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("STATE", "");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d("BLE SERVICE DISCOVERED", gatt.toString() + ", status: " + status);
            Toast.makeText(context, "BLE-Device Discovered", Toast.LENGTH_SHORT).show();
            setHeartRateCharacteristic(gatt.getServices());
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            int heartRate = characteristic.getValue()[1];

            for (HeartRateSensorEventListener listener : getEventListeners()) {
                listener.onHeatRateEvent(heartRate);
            }
        }
    };

    private void setHeartRateCharacteristic(List<BluetoothGattService> services) {
        for (BluetoothGattService service : services) {
            for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                if (characteristic.getUuid().toString().equals(HEART_RATE_CHARACTERISTIC)) {
                    bluetoothGattConnection.setCharacteristicNotification(characteristic, true);

                    BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(0);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bluetoothGattConnection.writeDescriptor(descriptor);
                }
            }
        }
    }
}
