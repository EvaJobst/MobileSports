package at.fhooe.mos.mountaineer.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import at.fhooe.mos.mountaineer.R;

/**
 * Created by stefan on 25.11.2017.
 */

public class Notifications {
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "mountaineerNotifications2";

    private static NotificationCompat.Builder notificationBuilder = null;

    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }

    public static Notification getNotification(Context context) {
        return getNotificationBuilder(context).build();
    }

    public static Notification getNotification(Context context, String text) {
        return getNotificationBuilder(context)
                .setContentText(text)
                .build();
    }

    private static synchronized NotificationCompat.Builder getNotificationBuilder(Context context){

        if(notificationBuilder == null){
            createNotificationChannel(context);

            notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

            notificationBuilder
                    .setSmallIcon(R.drawable.ic_walk)
                    .setContentTitle("Mountaineer")
                    .setContentText("recording")
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(getTourActivityPendingIntent(context));
        }

        return notificationBuilder;
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notifications for mountaineer", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            getNotificationManager(context).createNotificationChannel(channel);
        }
    }

    private static NotificationManager getNotificationManager(Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private static PendingIntent getTourActivityPendingIntent(Context context){
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
