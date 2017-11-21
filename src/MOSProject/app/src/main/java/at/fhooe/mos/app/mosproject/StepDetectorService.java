package at.fhooe.mos.app.mosproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import at.fhooe.mos.app.mosproject.pedometer.StepDetector;
import at.fhooe.mos.app.mosproject.pedometer.StepEventListener;
import at.fhooe.mos.app.mosproject.ui.MainActivity;

/**
 * Created by stefan on 16.11.2017.
 */

public class StepDetectorService extends Service implements StepEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private StepDetector stepDetector;
    private ScreenOffBroadcastReceiver screenOffBroadcastReceiver;
    private PowerManager.WakeLock wakeLock;
    private boolean aggressiveWakeLock = true;

    private NotificationManager notificationManager;
    private int notificationId = 1;
    private NotificationCompat.Builder notificationBuilder;

    private int stepCount = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        acquireWakeLock();

        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensorListeners();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        screenOffBroadcastReceiver = new ScreenOffBroadcastReceiver();
        registerReceiver(screenOffBroadcastReceiver, filter);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        initNotificationBuilder();
        updateNotification();

        Toast.makeText(this, "accelerometerSensor service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(notificationId);

        unregisterReceiver(screenOffBroadcastReceiver);

        unregisterSensorListeners();

        releaseWakeLock();

        super.onDestroy();

        Toast.makeText(this, "accelerometerSensor service stopped", Toast.LENGTH_SHORT).show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void registerSensorListeners() {
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        sensorManager.registerListener(stepDetector, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void unregisterSensorListeners() {
        sensorManager.unregisterListener(stepDetector);
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        if (aggressiveWakeLock) {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        } else {
            wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        }
        wakeLock = pm.newWakeLock(wakeFlags, "StepDetectorService");
        wakeLock.acquire();
    }

    private void releaseWakeLock() {
        wakeLock.release();
    }

    private void initNotificationBuilder() {
        notificationBuilder = new NotificationCompat.Builder(this, "channelID");

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("StepDetector")
                .setContentIntent(resultPendingIntent);
    }

    private void updateNotification() {
        notificationBuilder.setContentText("Steps: " + stepCount);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    @Override
    public void onStepEvent() {
        stepCount++;

        if(stepCount % 5 == 0){
            updateNotification();
        }

        EventBus.getDefault().post(new StepCountEvent(stepCount));
    }

    private class ScreenOffBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                StepDetectorService.this.unregisterSensorListeners();
                StepDetectorService.this.registerSensorListeners();

                if (aggressiveWakeLock) {
                    wakeLock.release();
                    acquireWakeLock();
                }
            }
        }
    }

    public static class StepCountEvent {
        public int stepCount;

        public StepCountEvent(int stepCount){
            this.stepCount = stepCount;
        }
    }
}
