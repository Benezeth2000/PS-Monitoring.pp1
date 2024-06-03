package com.visthome.psmonitoringapp.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.visthome.psmonitoringapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "patient_reminder_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        // Enqueue the NotificationWorker to perform the patient check
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        WorkManager.getInstance(context).enqueue(workRequest);
        // Create and show the notification
        //createNotificationChannel(context);
        //createNotification(context);
    }

    private void createNotification(Context context) {
        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)  // Replace with your app icon
                .setContentTitle("Patient Reminder")
                .setContentText("Reminder for patient: ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel for API 26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Patient Reminder Channel";
            String description = "Channel for patient reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
