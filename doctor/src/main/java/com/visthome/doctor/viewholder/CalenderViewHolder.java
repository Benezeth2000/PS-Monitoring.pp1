package com.visthome.doctor.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visthome.doctor.R;
import com.visthome.doctor.adapter.CalenderAdapter;

public class CalenderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView dayOfMonth;
    private final CalenderAdapter.OnItemListener onItemListener;

    public CalenderViewHolder(@NonNull View itemView, CalenderAdapter.OnItemListener onItemListener) {
        super(itemView);

        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemListener.onItemClick(getBindingAdapterPosition(), (String) dayOfMonth.getText());
    }
}
