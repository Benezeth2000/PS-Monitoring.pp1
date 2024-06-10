package com.visthome.psmonitoringapp.doktaActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.visthome.psmonitoringapp.R;

public class EditPatient extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    private Uri pdfUri;
    private static final int PICK_PDF_FILE = 2;
    private TextView pdfUrlTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

        EditText firtName = findViewById(R.id.firtName);
        EditText middleName = findViewById(R.id.middleName);
        EditText lastName = findViewById(R.id.lastName);
        EditText address = findViewById(R.id.address);
        EditText job = findViewById(R.id.job);
        EditText phoNo = findViewById(R.id.phoneNo);
        EditText diseases = findViewById(R.id.diseases);
        EditText patientEmail = findViewById(R.id.patientEmail);
        EditText patientPass = findViewById(R.id.passwordUser);
        TextView selectDate = findViewById(R.id.selectDate);
        TextView scheduled = findViewById(R.id.scheduled);
        TextView time = findViewById(R.id.time);
        Button add = findViewById(R.id.addPatient);
        pdfUrlTextView = findViewById(R.id.selectMedicalReport);


        Intent intent = getIntent();
        //String getCustomDate = intent.getStringExtra("customDate");
        String fname = intent.getStringExtra("fname");
        String mname = intent.getStringExtra("mname");
        String lname = intent.getStringExtra("lname");
        String adress = intent.getStringExtra("adress");
        String jobP = intent.getStringExtra("job");
        String phoneNo = intent.getStringExtra("phoneNo");
        String Diseases = intent.getStringExtra("diseases");
        String pEmail = intent.getStringExtra("pEmail");
        String pPass = intent.getStringExtra("pPass");
        String medicalPdf = intent.getStringExtra("medicalPdf");

        firtName.setText(fname);
        middleName.setText(mname);
        lastName.setText(lname);
        job.setText(jobP);
        phoNo.setText(phoneNo);
        address.setText(adress);
        diseases.setText(Diseases);
        patientEmail.setText(pEmail);
        patientPass.setText(pPass);


    }
}