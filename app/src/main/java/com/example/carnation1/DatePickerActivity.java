package com.example.carnation1;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;
import com.example.carnation1.views.TimePickerHourOnly;

import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;


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
    }


    public void sendReservation(View v) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parkingSpot", getIntent().getStringExtra("parkingSpot"));
        jsonObject.put("type", "reservation");
        jsonObject.put("year", Integer.toString(year));
        jsonObject.put("month", Integer.toString(month));
        jsonObject.put("day", Integer.toString(day));
        jsonObject.put("hour", Integer.toString(hour));
        jsonObject.put("sessionNumber", ServerConnection.sessionNumber);
        jsonObject.put("userNumber", ServerConnection.userNumber);
        JSONObject result = ServerConnection.send(jsonObject);
        if (result.get("result").toString().equals("OK")) {
            Toast.makeText(this, "예약 완료", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "서버와의 연결이 원활하지 않습니다. 나중에 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
