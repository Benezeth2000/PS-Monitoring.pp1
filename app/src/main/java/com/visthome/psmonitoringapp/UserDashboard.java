package com.visthome.psmonitoringapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.visthome.psmonitoringapp.entity.Patients;

import java.util.Objects;

public class UserDashboard extends AppCompatActivity {

    TextView signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        signOut = findViewById(R.id.logOut);
        TextView username = findViewById(R.id.username);
        TextView appointment = findViewById(R.id.appointment);

        //Code for log out
        signOut.setOnClickListener((v) -> {
            SignOut();
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