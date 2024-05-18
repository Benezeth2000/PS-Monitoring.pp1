package com.visthome.psmonitoringapp.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    if (currentDateTime.equals(customDate + " " + customTime)) {
                        sendNotification(patient.getPatientUid());
                    }
                }
            }
        });
    }

    private void sendNotification(String patientName) {
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Patient Reminder")
                .setContentText("It's time for " + patientName + " to take their medication")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, builder.build());
    }
}
