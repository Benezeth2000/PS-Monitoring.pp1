package com.visthome.doctor.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.visthome.doctor.R;
import com.visthome.doctor.entity.Patients;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Worker class that will be responsible for the periodic checks.
public class NotificationWorker extends Worker {
    private static final String TAG = "NotificationWorker";
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

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
                    String currentDateTime = sdf.format(new Date());

                    if (currentDateTime.equals(customDate + " " + customTime)) {
                        sendNotification(patient.getPatientUid());
                        sendBroadcastToOtherApp(patient.getPatientUid());
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

        // Show Toast and log the notification sending
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(getApplicationContext(), "Notification sent for " + patientName, Toast.LENGTH_SHORT).show();
        });
        Log.d("TAG", "Notification sent for " + patientName);
    }

    private void sendBroadcastToOtherApp(String patientName) {
        Intent intent = new Intent("com.visthome.PATIENT_REMINDER");
        intent.putExtra("patientName", patientName);
        getApplicationContext().sendBroadcast(intent);

        // Show Toast and log the broadcast sending
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(getApplicationContext(), "Broadcast sent for " + patientName, Toast.LENGTH_SHORT).show();
        });
        Log.d("TAGone", "Broadcast sent for " + patientName);
    }
}
