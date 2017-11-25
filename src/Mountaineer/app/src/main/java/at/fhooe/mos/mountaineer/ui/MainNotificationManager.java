package at.fhooe.mos.mountaineer.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.model.TourDataFormatter;

/**
 * Created by stefan on 25.11.2017.
 */

public class MainNotificationManager {
    private static final String NOTIFICATION_CHANNEL_ID = "mountaineerNotifications2";
    private static final int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager = null;
    private NotificationCompat.Builder notificationBuilder = null;

    private static TourDataFormatter tourDataFormatter = TourDataFormatter.getInstance();

    public MainNotificationManager(Context context) {
        notificationManager = getNotificationManager(context);
        notificationBuilder = getNotificationBuilder(context);
    }

    public int getNotificationId() {
        return NOTIFICATION_ID;
    }

    public Notification getNotification() {
        return notificationBuilder.build();
    }

    public Notification getNotification(String text) {
        return notificationBuilder
                .setContentText(text)
                .build();
    }

    public Notification getNotification(Tour tour) {
        String text = "steps: " + tourDataFormatter.getTotalSteps(tour) + " - duration: " + tourDataFormatter.getDuration(tour);

        return getNotification(text);
    }

    public void showNotification(Tour tour) {
        notificationManager.notify(NOTIFICATION_ID, getNotification(tour));
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        createNotificationChannel(context);

        notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_walk)
                .setContentTitle("Mountaineer")
                .setContentText("recording")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(getTourActivityPendingIntent(context));

        return notificationBuilder;
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notifications for mountaineer", NotificationManager.IMPORTANCE_LOW);
            getNotificationManager(context).createNotificationChannel(channel);
        }
    }

    private NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent getTourActivityPendingIntent(Context context) {
        Intent resultIntent = new Intent(context, TourActivity_.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        return pendingIntent;
    }
}
