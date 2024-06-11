package com.visthome.psmonitoringapp.doktaActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.Date;
import java.util.Locale;

public class CalendaerEdit extends AppCompatActivity {

    private Button showDateTimeButton, setAlarmButton;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button next, previous;
    private TextView monthYearText;
    private RecyclerView calenderRecyclerView;
    private LocalDate selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendaer_edit);

        CalendarView calendarView = findViewById(R.id.calendarView);
        //final TextView selectedDay = findViewById(R.id.selectedDay);
        //final TextView selectedMonth = findViewById(R.id.selectedMonth);
        //final TextView selectedYear = findViewById(R.id.selectedYear);
        final TextView textInput = findViewById(R.id.selectedYear);
        timePicker = findViewById(R.id.timePicker);
        //final TextView selectedTime = findViewById(R.id.selectedTime);
        final Button save = findViewById(R.id.save);
        final View dayContent = findViewById(R.id.dayContent);

        Intent intent = getIntent();
        //String getCustomDate = intent.getStringExtra("customDate");
        String fname = intent.getStringExtra("fname");
        String mname = intent.getStringExtra("mname");
        String lname = intent.getStringExtra("lname");
        String adress = intent.getStringExtra("adress");
        String jobP = intent.getStringExtra("job");
        String phoneNo = intent.getStringExtra("phoneNo");
        String Diseases = intent.getStringExtra("diseases");
        String pEmail = intent.getStringExtra("pEmail");
        String pPass = intent.getStringExtra("pPass");
        String customeDate = intent.getStringExtra("customeDate");
        String customeTime = intent.getStringExtra("customeTime");
        String medicalPdf = intent.getStringExtra("medicalPdf");
        String pUID = intent.getStringExtra("pUID");
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

                Intent intent = new Intent(CalendaerEdit.this, EditPatient.class);
                intent.putExtra("customDate", gotDate);
                intent.putExtra("gotTime", gotTime);
                intent.putExtra("fname", fname);
                intent.putExtra("mname", mname);
                intent.putExtra("lname", lname);
                intent.putExtra("adress", adress);
                intent.putExtra("job", jobP);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("diseases", Diseases);
                intent.putExtra("customeDate", customeDate);
                intent.putExtra("customeTime", customeTime);
                intent.putExtra("medicalPdf", medicalPdf);
                intent.putExtra("pUID", pUID);
                startActivity(intent);
            }
        });
    }
}