package com.visthome.psmonitoringapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.Patients;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationWorker extends Worker {
    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        createNotificationChannel(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("StartedWorker", "Worker has started.");
        // Perform the task of checking patients and sending notifications
        checkPatients();

        return Result.success();
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void checkPatients() {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userUID = currentUser.getUid();
            Log.d("FetchingPatients", "Fetching patients from Firestore. " + userEmail + " " + userUID);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Patients").document(userUID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Patients patient = document.toObject(Patients.class);

                        if (patient != null) {
                            String customDate = patient.getCustomDate();
                            String customTime = patient.getCustomTime();

                            String scheduledTime = customDate + " " + customTime;

                            Log.d("pDetails", scheduledTime);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                            String currentDateTime = sdf.format(new Date());

                            Log.d("NowDetails", currentDateTime);

                            try {
                                Date scheduledDate = sdf.parse(scheduledTime);
                                Date currentDate = sdf.parse(currentDateTime);

                                Log.d("currentDate", String.valueOf(currentDate));
                                Log.d("scheduledDate", String.valueOf(scheduledDate));

                                assert currentDate != null;
                                if (currentDate.equals(scheduledDate)) {
                                    sendNotification(getApplicationContext());
                                    Log.d("reached", "Scheduled time reached");
                                } else {
                                    Log.d("NotReached", "Scheduled time not reached");
                                }
                            } catch (Exception e) {
                                Log.e("DateParsingError", "Error parsing dates", e);
                            }
                        }
                    } else {
                        Log.d("FetchingPatients", "No such document");
                    }
                } else {
                    Log.d("FetchingPatients", "get failed with ", task.getException());
                }
            });
        } else {
            Log.d("FetchingPatients", "No user is currently signed in.");
        }
    }


    private void sendNotification(Context context) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Patient Reminder")
                .setContentText("It's time for " + patientName + " to take their medication")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());*/
        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)  // Replace with your app icon
                .setContentTitle("Patient Reminder")
                .setContentText("Reminder for patient: to see dokta ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        Log.d("sentNot", "Notification sent");
    }
}
