package com.example.pepper2connect.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.example.pepper2connect.BroadcastReceiver.NotificationReminderBroadcast;

public class NotificationUtil {


    public static void setNotification(Context context, String strTitle, String strContent) {
        try {
            Global.getInstance().setContent(strContent);
            Global.getInstance().setTitle(strTitle);


            Intent intent = new Intent(context, NotificationReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        } catch (Exception ex) {
            String err = "";
            err = ex.getMessage();
            err +="";
        }
    }


    public static void createChannel(Context context, String channel_id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Pepper2Connect";
            String description = "Channel to notify Users new Information";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}