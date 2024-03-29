package com.visthome.psmonitoringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText email = findViewById(R.id.emailLogIn);
        EditText password = findViewById(R.id.passwordLogIn);
        TextView errorLogIn = findViewById(R.id.errorLogin);
        TextView forgotPass = findViewById(R.id.forgotPass);
        Button logIn = findViewById(R.id.ingiaLogIn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString();
                String passwordUser = password.getText().toString();

                if (TextUtils.isEmpty(emailUser) || TextUtils.isEmpty(passwordUser)) {
                    Toast.makeText(MainActivity.this, "fill all fields!", Toast.LENGTH_LONG).show();
                }
                if (emailUser.isEmpty()) {
                    email.setError("Please enter email");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                    email.setError("Invalid email!");
                    email.requestFocus();
                    return;
                }

                if (passwordUser.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                    return;
                }


                ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Please wait...");
                dialog.show();


                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailUser, passwordUser)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                if (authResult != null && authResult.getUser() != null) {
                                    // User has valid credentials, start MainScreen activity
                                    startActivity(new Intent(MainActivity.this, UserDashboard.class));
                                    finish();
                                } else {
                                    // User credentials are invalid, show error message
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // An error occurred while trying to sign in, show error message
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "fail to log in!" + e, Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            Intent intent = new Intent(MainActivity.this, UserDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}