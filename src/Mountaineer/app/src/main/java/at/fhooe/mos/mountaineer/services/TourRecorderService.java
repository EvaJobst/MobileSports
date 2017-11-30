package at.fhooe.mos.mountaineer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import org.androidannotations.annotations.EService;

import at.fhooe.mos.mountaineer.FirebaseManager;
import at.fhooe.mos.mountaineer.PersistenceManager;
import at.fhooe.mos.mountaineer.sensors.pedometer.PedometerManager;
import at.fhooe.mos.mountaineer.sensors.stopwatch.Stopwatch;
import at.fhooe.mos.mountaineer.ui.MainNotificationManager;

/**
 * Created by stefan on 25.11.2017.
 */

@EService
public class TourRecorderService extends Service {
    public static final String TAG = TourRecorderService.class.getSimpleName();

    private static final int NOTIFICATION_PERIODIC_UPDATE_TIME_MS = 1000;

    private PowerManager.WakeLock wakeLock;
    private Handler handler;
    private PeriodicNotificationUpdater periodicNotificationUpdater;

    private TourDataCollector tourDataCollector;

    private MainNotificationManager mainNotificationManager;

    private PedometerManager pedometerManager;
    private Stopwatch stopwatch;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mainNotificationManager = new MainNotificationManager(this);

        handler = new Handler();
        periodicNotificationUpdater = new PeriodicNotificationUpdater();

        tourDataCollector = new TourDataCollector();

        pedometerManager = new PedometerManager(this);
        stopwatch = new Stopwatch();
        stopwatch.registerListener(tourDataCollector);

        acquireWakeLock();
        startForeground(mainNotificationManager.getNotificationId(), mainNotificationManager.getNotification());

        pedometerManager.setup(tourDataCollector);
        stopwatch.start();

        startNotificationUpdates();
    }

    @Override
    public void onDestroy() {
        stopNotificationUpdates();

        stopForeground(true);

        pedometerManager.destroy();
        stopwatch.stop();

        tourDataCollector.publishFinalTourData();

        releaseWakeLock();

        super.onDestroy();
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
        handler.postDelayed(periodicNotificationUpdater, NOTIFICATION_PERIODIC_UPDATE_TIME_MS);
    }

    private void stopNotificationUpdates() {
        handler.removeCallbacks(periodicNotificationUpdater);
    }

    private class PeriodicNotificationUpdater implements Runnable {
        @Override
        public void run() {
            mainNotificationManager.showNotification(tourDataCollector.getTour());

            handler.postDelayed(this, NOTIFICATION_PERIODIC_UPDATE_TIME_MS);
        }
    }
}
