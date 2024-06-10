package com.visthome.psmonitoringapp.doktaAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.Patients;

public class ViewPatientAdapter extends FirestoreRecyclerAdapter<Patients, ViewPatientAdapter.MedicalViewHolder> {
    public ViewPatientAdapter(@NonNull FirestoreRecyclerOptions<Patients> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewPatientAdapter.MedicalViewHolder medicalViewHolder, int i, @NonNull Patients patients) {

    }

    @NonNull
    @Override
    public ViewPatientAdapter.MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class MedicalViewHolder extends RecyclerView.ViewHolder {
        TextView medical;
        TextView concerns;
        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
           medical = itemView.findViewById(R.id.viewMedical);
            concerns = itemView.findViewById(R.id.concerns);
        }
    }
}
