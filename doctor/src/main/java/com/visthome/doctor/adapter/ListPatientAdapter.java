package com.visthome.doctor.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.visthome.doctor.R;
import com.visthome.doctor.entity.Patients;

import java.util.Date;

public class ListPatientAdapter extends FirestoreRecyclerAdapter<Patients, ListPatientAdapter.PatientViewHolder> {
    Context context;

    public ListPatientAdapter(@NonNull FirestoreRecyclerOptions<Patients> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ListPatientAdapter.PatientViewHolder holder, int position, @NonNull Patients model) {

        holder.patientName.setText(model.getLastName());
        holder.address.setText(model.getAddress());
        holder.time.setText(getElapsedTime(model.getDate()));
        holder.status.setText(model.getCustomDate());
    }

    @NonNull
    @Override
    public ListPatientAdapter.PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_of_my_patients, parent, false);
        return new PatientViewHolder(view);
    }


    public class PatientViewHolder extends RecyclerView.ViewHolder {

        TextView patientName;
        TextView address;
        TextView status;
        TextView time;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patientName);
            address = itemView.findViewById(R.id.patientAddress);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.timeCreated);
        }
    }

    private String getElapsedTime(Date date) {
        long now = System.currentTimeMillis();
        long elapsedTime = now - date.getTime();
        CharSequence elapsedDisplay = DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
        return elapsedDisplay.toString();
    }
}
