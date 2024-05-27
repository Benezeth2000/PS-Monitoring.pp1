package com.visthome.psmonitoringapp.doktaActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.visthome.psmonitoringapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        //final TextView selectedTime = findViewById(R.id.selectedTime);
        final Button save = findViewById(R.id.save);
        final View dayContent = findViewById(R.id.dayContent);

        Spinner timeSpinner = findViewById(R.id.selectedTime);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.times,
                android.R.layout.simple_spinner_item
        );

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedTime = (String) parent.getItemAtPosition(position);
                // Here you can handle the selected time
                Toast.makeText(Calender.this, "Selected time: " + selectedTime, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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
                String gotTime = timeSpinner.getSelectedItem().toString();

                Intent intent = new Intent(Calender.this, Add_patient_in_my_list.class);
                intent.putExtra("customDate", gotDate);
                intent.putExtra("gotTime", gotTime);
                startActivity(intent);
            }
        });

    }

}