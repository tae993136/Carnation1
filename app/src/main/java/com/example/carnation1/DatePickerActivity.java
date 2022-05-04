package com.example.carnation1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private TextView textView;
    //private int mHour = 0, mMin = 0;
    private int mYear = 0, mMonth = 0, mDay = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date_picker);
        Calendar calendar = new GregorianCalendar();
        Date date = new Date();

        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        textView = findViewById(R.id.textView);
        timePicker = findViewById(R.id.TimePicker);


        DatePicker datePicker = findViewById(R.id.vDatePicker);
        datePicker.init(mYear, mMonth, mDay, mOnDateChangedListener);

        timePicker.setOnTimeChangedListener (new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                textView.setText(hourOfDay + "시" + minute + "분");
            }
        });


    }



    public void mOnClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("mYear", mYear);
        intent.putExtra("mMonth", mMonth);
        intent.putExtra("mDay", mDay);
        setResult(RESULT_OK, intent);
        finish();
    }


    DatePicker.OnDateChangedListener mOnDateChangedListener =
            new DatePicker.OnDateChangedListener() {

        @Override

        public void onDateChanged(DatePicker datePicker, int yy, int mm, int dd) {
            mYear = yy;
            mMonth = mm;
            mDay = dd;
        }

    };


}
