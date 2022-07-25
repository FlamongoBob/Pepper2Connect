package com.example.pepper2connect.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import com.example.pepper2connect.MainActivity;
import com.example.pepper2connect.R;
import com.example.pepper2connect.utils.Global;

public class NotificationReminderBroadcast extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "Patient_Information";
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context
                        , NOTIFICATION_ID
                        , contentIntent
                        , PendingIntent.FLAG_IMMUTABLE);

        String title = Global.getInstance().getContent();
        String content = Global.getInstance().getContent();

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err +="";
        }
    }
}
