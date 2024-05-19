package com.visthome.psmonitoringapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.visthome.PATIENT_REMINDER".equals(action)) {
            String patientName = intent.getStringExtra("patientName");
            // Handle the notification logic here (e.g., display a local notification)
        }
    }
}
