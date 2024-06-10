package com.visthome.psmonitoringapp.doktaAdapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.doktaActivities.EditPatient;
import com.visthome.psmonitoringapp.doktaActivities.ViewPatientMedical;
import com.visthome.psmonitoringapp.entity.Patients;

import java.util.Date;
import java.util.List;

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
        holder.status.setText(model.getCustomDate() + " at " + model.getCustomTime());

        String pID = model.getPatientUid();
        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewMore = new Intent(context, ViewPatientMedical.class);
                viewMore.putExtra("name", model.getLastName());
                viewMore.putExtra("fname", model.getFirstName());
                viewMore.putExtra("diseases", model.getDiseases());
                viewMore.putExtra("medicalPdf", model.getMedicalPdf());
                context.startActivity(viewMore);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(context, EditPatient.class);
                edit.putExtra("fname", model.getFirstName());
                edit.putExtra("mname", model.getMiddleName());
                edit.putExtra("lname", model.getLastName());
                edit.putExtra("adress", model.getAddress());
                edit.putExtra("job", model.getJob());
                edit.putExtra("phoneNo", model.getPhoneNo());
                edit.putExtra("diseases", model.getDiseases());
                edit.putExtra("pEmail", model.getPatientEmail());
                edit.putExtra("pPass", model.getPatientPass());
                edit.putExtra("medicalPdf", model.getMedicalPdf());
                context.startActivity(edit);
            }
        });
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
        TextView time, viewMore, edit;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.patientName);
            address = itemView.findViewById(R.id.patientAddress);
            status = itemView.findViewById(R.id.status);
            time = itemView.findViewById(R.id.timeCreated);
            viewMore = itemView.findViewById(R.id.viewMore);
            edit = itemView.findViewById(R.id.edit);
        }
    }

    private String getElapsedTime(Date date) {
        long now = System.currentTimeMillis();
        long elapsedTime = now - date.getTime();
        CharSequence elapsedDisplay = DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
        return elapsedDisplay.toString();
    }
}
