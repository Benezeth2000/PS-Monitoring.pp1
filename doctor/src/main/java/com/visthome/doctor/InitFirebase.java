package com.visthome.doctor;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class InitFirebase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

    }
}
