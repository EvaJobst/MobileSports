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

import at.fhooe.mos.app.mosproject.pedometer.StepDetector;
import at.fhooe.mos.app.mosproject.pedometer.StepEventListener;
import at.fhooe.mos.app.mosproject.ui.MainActivity;

/**
 * Created by stefan on 16.11.2017.
 */

public class StepDetectorService extends Service implements StepEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private StepDetector mStepDetector;
    private ScreenOffBroadcastReceiver mReceiver;
    private PowerManager.WakeLock wakeLock;
    private boolean aggressiveWakeLock = true;

    private NotificationManager notificationManager;
    private int notificationId = 1;
    private NotificationCompat.Builder notificationBuilder;

    private Listener listener;
    private Binder stepDetectorBinder = new LocalBinder();

    private int stepCount = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return stepDetectorBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        acquireWakeLock();

        mStepDetector = new StepDetector();
        mStepDetector.registerListener(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        registerSensorListeners();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOffBroadcastReceiver();
        registerReceiver(mReceiver, filter);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        initNotificationBuilder();
        updateNotification();

        Toast.makeText(this, "sensor service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(notificationId);

        unregisterReceiver(mReceiver);

        unregisterSensorListeners();

        releaseWakeLock();

        super.onDestroy();

        Toast.makeText(this, "sensor service stopped", Toast.LENGTH_SHORT).show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void registerListener(Listener listener) {
        this.listener = listener;

        if (listener != null) {
            listener.onNewStepCountValue(stepCount);
        }
    }

    public void removeListener() {
        this.listener = null;
    }

    private void registerSensorListeners() {
        // SENSOR_DELAY_GAME: 20,000 microsecond delay => 50 vales per second
        mSensorManager.registerListener(mStepDetector, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void unregisterSensorListeners() {
        mSensorManager.unregisterListener(mStepDetector);
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

        if (listener != null) {
            listener.onNewStepCountValue(stepCount);
        }
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

    public class LocalBinder extends Binder {
        public StepDetectorService getServiceInstance() {
            return StepDetectorService.this;
        }
    }

    public interface Listener {
        void onNewStepCountValue(int stepCount);
    }
}
