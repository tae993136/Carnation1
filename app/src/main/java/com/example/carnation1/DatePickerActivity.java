package com.example.carnation1;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;
import com.example.carnation1.views.TimePickerHourOnly;

import org.json.simple.JSONObject;

import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DatePickerActivity extends AppCompatActivity {

    int year, month, day, hour;
    String parkingSpot;
    DatePicker datePicker;
    TimePickerHourOnly timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        datePicker = findViewById(R.id.datePicket_Date);
        timePicker = findViewById(R.id.datePicker_Time);

        var now = new GregorianCalendar();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);
        hour = now.get(Calendar.HOUR_OF_DAY);

        datePicker.setMinDate(now.getTimeInMillis());
        datePicker.setMaxDate(now.getTimeInMillis() + (2592000000L));
        timePicker.setHour24(hour);
        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            this.year = year;
            month = monthOfYear;
            day = dayOfMonth;
        });
    }


    public void sendReservation(View v) {
        var timeForReservation = new Calendar.Builder().setLocale(Locale.getDefault()).setDate(year, month, day).setTimeOfDay(timePicker.getHour24(), 0, 0).build();
        if (Instant.now().toEpochMilli() - timeForReservation.getTimeInMillis() >= 0) {
            Toast.makeText(this, "예약 시각이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parkingSpot", getIntent().getStringExtra("parkingSpot"));
        jsonObject.put("type", "reservation");
        jsonObject.put("year", Integer.toString(timeForReservation.get(Calendar.YEAR)));
        jsonObject.put("month", Integer.toString(timeForReservation.get(Calendar.MONTH) + 1));
        jsonObject.put("day", Integer.toString(timeForReservation.get(Calendar.DAY_OF_MONTH)));
        jsonObject.put("hour", Integer.toString(timeForReservation.get(Calendar.HOUR_OF_DAY)));
        jsonObject.put("sessionNumber", ServerConnection.sessionNumber);
        jsonObject.put("userNumber", ServerConnection.userNumber);
        JSONObject result = ServerConnection.send(jsonObject);
        if (result.get("result").toString().equals("OK")) {
            Toast.makeText(this, "예약 완료", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else if (result.get("data").toString().equals("Another active reservation exists")) {
            Toast.makeText(this, "이미 예약된 기록이 있습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "서버와의 연결이 원활하지 않습니다. 나중에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
