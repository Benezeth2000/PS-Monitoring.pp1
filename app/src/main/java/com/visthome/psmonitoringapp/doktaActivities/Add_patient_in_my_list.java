package com.visthome.psmonitoringapp.doktaActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.AllUsers;
import com.visthome.psmonitoringapp.entity.Patients;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Add_patient_in_my_list extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;
    ;
    private Uri pdfUri;
    private static final int PICK_PDF_FILE = 2;
    private TextView pdfUrlTextView;

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
        TextView time = findViewById(R.id.time);
        Button add = findViewById(R.id.addPatient);
        pdfUrlTextView = findViewById(R.id.selectMedicalReport);

        Intent intent = getIntent();
        String getCustomDate = intent.getStringExtra("customDate");
        String gotTime = intent.getStringExtra("gotTime");

        scheduled.setText(getCustomDate);
        time.setText(gotTime);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Trigger file picker intent
        pdfUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialogPlus = DialogPlus.newDialog(Add_patient_in_my_list.this)
                        .setContentHolder(new ViewHolder(R.layout.calendar_layout))
                        .setExpanded(true, 550)
                        .create();
                dialogPlus.show();

                CalendarView calendarView = findViewById(R.id.calendarView);
                final TextView textInput = findViewById(R.id.selectedYear);
                TimePicker timePicker = findViewById(R.id.timePicker);
                final Button save = findViewById(R.id.save);
                final View dayContent = findViewById(R.id.dayContent);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        int currentMonth = month + 1;

                        String dayOfToday = year + "/" + currentMonth + "/" + dayOfMonth;

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        Date date = null;
                        try {
                            date = inputFormat.parse(dayOfToday);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(date);
                        textInput.setText(formattedDate);

                        if (dayContent.getVisibility()==view.GONE){
                            dayContent.setVisibility(View.VISIBLE);
                        }

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        // calendarStrings.add(String.valueOf(calendarView.getDate()));
                        // calendarStrings.add(textInput.getText().toString());
                        //textInput.setText("");

                        String gotDate = textInput.getText().toString();
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        // Determine AM/PM period
                        String period = hour < 12 ? "AM" : "PM";
                        // Convert hour from 24-hour format to 12-hour format
                /*if (hour > 12) {
                    hour -= 12;
                } else if (hour == 0) {
                    hour = 12;
                }*/

                        //String hourString = String.valueOf(hour);
                        String hourString = String.format("%02d", hour); // Zero-padded hour
                        String minuteString = String.format("%02d", minute);  // Zero-padded minute

                        // Concatenate hour, minute, and AM/PM
                        String gotTime = hourString + ":" + minuteString + " " + period;

                /*String gotDate = textInput.getText().toString();
                String gotTime = timeSpinner.getSelectedItem().toString();*/

                        Intent intent = new Intent(Add_patient_in_my_list.this, Add_patient_in_my_list.class);
                        intent.putExtra("customDate", gotDate);
                        intent.putExtra("gotTime", gotTime);
                        startActivity(intent);
                    }
                });

                /*Intent intent = new Intent(Add_patient_in_my_list.this, Calender.class);
                startActivity(intent);*/
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a Firestore reference
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                // Get reference to the businessAccount document
                CollectionReference patientCollection = db.collection("Patients");
                CollectionReference allUsers = db.collection("AllUsers");


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
                String Time = time.getText().toString();

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

                if (!isValidPhoneNumber(phoneNo)) {
                    phoNo.setError("Phone number must start with 06 or 07");
                    phoNo.requestFocus();
                    return;
                }

                if (dises.isEmpty()) {
                    diseases.setError("Enter diseases");
                    diseases.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    patientEmail.setError("Enter email");
                    patientEmail.requestFocus();
                    return;
                }

                if (!isValidEmail(email)) {
                    patientEmail.setError("Enter a valid email");
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
                String pdfUrlString = pdfUrlTextView.getText().toString();
                if (!pdfUrlString.isEmpty()) {
                    pdfUri = Uri.parse(pdfUrlString);

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registered successfully
                            FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();

                            if (currentUser1 != null) {
                                FirebaseFirestore dbUser = FirebaseFirestore.getInstance();

                                assert currentUser != null;
                                DocumentReference doctorRef = dbUser.collection("Doctors").document(currentUser.getUid());

                                doctorRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            String doctorName = documentSnapshot.getString("doctorName");
                                            String doctorUid = documentSnapshot.getString("doctorUid");
                                            String doctorEmail = documentSnapshot.getString("email");

                                            String userId = Objects.requireNonNull(currentUser1).getUid();

                                                StorageReference pdfRef = storageRef.child("patients/" + userId + "/" + pdfUri.getLastPathSegment());
                                                pdfRef.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri downloadUrl) {
                                                                String patientProfile = "";

                                                                Patients createPatient = new Patients(userId, doctorName, doctorUid, doctorEmail, uploadId, Fname, Mname, Lname, addr, work, phoneNo, dises, currentTime, email, pass, currentDate, timeTomeet, Time, downloadUrl.toString(), patientProfile);

                                                                String role = "patient";

                                                                AllUsers allUsers1 = new AllUsers(email, pass, userId, role);

                                                                patientCollection.document(userId).

                                                                        set(createPatient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {

                                                                                allUsers.document(userId).set(allUsers1).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        dialog.dismiss();
                                                                                        Toast.makeText(Add_patient_in_my_list.this, "Failed to add patient, try again ", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Add_patient_in_my_list.this, "Failed to add patient, try again ", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                dialog.dismiss();
                                                                Toast.makeText(Add_patient_in_my_list.this, "Failed to get download URL, try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog.dismiss();
                                                        Toast.makeText(Add_patient_in_my_list.this, "Failed to upload PDF, try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        }
                                    }
                                });


                            }
                        }
                    }
                });
            }else {
                dialog.dismiss();
                Toast.makeText(Add_patient_in_my_list.this, "No PDF file selected", Toast.LENGTH_SHORT).show();
            }


            }
        });
    }

    void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri pdfUri = data.getData();
                assert pdfUri != null;
                pdfUrlTextView.setText(pdfUri.toString());
            }
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("06") || phoneNumber.startsWith("07");
    }
    boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}