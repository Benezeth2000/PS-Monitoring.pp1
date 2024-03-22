package com.visthome.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.visthome.doctor.adapter.ListPatientAdapter;
import com.visthome.doctor.entity.Patients;

import java.util.ArrayList;
import java.util.List;

public class Lists_of_patients extends AppCompatActivity {
    ListPatientAdapter listPatientAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_of_patients);

        TextInputLayout addP = findViewById(R.id.textInputLayoutDash);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_messageChats);

        retrieveAll();

        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lists_of_patients.this, Add_patient_in_my_list.class);
                startActivity(intent);
            }
        });
    }

    private void retrieveAll() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Query query = db.collection("Patients");

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
        if (listPatientAdapter!=null){
            listPatientAdapter.notifyDataSetChanged();
        }
    }

}