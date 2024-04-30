package com.visthome.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.visthome.doctor.adapter.CalenderAdapter;
//import com.visthome.doctor.viewholder.CalenderViewHolder;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Calender extends AppCompatActivity {

    private Button next, previous;
    private TextView monthYearText;
    private RecyclerView calenderRecyclerView;
    private LocalDate selectedDate;

    //CalenderAdapter calenderAdapter;

    private int currentYear = 0;
    private int currentMonth = 0;
    private int currentDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        CalendarView calendarView = findViewById(R.id.calendarView);
        /*final TextView selectedDay = findViewById(R.id.selectedDay);
        final TextView selectedMonth = findViewById(R.id.selectedMonth);
        final TextView selectedYear = findViewById(R.id.selectedYear);*/
        final TextView textInput = findViewById(R.id.textInput);
        final Button save = findViewById(R.id.save);
        final View dayContent = findViewById(R.id.dayContent);


        List<String> calendarStrings = new ArrayList<>();

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

                Intent intent = new Intent(Calender.this, Add_patient_in_my_list.class);
                intent.putExtra("customDate", gotDate);
                startActivity(intent);
            }
        });

    }

}