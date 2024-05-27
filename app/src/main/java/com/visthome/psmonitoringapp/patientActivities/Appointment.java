package com.visthome.psmonitoringapp.patientActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.visthome.psmonitoringapp.Adapter.ListAppointmentAdapter;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.Patients;

public class Appointment extends AppCompatActivity {
    ListAppointmentAdapter listAppointmentAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_messageChats);

        retrieveAll();
    }

    private void retrieveAll() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Patients");

        FirestoreRecyclerOptions<Patients> options = new FirestoreRecyclerOptions.Builder<Patients>()
                .setQuery(query, Patients.class).build();

        listAppointmentAdapter = new ListAppointmentAdapter(options, Appointment.this);
        LinearLayoutManager manager = new LinearLayoutManager(Appointment.this);
        manager.setReverseLayout(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listAppointmentAdapter);

        progressBar.setVisibility(View.VISIBLE);
        int progressBarDuration = 10000; // Set the duration in milliseconds (e.g., 10 seconds)
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
        }, progressBarDuration);

    }

    @Override
    public  void onStart() {
        super.onStart();
        if (listAppointmentAdapter!=null){
            listAppointmentAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listAppointmentAdapter!=null){
            listAppointmentAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAppointmentAdapter != null) {
            listAppointmentAdapter.notifyDataSetChanged();
        }
    }
}