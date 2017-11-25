package at.fhooe.mos.mountaineer.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;

import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerSensor;
import at.fhooe.mos.mountaineer.ui.Notifications;

/**
 * Created by stefan on 25.11.2017.
 */

@EService
public class TourRecorderService extends Service {
    public static final String TAG = TourRecorderService.class.getSimpleName();

    private PowerManager.WakeLock wakeLock;
    private Handler handler;
    private PeriodicNotificationUpdater periodicNotificationUpdater;

    private PedometerSensor pedometerSensor;

    @SystemService
    public NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        periodicNotificationUpdater = new PeriodicNotificationUpdater();

        pedometerSensor = new PedometerSensor(this);

        acquireWakeLock();
        startForeground(Notifications.getNotificationId(), Notifications.getNotification(this));

        pedometerSensor.setup();

        startNotificationUpdates();
        Toast.makeText(this, "accelerometerSensor service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        stopNotificationUpdates();

        stopForeground(true);

        pedometerSensor.destroy();

        releaseWakeLock();

        super.onDestroy();

        Toast.makeText(this, "accelerometerSensor service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wakeLock.acquire();
    }

    private void releaseWakeLock() {
        wakeLock.release();
    }

    private void startNotificationUpdates() {
        handler.post(periodicNotificationUpdater);
    }

    private void stopNotificationUpdates() {
        handler.removeCallbacks(periodicNotificationUpdater);
    }

    private class PeriodicNotificationUpdater implements Runnable {
        @Override
        public void run() {
            int steps = pedometerSensor.getTotalStepCount();

            String text = "steps: " + steps;

            notificationManager.notify(Notifications.getNotificationId(), Notifications.getNotification(TourRecorderService.this, text));

            handler.postDelayed(this, 1000);
        }
    }
}
