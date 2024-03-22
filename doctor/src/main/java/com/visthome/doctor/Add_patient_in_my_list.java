package com.visthome.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ServerValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.visthome.doctor.entity.Patients;

import java.util.Date;

public class Add_patient_in_my_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_in_my_list);

        EditText firtName = findViewById(R.id.firtName);
        EditText middleName = findViewById(R.id.middleName);
        EditText lastName = findViewById(R.id.lastName);
        EditText address= findViewById(R.id.address);
        EditText job = findViewById(R.id.job);
        EditText phoNo = findViewById(R.id.phoneNo);
        EditText diseases = findViewById(R.id.diseases);
        Button add = findViewById(R.id.addPatient);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a Firestore reference
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Get reference to the businessAccount document
                CollectionReference patientCollection = db.collection("Patients");


                String Fname = firtName.getText().toString();
                String Mname = middleName.getText().toString();
                String Lname = lastName.getText().toString();
                String addr = address.getText().toString();
                String work = job.getText().toString();
                String phoneNo = phoNo.getText().toString();
                String dises = diseases.getText().toString();

                String uploadId = db.collection("Patients").document().getId();
                Date currentDate = new Date(); // Get the current date and time
                String currentTime = ServerValue.TIMESTAMP.toString();

                if (Fname.isEmpty()) {
                    firtName.setError("Enter first name");
                    firtName.requestFocus();
                    return;
                }

                if (Mname.isEmpty()) {
                    middleName.setError("Enter middle name");
                    middleName.requestFocus();
                    return;
                }

                if (Lname.isEmpty()) {
                    lastName.setError("Enter last name");
                    lastName.requestFocus();
                    return;
                }

                if (addr.isEmpty()) {
                    address.setError("Enter address");
                    address.requestFocus();
                    return;
                }

                if (work.isEmpty()) {
                    job.setError("Enter job");
                    job.requestFocus();
                    return;
                }

                if (phoneNo.isEmpty()) {
                    phoNo.setError("Enter phone number");
                    phoNo.requestFocus();
                    return;
                }

                if (dises.isEmpty()) {
                    diseases.setError("Enter diseases");
                    diseases.requestFocus();
                    return;
                }

                ProgressDialog dialog = new ProgressDialog(Add_patient_in_my_list.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                Patients createPatient = new Patients(
                        uploadId,
                        Fname,
                        Mname,
                        Lname,
                        addr,
                        work,
                        phoneNo,
                        dises,
                        currentTime,
                        currentDate
                );

                patientCollection.document(uploadId).set(createPatient)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firtName.setText("");
                                middleName.setText("");
                                lastName.setText("");
                                job.setText("");
                                phoNo.setText("");
                                address.setText("");
                                diseases.setText("");

                                dialog.dismiss();
                                Toast.makeText(Add_patient_in_my_list.this, "Patient added successful", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(Add_patient_in_my_list.this, "Failed to add patient, try again ", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}