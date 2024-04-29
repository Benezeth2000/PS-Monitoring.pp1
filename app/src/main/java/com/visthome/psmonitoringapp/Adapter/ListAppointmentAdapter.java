package com.visthome.psmonitoringapp.Adapter;

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
import com.visthome.psmonitoringapp.R;
import com.visthome.psmonitoringapp.entity.Patients;

import java.util.Date;

public class ListAppointmentAdapter extends FirestoreRecyclerAdapter<Patients, ListAppointmentAdapter.AppointmentViewHolder> {
    Context context;

    public ListAppointmentAdapter(@NonNull FirestoreRecyclerOptions<Patients> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListAppointmentAdapter.AppointmentViewHolder holder, int position, @NonNull Patients model) {
        holder.doctorName.setText(model.getDoctorName());
        holder.timeCreated.setText(getElapsedTime(model.getDate()));
        holder.timeOfApp.setText(model.getCustomDate());
    }

    @NonNull
    @Override
    public ListAppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_appointments, parent, false);
        return new AppointmentViewHolder(view);
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView doctorName;
        TextView timeOfApp;
        TextView timeCreated;
        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.doctor);
            timeOfApp = itemView.findViewById(R.id.timeOfApp);
            timeCreated = itemView.findViewById(R.id.timeCreatedApp);
        }
    }

    private String getElapsedTime(Date date) {
        long now = System.currentTimeMillis();
        long elapsedTime = now - date.getTime();
        CharSequence elapsedDisplay = DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.MINUTE_IN_MILLIS);
        return elapsedDisplay.toString();
    }
}
