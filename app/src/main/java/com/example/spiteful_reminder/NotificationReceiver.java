package com.example.spiteful_reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

//public class NotificationReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        // Retrieve the reminder data from the intent or database
//        String reminderText = "Text"; // Get the reminder text from the intent or database
//
//        // Create and show the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "channel_id")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Reminder")
//                .setContentText(reminderText);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.notify(0, builder.build());
//        }
//    }
//}
//

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Broadcast Received!", Toast.LENGTH_SHORT).show();
        // Retrieve the reminder text from the intent or database
        String reminderText = intent.getStringExtra("memo"); // Assuming you passed the memo in the intent

        if (reminderText == null || reminderText.isEmpty()) {
            // Handle the case where reminderText is missing or empty
            reminderText = "Default Reminder Text";
        }

        // Define the notification channel
        String channelId = "your_channel_id";
        NotificationChannel channel = new NotificationChannel(channelId, "Your Channel Name", NotificationManager.IMPORTANCE_DEFAULT);

        // Create the notification manager and register the channel
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);

            // Use the memo as part of the notification ID
            int notificationId = reminderText.hashCode();

            // Create and show the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.applogo)
                    .setContentTitle("Reminder")
                    .setContentText(reminderText);

            notificationManager.notify(0, builder.build());


        } else {
            Toast.makeText(context, "HELL HAS COME!!!", Toast.LENGTH_SHORT).show();
            // Handle the case where notificationManager is null
        }
    }
}

