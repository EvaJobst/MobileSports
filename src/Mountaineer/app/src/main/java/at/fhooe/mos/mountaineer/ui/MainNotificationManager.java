package at.fhooe.mos.mountaineer.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.model.tour.TourDataFormatter;

/**
 * Created by stefan on 25.11.2017.
 */

public class MainNotificationManager {
    private static final String NOTIFICATION_CHANNEL_ID = "mountaineerNotifications2";
    private static final int NOTIFICATION_ID = 1;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private RemoteViews notificationRemoteViews;

    private static TourDataFormatter tourDataFormatter = TourDataFormatter.getInstance();

    public MainNotificationManager(Context context) {
        initNotificationManager(context);

        initNotificationRemoteViews(context);

        initNotificationChannel();

        initNotificationBuilder(context);
    }

    public int getNotificationId() {
        return NOTIFICATION_ID;
    }

    public Notification getNotification() {
        return notificationBuilder.build();
    }

    public Notification getNotification(Tour tour) {
        notificationRemoteViews.setTextViewText(R.id.firstTextView, tourDataFormatter.getDuration(tour));
        notificationRemoteViews.setTextViewText(R.id.secondTextView, tourDataFormatter.getTotalSteps(tour));

        return notificationBuilder.build();
    }

    public void showNotification(Tour tour) {
        notificationManager.notify(NOTIFICATION_ID, getNotification(tour));
    }

    private void initNotificationManager(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void initNotificationRemoteViews(Context context) {
        notificationRemoteViews = new RemoteViews(context.getPackageName(), R.layout.main_notification);
        notificationRemoteViews.setImageViewResource(R.id.firstImageView, R.drawable.ic_sun);
        notificationRemoteViews.setTextViewText(R.id.firstTextView, "00:00");
        notificationRemoteViews.setImageViewResource(R.id.secondImageView, R.drawable.ic_walk);
        notificationRemoteViews.setTextViewText(R.id.secondTextView, "0000");
    }

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notifications for mountaineer", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initNotificationBuilder(Context context) {
        notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        notificationBuilder
                .setSmallIcon(R.drawable.ic_walk)
                //.setContentTitle("Mountaineer")
                //.setContentText("recording")
                .setContent(notificationRemoteViews)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(getTourActivityPendingIntent(context));
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
