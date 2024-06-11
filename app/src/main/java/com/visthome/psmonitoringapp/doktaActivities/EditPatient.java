package com.visthome.psmonitoringapp.doktaActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.visthome.psmonitoringapp.R;

import java.util.HashMap;
import java.util.Map;

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
        String customeDate = intent.getStringExtra("customeDate");
        String customeTime = intent.getStringExtra("customeTime");
        String medicalPdf = intent.getStringExtra("medicalPdf");
        String pUID = intent.getStringExtra("pUID");

        firtName.setText(fname);
        middleName.setText(mname);
        lastName.setText(lname);
        job.setText(jobP);
        phoNo.setText(phoneNo);
        address.setText(adress);
        diseases.setText(Diseases);
        //patientEmail.setText(pEmail);
        //patientPass.setText(pPass);
        scheduled.setText(customeDate);
        time.setText(customeTime);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPatient.this, CalendaerEdit.class);
                intent.putExtra("fname", fname);
                intent.putExtra("mname", mname);
                intent.putExtra("lname", lname);
                intent.putExtra("adress", adress);
                intent.putExtra("job", jobP);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("diseases", Diseases);
                intent.putExtra("customeDate", customeDate);
                intent.putExtra("customeTime", customeTime);
                intent.putExtra("medicalPdf", medicalPdf);
                intent.putExtra("pUID", pUID);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog dialog = new ProgressDialog(EditPatient.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                /// Create a map with updated values
                Map<String, Object> updateMap = new HashMap<>();
                updateMap.put("firstName", firtName.getText().toString());
                updateMap.put("middleName", middleName.getText().toString());
                updateMap.put("lastName", lastName.getText().toString());
                updateMap.put("job", job.getText().toString());
                updateMap.put("phoneNo", phoNo.getText().toString());
                updateMap.put("address", address.getText().toString());
                updateMap.put("diseases", diseases.getText().toString());
                updateMap.put("customDate", scheduled.getText().toString());
                updateMap.put("customTime", time.getText().toString());

                // Initialize Firestore instance
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentRef = db.collection("Patients").document(pUID);

                // Update Firestore document with new values
                documentRef.update(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(EditPatient.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });

    }
}