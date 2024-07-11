package com.visthome.psmonitoringapp.doktaActivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.visthome.psmonitoringapp.AlarmReceiver.AlarmReceive;
import com.visthome.psmonitoringapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Calender extends AppCompatActivity {
    private Button showDateTimeButton, setAlarmButton;
    private DatePicker datePicker;
    private TimePicker timePicker;
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

       /* showDateTimeButton = findViewById(R.id.showDateTimeButton);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);

        showDateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                toggleDateTimeVisibility();
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmMethod();
            }
        });*/

        CalendarView calendarView = findViewById(R.id.calendarView);
        //final TextView selectedDay = findViewById(R.id.selectedDay);
        //final TextView selectedMonth = findViewById(R.id.selectedMonth);
        //final TextView selectedYear = findViewById(R.id.selectedYear);
        final TextView textInput = findViewById(R.id.selectedYear);
        timePicker = findViewById(R.id.timePicker);
        //final TextView selectedTime = findViewById(R.id.selectedTime);
        final Button save = findViewById(R.id.save);
        final View dayContent = findViewById(R.id.dayContent);

        //Spinner timeSpinner = findViewById(R.id.selectedTime);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.times,
                android.R.layout.simple_spinner_item
        );

        /*timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = timePicker.getHour();
                int minut = timePicker.getMinute();

                selectedYear.setText(hour + " " + minut);
            }
        });*/

        /*timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/

        //List<String> calendarStrings = new ArrayList<>();

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

                Intent intent = new Intent(Calender.this, Add_patient_in_my_list.class);
                intent.putExtra("customDate", gotDate);
                intent.putExtra("gotTime", gotTime);
                startActivity(intent);
            }
        });

    }

   /* private void setAlarmMethod() {
        cancelPreviousAlarm();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

       Calendar calender = Calendar.getInstance();
       calender.set(year, month, day, hour, minute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Calender.this, AlarmReceive.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Calender.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);

        Toast.makeText(this, "Alarm set for " + day + "/" + (month +1) + "/" + year + "/" + " at " + hour + ":" + minute, Toast.LENGTH_LONG).show();

        Intent gotoAddPatient = new Intent(Calender.this, Add_patient_in_my_list.class);
        startActivity(gotoAddPatient);
    }

    private void cancelPreviousAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Calender.this, AlarmReceive.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Calender.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        if (pendingIntent != null){
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    private void toggleDateTimeVisibility() {
        int datevisibility = datePicker.getVisibility();
        int timevisibility = timePicker.getVisibility();

        if (datevisibility == View.GONE & timevisibility == View.GONE){

            datePicker.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.VISIBLE);
            setAlarmButton.setVisibility(View.VISIBLE);
        } else {

            datePicker.setVisibility(View.GONE);
            timePicker.setVisibility(View.GONE);
            setAlarmButton.setVisibility(View.GONE);
        }
    }
*/
}