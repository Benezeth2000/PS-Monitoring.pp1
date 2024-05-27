package com.visthome.psmonitoringapp.doktaService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

//create a Service that will run in the background and check the conditions.
public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPeriodicTask();
        return START_STICKY;
    }

    private void startPeriodicTask() {
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES)
                        .build();
        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}