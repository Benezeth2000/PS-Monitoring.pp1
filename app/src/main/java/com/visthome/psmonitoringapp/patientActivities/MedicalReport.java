package com.visthome.psmonitoringapp.patientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.doktaActivities.Add_patient_in_my_list;

import java.net.URLEncoder;

public class MedicalReport extends AppCompatActivity {
    WebView webView;
    private ProgressBar progressBar;
    private static final String TAG = "PdfViewActivity";
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_report);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
       // webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        // Hide ProgressBar initially
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String uid = intent.getStringExtra("USER_UID");
        String email = intent.getStringExtra("USER_EMAIL");

        // Retrieve metadata from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert uid != null;
        db.collection("Patients").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (document != null && document.exists()) {
                            // Assuming your metadata document has a field "pdfPath" that stores the path to the PDF in Firebase Storage
                            String pdfPath = document.getString("medicalPdf");
                            if (pdfPath != null) {
                                Log.d("GOTONE", pdfPath);
                                try {
                                    String encodedUrl = URLEncoder.encode(pdfPath, "UTF-8");
                                    String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + encodedUrl;
                                    Log.d("googleDocsUrl", googleDocsUrl);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            super.onPageFinished(view, url);
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                    webView.loadUrl(googleDocsUrl);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error constructing Google Docs URL", e);
                                }
                            } else {
                                Log.e(TAG, "PDF path is null");
                            }
                        } else {
                            // Handle the case where the document does not exist
                            Log.e(TAG, "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
    }

    void loadPdfFromStorage(String pdfPath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pdfRef = storageRef.child(pdfPath);

        pdfRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String pdfUrl = uri.toString();
                String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;
                Log.d("GOOGLEURL", googleDocsUrl);
                webView.loadUrl(googleDocsUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });
    }
}