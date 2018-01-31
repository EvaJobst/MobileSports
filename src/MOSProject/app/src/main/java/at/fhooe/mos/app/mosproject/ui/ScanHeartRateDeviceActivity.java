package at.fhooe.mos.app.mosproject.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.heartrate.BLEService;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanHeartRateDeviceActivity extends AppCompatActivity {
    String MAC = "E6:5D:9E:F6:06:93";
    BluetoothAdapter bluetoothAdapter;
    Bundle extras;

    @OnClick(R.id.stopScanDevice)
    public void onStopScanDeviceClick() {
        bluetoothAdapter.stopLeScan(leScanCallback);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_heart_rate_device);
        ButterKnife.bind(this);
        extras = getIntent().getExtras();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Dude, BLE is not supported.", Toast.LENGTH_SHORT).show();
            finish();
        }

        else {
            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }

            else {
                bluetoothAdapter.startLeScan(leScanCallback);
            }
        }
    }

    public BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if(bluetoothDevice.getAddress().equals(MAC)) {
                Toast.makeText(ScanHeartRateDeviceActivity.this, "BLE-Device found", Toast.LENGTH_SHORT).show();
                bluetoothAdapter.stopLeScan(this);
                Intent intent = new Intent(ScanHeartRateDeviceActivity.this, HeartRateDeviceActivity.class);
                intent.putExtras(extras);
                BLEService.bluetoothDevice = bluetoothDevice;
                startActivity(intent);
            }
        }
    };
}
