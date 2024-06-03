package com.visthome.psmonitoringapp.service;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
    }

    @NonNull
    @Override
    public Result doWork() {
        // Perform the task of checking patients and sending notifications
        checkPatients();

        return Result.success();
    }

    private void checkPatients() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Patients").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Patients patient = document.toObject(Patients.class);

                    String customDate = patient.getCustomDate();
                    String customTime = patient.getCustomTime();

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    if (currentDateTime.equals(customDate + " " + customTime)) {
                        sendNotification(patient.getLastName());
                    }
                }
            }
        });
    }

    private void sendNotification(String patientName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Patient Reminder")
                .setContentText("It's time for " + patientName + " to take their medication")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
