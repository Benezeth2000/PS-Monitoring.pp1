package com.visthome.psmonitoringapp.patientActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.doktaActivities.Doctor_profile;
import com.visthome.psmonitoringapp.doktaActivities.Lists_of_patients;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class PatientProfile extends AppCompatActivity {
    LinearLayout home, settings;
    RoundedImageView profile;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    Uri selectedImageUri;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        TextView changeEmail = findViewById(R.id.changeEmail);
        TextView changePhoneNo = findViewById(R.id.changePhoneNo);
        home= findViewById(R.id.explore);
        profile= findViewById(R.id.profile);
        settings= findViewById(R.id.settings);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Get the currently logged-in user's UID
        String currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Patients").document(currentUserId);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("patientProfile")) {
                        String imageUri = documentSnapshot.getString("patientProfile");

                        // Call a method to load the image into ImageView
                        loadImageIntoImageView(imageUri);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        if (data != null && data.getData() != null) {
                            // Get the selected image URI
                            selectedImageUri = result.getData().getData();

                            Glide.with(PatientProfile.this)
                                    .load(selectedImageUri)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(profile);

                            if (selectedImageUri != null) {
                                // Now, directly send the details to the database
                                sendImageToDatabase(selectedImageUri);
                            }

                        }
                    }
                }
        );

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, PatientProfile.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, PatientProfile.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(view -> ImagePicker.with(PatientProfile.this).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent(new Function1<Intent, Unit>() {
                    @Override
                    public Unit invoke(Intent intent) {
                        imagePickerLauncher.launch(intent);
                        return null;
                    }
                }));

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PatientProfile.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProgressDialog dialog = new ProgressDialog(PatientProfile.this);
                        dialog.setMessage("Please wait...");
                        dialog.show();

                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(PatientProfile.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(PatientProfile.this, "Check your email", Toast.LENGTH_LONG).show();
                                    //errorLogIn.setText("Check your email inbox for password reset link");
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(PatientProfile.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        changePhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogPlus dialogPlus = DialogPlus.newDialog(PatientProfile.this)
                        .setContentHolder(new ViewHolder(R.layout.change_phone_number))
                        .setExpanded(true)
                        .create();
                dialogPlus.show();

                // Get the root view of the dialog content
                View dialogView = dialogPlus.getHolderView();

                EditText phoneChange = dialogView.findViewById(R.id.phoneChange);
                Button update = dialogView.findViewById(R.id.savePhone);

                // Get the currently logged-in user's UID
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Create a DatabaseReference to point to the "Users" collection and the specific user's node using the UID
                // Define Firestore DocumentReference for the current user
                DocumentReference currentUserReference = db.collection("Patients").document(currentUserId);

                currentUserReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Handle errors
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // DocumentSnapshot data exists
                            //String em = documentSnapshot.getString("email");
                            String PhoneNo = documentSnapshot.getString("phoneNo");

                            // Use the retrieved data
                            //assert PhoneNo != null;
                            assert PhoneNo != null;
                            if (PhoneNo.equals("")) {
                                phoneChange.setText("No phone number");
                            } else {
                                phoneChange.setText(PhoneNo);
                            }

                        } else {
                            // Document does not exist or there's an error
                        }
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ProgressDialog dialog = new ProgressDialog(PatientProfile.this);
                        dialog.setMessage("Please wait...");
                        dialog.show();

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        String currentUserId = currentFirebaseUser.getUid();

                        // Get the new email and password from the input fields
                        //String newEmail = email.getText().toString();
                        String newPhoneNo = phoneChange.getText().toString();

                        // Update the email address
                        // Now update the password
                        // Update the values in the database
                        Map<String, Object> map = new HashMap<>();
                        map.put("phoneNo", newPhoneNo);

                        // Save the new data to the "users" collection
                        // Update the values in the database
                        DocumentReference userRef = db.collection("Patients").document(currentUserId);
                        userRef.update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(PatientProfile.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PatientProfile.this, "Update Unsuccessfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });


            }
        });
    }

    private void sendImageToDatabase(Uri selectedImageUri) {
        ProgressDialog dialog = new ProgressDialog(PatientProfile.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        // Get the currently logged-in user's UID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentRef = db.collection("Patients").document(currentUserId);

        // Check if there's an image selected
        if (selectedImageUri != null && !selectedImageUri.toString().isEmpty()) {

            // Get reference to Firebase Storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference("patientProfile");

            // Create a reference to the image in Firebase Storage
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));

            // Upload the image to Firebase Storage
            fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get the download URL of the uploaded image
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Update the image URL in Firestore
                            documentRef
                                    .update("patientProfile", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(PatientProfile.this, "Profile updated successfully", Toast.LENGTH_LONG).show();
                                            // Close any dialogs or perform other actions after successful image update
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PatientProfile.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                    // Handle failure scenarios if needed
                    dialog.dismiss();
                }
            });
        } else {
            // If no image selected, just display the success message
            Toast.makeText(PatientProfile.this, "No image selected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void loadImageIntoImageView(String imageUrl) {
        RoundedImageView imageView = findViewById(R.id.profile);

        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}