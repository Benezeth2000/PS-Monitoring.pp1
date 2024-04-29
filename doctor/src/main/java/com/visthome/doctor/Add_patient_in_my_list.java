package com.visthome.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.ServerValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.visthome.doctor.entity.Patients;

import java.util.Date;
import java.util.Objects;

public class Add_patient_in_my_list extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_in_my_list);

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
        Button add = findViewById(R.id.addPatient);

        Intent intent = getIntent();
        String getCustomDate = intent.getStringExtra("customDate");

        scheduled.setText(getCustomDate);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DialogPlus dialogPlus = DialogPlus.newDialog(Add_patient_in_my_list.this)
                        .setContentHolder(new ViewHolder(R.layout.calender_cell))
                        .setExpanded(true, 550)
                        .create();
                dialogPlus.show();*/
                Intent intent = new Intent(Add_patient_in_my_list.this, Calender.class);
                startActivity(intent);
            }
        });

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
                String email = patientEmail.getText().toString();
                String pass = patientPass.getText().toString();
                String timeTomeet = scheduled.getText().toString();

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

                if (email.isEmpty()) {
                    patientEmail.setError("Enter diseases");
                    patientEmail.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    patientPass.setError("Enter diseases");
                    patientPass.requestFocus();
                    return;
                }

                if (timeTomeet.isEmpty()) {
                    scheduled.setError("select date");
                    scheduled.requestFocus();
                    return;
                }
                ProgressDialog dialog = new ProgressDialog(Add_patient_in_my_list.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User registered successfully
                                    FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();

                                    if (currentUser1 != null) {
                                        FirebaseFirestore dbUser = FirebaseFirestore.getInstance();

                                        DocumentReference doctorRef = dbUser.collection("Doctors").document(currentUser.getUid());

                                        doctorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    String doctorName = documentSnapshot.getString("doctorName");
                                                    String doctorUid = documentSnapshot.getString("doctorUid");
                                                    String doctorEmail = documentSnapshot.getString("email");

                                                    String userId = Objects.requireNonNull(currentUser1).getUid();

                                                    Patients createPatient = new Patients(
                                                            userId,
                                                            doctorName,
                                                            doctorUid,
                                                            doctorEmail,
                                                            uploadId,
                                                            Fname,
                                                            Mname,
                                                            Lname,
                                                            addr,
                                                            work,
                                                            phoneNo,
                                                            dises,
                                                            currentTime,
                                                            email,
                                                            pass,
                                                            currentDate,
                                                            timeTomeet
                                                    );

                                                    patientCollection.document(userId).

                                                            set(createPatient)
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
                                                                    patientPass.setText("");
                                                                    patientEmail.setText("");

                                                                    dialog.dismiss();
                                                                    Toast.makeText(Add_patient_in_my_list.this, "Patient added successful", Toast.LENGTH_LONG).show();
                                                                }
                                                            }).

                                                            addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(Add_patient_in_my_list.this, "Failed to add patient, try again ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        });


                                    }
                                }
                            }
                        });


            }
        });
    }
}