package com.visthome.psmonitoringapp.doktaActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.visthome.psmonitoringapp.R;

public class ViewPatientMedical extends AppCompatActivity {
TextView name, medical, concerns;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_medical);

        name = findViewById(R.id.name);
        medical = findViewById(R.id.viewMedical);
        concerns = findViewById(R.id.concerns);

        Intent intent = getIntent();
        String fName = intent.getStringExtra("fname");
        String lName = intent.getStringExtra("name");
        String diseases = intent.getStringExtra("diseases");
        String medicalPdf = intent.getStringExtra("medicalPdf");
        String USER_UID = intent.getStringExtra("USER_UID");

        name.setText(fName + " " + lName);
        concerns.setText(diseases);
        //medical.setText(medicalPdf);

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMore = new Intent(ViewPatientMedical.this, ViewReport.class);
                viewMore.putExtra("USER_UID", USER_UID);
            }
        });

    }
}