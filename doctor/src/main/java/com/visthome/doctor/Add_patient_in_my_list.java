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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.ServerValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.visthome.doctor.entity.Patients;

import java.util.Date;
import java.util.List;
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
        Button add = findViewById(R.id.addPatient);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                ProgressDialog dialog = new ProgressDialog(Add_patient_in_my_list.this);
                dialog.setMessage("Please wait...");
                dialog.show();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    List<String> signInMethods = task.getResult().getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        // Email is already registered
                                        // signInMethods list will contain the sign-in methods associated with this email
                                        // For example, if the email is registered with password authentication,
                                        // signInMethods will contain "password"
                                        // You can handle this case here
                                        //error.setText("email is already taken, try another email");
                                        Toast.makeText(Add_patient_in_my_list.this, "email is already taken, try another email", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    } else {
                                        // Email is not registered
                                        // You can handle this case here
                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // User registered successfully
                                                            FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();

                                                            if (currentUser1 != null) {
                                                                FirebaseFirestore dbUser = FirebaseFirestore.getInstance();

                                                                String userId = Objects.requireNonNull(currentUser1).getUid();

                                                                Patients createPatient = new Patients(
                                                                        userId,
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
                                                                        currentDate
                                                                );

                                                                patientCollection.document(uploadId).

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