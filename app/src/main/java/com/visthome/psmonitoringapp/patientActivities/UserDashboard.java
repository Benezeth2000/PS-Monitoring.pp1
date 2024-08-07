package com.visthome.psmonitoringapp.patientActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.Patients;
import com.visthome.psmonitoringapp.service.NotificationReceiver;

import java.util.Calendar;
import java.util.Objects;

public class UserDashboard extends AppCompatActivity {

    TextView signOut, medicalReport;
    ImageView image;
    LinearLayout settings, home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        signOut = findViewById(R.id.logOut);
        medicalReport = findViewById(R.id.medicalReport);
        image = findViewById(R.id.image);
        settings = findViewById(R.id.settings);
        home = findViewById(R.id.home);
        TextView username = findViewById(R.id.username);
        TextView appointment = findViewById(R.id.appointment);

        scheduleAlarm();

        // Start the Notification Service
        //Ensure the Service starts when your app launches.
        //Intent serviceIntent = new Intent(this, NotificationReceiver.class);
        //startService(serviceIntent);

        //Code for log out
        signOut.setOnClickListener((v) -> {
            SignOut();
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, UserDashboard.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, PatientProfile.class);
                startActivity(intent);
            }
        });

        medicalReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current user from Firebase Authentication
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    String uid = user.getUid();
                    String email = user.getEmail();
                    Intent intent = new Intent(UserDashboard.this, MedicalReport.class);
                    intent.putExtra("USER_UID", uid);
                    intent.putExtra("USER_EMAIL", email);
                    startActivity(intent);
                } else {
                    // Handle the case where the user is not logged in
                    Toast.makeText(UserDashboard.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }

            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        db1.collection("Patients").document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle errors
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Data exists for the current user
                            Patients currentPatient = snapshot.toObject(Patients.class);
                            if (currentPatient != null) {
                                username.setText(currentPatient.getLastName());

                                String imageUrl = currentPatient.getPatientProfile();

                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(UserDashboard.this)
                                            .load(imageUrl)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(image);
                                } else {
                                    // Load default drawable image
                                    Glide.with(UserDashboard.this)
                                            .load(R.drawable.profilep) // Replace with your default image resource
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(image);
                                }
                            }
                        } else {
                            // Document doesn't exist
                            Toast.makeText(UserDashboard.this, "Does not exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, Appointment.class);
                startActivity(intent);
            }
        });
    }

    /*private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the time for the alarm to trigger (e.g., 8:00 AM)
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        long triggerTime = calendar.getTimeInMillis();

        // Set the repeating alarm to trigger at the specified time every day
        long interval = System.currentTimeMillis() + 60000; // 24 hours
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, pendingIntent);
    }*/

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        long interval = 60 * 1000; // 1 minute in milliseconds
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
    }


    private void SignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("You want to Log out.?");

        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog progressBar = new ProgressDialog(UserDashboard.this);
                progressBar.setMessage("Please wait, while sign out...");
                progressBar.show();

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(UserDashboard.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UserDashboard.this, "Cancelled.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserDashboard.this, UserDashboard.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}