package com.visthome.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;
import com.visthome.doctor.entity.Doctors;

import java.util.List;
import java.util.Objects;

public class Register extends AppCompatActivity {

    EditText emailDoct;
    EditText username;
    EditText passwordDoct;
    EditText phoneNumber;
    TextView errorLogIn, Account;
    TextView forgotPass;
    Button logIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailDoct = findViewById(R.id.emailLogIn);
        passwordDoct = findViewById(R.id.passwordLogIn);
        errorLogIn = findViewById(R.id.errorLogin);
        Account = findViewById(R.id.Account);
        logIn = findViewById(R.id.ingiaLogIn);


        Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        logIn.setOnClickListener(view -> {
            String jina = username.getText().toString().trim();
            String email = emailDoct.getText().toString().trim();
            String phoneNo = phoneNumber.getText().toString().trim();
            String password = passwordDoct.getText().toString().trim();

            if (jina.isEmpty()) {
                username.setError("Enter name");
                username.requestFocus();
                return;
            }

            if (phoneNo.isEmpty()) {
                phoneNumber.setError("Enter Phone Number");
                phoneNumber.requestFocus();
                return;
            }

            if (phoneNo.length() < 10) {
                phoneNumber.setError("Ten digits required!");
                phoneNumber.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                emailDoct.setError("Enter email");
                emailDoct.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailDoct.setError("Invalid email!");
                emailDoct.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordDoct.setError("Enter password");
                passwordDoct.requestFocus();
                return;
            }

            if (password.length() < 8) {
                passwordDoct.setError("Eight characters required!");
                passwordDoct.requestFocus();
                return;
            }

            ProgressDialog dialog = new ProgressDialog(Register.this);
            dialog.setMessage("Please wait...");
            dialog.show();

            // Email is not registered
            // You can handle this case here
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // User registered successfully
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                if (currentUser != null) {
                                    FirebaseFirestore dbUser = FirebaseFirestore.getInstance();

                                    String userId = Objects.requireNonNull(user).getUid();

                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {

                                                    if (!task.isSuccessful()) {
                                                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }

                                                    // Get new FCM registration token
                                                    String token = task.getResult();

                                                    Doctors NewUser = new Doctors(jina, phoneNo, email, password, userId, token);

                                                    firestore.collection("Doctors")
                                                            .document(userId)
                                                            .set(NewUser)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                    // Data uploaded successfully
                                                                    // Handle success message or further actions
                                                                    errorLogIn.setText("Registration Successful, click the Log in button to sign in");
                                                                    //Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                                    dialog.dismiss();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Handle errors
                                                                    Log.e("TAG", "Error uploading user data for user ID " + userId + ": " + e.getMessage());
                                                                    Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                }
                                            });
                                }
                            }

                        }

                    });


        });

    }
}