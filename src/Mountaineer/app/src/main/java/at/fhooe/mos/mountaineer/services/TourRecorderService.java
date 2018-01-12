package at.fhooe.mos.mountaineer.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import org.androidannotations.annotations.EService;

import java.util.ArrayList;

import at.fhooe.mos.mountaineer.model.user.UserInformation;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.sensors.RealSensorFactory;
import at.fhooe.mos.mountaineer.sensors.Sensor;
import at.fhooe.mos.mountaineer.sensors.SensorFactory;
import at.fhooe.mos.mountaineer.sensors.SimulationSensorFactory;
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

    public TourDataCollector tourDataCollector;

    private MainNotificationManager mainNotificationManager;
    private PersistenceManager persistenceManager;

    private ArrayList<Sensor> sensors;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mainNotificationManager = new MainNotificationManager(this);
        persistenceManager = new PersistenceManager(this);

        handler = new Handler();
        periodicNotificationUpdater = new PeriodicNotificationUpdater();

        sensors = new ArrayList<>();
        SensorFactory sensorFactory;

        if (PersistenceManager.Get(this).getSimulateSensorData()) {
            sensorFactory = new SimulationSensorFactory();
        } else {
            sensorFactory = new RealSensorFactory();
        }

        sensors.add(sensorFactory.getStopwatch());
        sensors.add(sensorFactory.getLocationSensor());
        sensors.add(sensorFactory.getStepSensor());
        sensors.add(sensorFactory.getHeartRateSensor());

        tourDataCollector = new TourDataCollector();

        for (Sensor sensor : sensors) {
            sensor.setup(this);
            sensor.registerListener(tourDataCollector);
        }

        UserInformation userInfo = persistenceManager.getUserInformation();

        if(!userInfo.isCompleteAndValid()){
            Toast.makeText(this, "Info: User information not complete!", Toast.LENGTH_SHORT).show();
        }

        tourDataCollector.start(userInfo);

        acquireWakeLock();
        startForeground(mainNotificationManager.getNotificationId(), mainNotificationManager.getNotification());

        startNotificationUpdates();
    }

    @Override
    public void onDestroy() {
        stopNotificationUpdates();

        for (Sensor sensor : sensors) {
            sensor.destroy();
            sensor.removeListener(tourDataCollector);
        }

        tourDataCollector.stop();
        tourDataCollector.publishFinalTourData();

        stopForeground(true);
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
