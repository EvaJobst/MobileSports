package at.fhooe.mos.mountaineer.sensors.pedometer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by stefan on 25.11.2017.
 */

public class PedometerManager {
    private Context context;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Pedometer pedometer;
    private PedometerManager.ScreenOffBroadcastReceiver screenOffBroadcastReceiver;

    public PedometerManager(Context context) {
        this.context = context;
    }

    public void setup(PedometerEventListener pedometerEventListener) {
        pedometer = new Pedometer();
        pedometer.registerListener(pedometerEventListener);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensorListeners();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        screenOffBroadcastReceiver = new PedometerManager.ScreenOffBroadcastReceiver();
        context.registerReceiver(screenOffBroadcastReceiver, filter);
    }

    public void destroy() {
        context.unregisterReceiver(screenOffBroadcastReceiver);

        unregisterSensorListeners();
    }

    private void registerSensorListeners() {
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        sensorManager.registerListener(pedometer, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void unregisterSensorListeners() {
        sensorManager.unregisterListener(pedometer);
    }

    private class ScreenOffBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                PedometerManager.this.unregisterSensorListeners();
                PedometerManager.this.registerSensorListeners();
            }
        }
    }
}
