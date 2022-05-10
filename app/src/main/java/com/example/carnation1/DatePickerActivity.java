package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carnation1.Client.ServerConnection;

import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class DatePickerActivity extends AppCompatActivity {


    int mHour = 0, mMin = 0;
    int mYear = 0, mMonth = 0, mDay = 0;
    String parkingSpot;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
        Calendar calendar = new GregorianCalendar();


        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMin = calendar.get(Calendar.MINUTE);


        TimePicker timePicker = findViewById(R.id.vTimePicker);
        timePicker.setHour(mHour);
        timePicker.setMinute(mMin);
        timePicker.setOnTimeChangedListener((timePicker1, hour, minute) -> {
            mHour = hour;
            mMin = minute;
        });
        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);


    }


    public void sendReservation(View v) {
        Intent intent = new Intent(DatePickerActivity.this, MyPage.class);
        parkingSpot = intent.getStringExtra("parkingSpot");
        intent.putExtra("mYear", Integer.toString(mYear));
        intent.putExtra("mMonth", Integer.toString(mMonth));
        intent.putExtra("mDay", Integer.toString(mDay));
        intent.putExtra("mHour", Integer.toString(mHour));
        intent.putExtra("mMin", Integer.toString(mMin));
        // String msg = intent.getStringExtra("mYear");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parkingSpot", getIntent().getStringExtra("parkingSpot"));
        jsonObject.put("type", "reservation");
        jsonObject.put("year", Integer.toString(mYear));
        jsonObject.put("month", Integer.toString(mMonth));
        jsonObject.put("day", Integer.toString(mDay));
        jsonObject.put("hour", Integer.toString(mHour));
        jsonObject.put("sessionNumber", ServerConnection.sessionNumber);
        jsonObject.put("userNumber", ServerConnection.userNumber);
        ServerConnection.send(jsonObject);
        startActivity(intent);
        setResult(RESULT_OK, intent);
        // finish();
    }


    DatePicker.OnDateChangedListener mOnDateChangedListener =
            (datePicker, yy, mm, dd) -> {
                mYear = yy;
                mMonth = mm;
                mDay = dd;
            };


}
