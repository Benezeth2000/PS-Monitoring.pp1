package com.visthome.psmonitoringapp.doktaActivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.doktaAdapter.ListPatientAdapter;
import com.visthome.psmonitoringapp.entity.Doctors;
import com.visthome.psmonitoringapp.entity.Patients;
import com.visthome.psmonitoringapp.patientActivities.MainActivity;
import com.visthome.psmonitoringapp.patientActivities.UserDashboard;
import com.visthome.psmonitoringapp.service.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Lists_of_patients extends AppCompatActivity {

    ListPatientAdapter listPatientAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView signOut;
    LinearLayout home, settings;
    ImageView image;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_of_patients);

        ImageView addP = findViewById(R.id.addPatientInList);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_messageChats);
        signOut = findViewById(R.id.logOut);
        TextView username = findViewById(R.id.username);
        TextView time = findViewById(R.id.time);
        home = findViewById(R.id.home);
        settings = findViewById(R.id.settings);
        image = findViewById(R.id.image);

        // Get the current date and time
        //String currentDateTime = getCurrentDateTime();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        // Set the current date and time to the TextView
        time.setText(currentDateTime);

        mAuth = FirebaseAuth.getInstance();

        // Get the currently signed-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String doktaEmail = currentUser.getEmail();

        scheduleAlarm();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lists_of_patients.this, Lists_of_patients.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lists_of_patients.this, Doctor_profile.class);
                startActivity(intent);
            }
        });
        // Start the Notification Service
        //Ensure the Service starts when your app launches.
       // Intent serviceIntent = new Intent(this, NotificationService.class);
        //startService(serviceIntent);

        //Code for log out
        signOut.setOnClickListener((v) -> {
            SignOut();
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();

        String currentUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        db1.collection("Doctors").document(currentUserId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle errors
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Data exists for the current user
                            Doctors currentDoctor = snapshot.toObject(Doctors.class);
                            if (currentDoctor != null) {
                                username.setText(currentDoctor.getDoctorName());

                                String imageUrl = currentDoctor.getUserProfile();

                                if (imageUrl != null && !imageUrl.isEmpty()) {
                                    Glide.with(Lists_of_patients.this)
                                            .load(imageUrl)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(image);
                                } else {
                                    // Load default drawable image
                                    Glide.with(Lists_of_patients.this)
                                            .load(R.drawable.profilep) // Replace with your default image resource
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(image);
                                }
                            }
                        } else {
                            // Document doesn't exist
                        }
                    }
                });

        retrieveAll(doktaEmail);

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lists_of_patients.this, Add_patient_in_my_list.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveAll(String currentDoktaEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Patients")
                .whereEqualTo("doctorEmail", currentDoktaEmail);

        FirestoreRecyclerOptions<Patients> options = new FirestoreRecyclerOptions.Builder<Patients>()
                .setQuery(query, Patients.class).build();

        listPatientAdapter = new ListPatientAdapter(options, Lists_of_patients.this);
        LinearLayoutManager manager = new LinearLayoutManager(Lists_of_patients.this);
        manager.setReverseLayout(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listPatientAdapter);

        progressBar.setVisibility(View.VISIBLE);
        int progressBarDuration = 10000; // Set the duration in milliseconds (e.g., 10 seconds)
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
        }, progressBarDuration);

    }

    private void SignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("You want to Log out.?");

        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog progressBar = new ProgressDialog(Lists_of_patients.this);
                progressBar.setMessage("Please wait, while sign out...");
                progressBar.show();

                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(Lists_of_patients.this, MainActivity.class);
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
                Toast.makeText(Lists_of_patients.this, "Cancelled.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Lists_of_patients.this, Lists_of_patients.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

    private String getCurrentDateTime() {
        // Create a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm a", Locale.getDefault());
        // Get the current date and time
        return dateFormat.format(new Date());

        //Calendar calendar = Calendar.getInstance();
        // return dateFormat.format(calendar.getTime());
    }

    private void scheduleAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        long interval = 60 * 1000; // 1 minute in milliseconds
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
    }


    @Override
    public  void onStart() {
        super.onStart();
        if (listPatientAdapter!=null){
            listPatientAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listPatientAdapter!=null){
            listPatientAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listPatientAdapter != null) {
            listPatientAdapter.notifyDataSetChanged();
        }
    }

}